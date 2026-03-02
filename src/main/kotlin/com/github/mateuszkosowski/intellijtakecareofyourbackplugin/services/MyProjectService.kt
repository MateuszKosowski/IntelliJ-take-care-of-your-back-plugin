package com.github.mateuszkosowski.intellijtakecareofyourbackplugin.services

import com.github.mateuszkosowski.intellijtakecareofyourbackplugin.BreakOverlay
import com.github.mateuszkosowski.intellijtakecareofyourbackplugin.MyBundle
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.WindowManager
import com.intellij.util.Consumer
import java.awt.Component
import java.awt.event.MouseEvent
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import javax.swing.SwingUtilities

@Service(Service.Level.PROJECT)
class MyProjectService(private val project: Project) {
    private val scheduler = Executors.newSingleThreadScheduledExecutor()
    private var overlay: BreakOverlay? = null

    private var isPaused = false

    private val secondsPassed = AtomicInteger(0)
    private var isTimerStarted = false

    private val WORK_TIME = 60
    private val BREAK_TIME = 15

    private var currentStatusText = MyBundle.message("status.waiting")

    fun startTimer() {
        if (isTimerStarted) return
        isTimerStarted = true

        SwingUtilities.invokeLater {
            val statusBar = WindowManager.getInstance().getStatusBar(project)
            if (statusBar?.getWidget("BackSaverWidget") == null) {
                statusBar?.addWidget(BackSaverWidget(), project)
            }
        }

        scheduler.scheduleAtFixedRate({ runLogic() }, 0, 1, TimeUnit.SECONDS)
    }

    private fun runLogic() {

        if (isPaused) {
            currentStatusText = MyBundle.message("status.paused")
            SwingUtilities.invokeLater {
                WindowManager.getInstance().getStatusBar(project)?.updateWidget("BackSaverWidget")
            }
            return
        }

        val currentSeconds = secondsPassed.incrementAndGet()

        val remainingWork = WORK_TIME - currentSeconds
        if (remainingWork >= 0) {
            val mins = remainingWork / 60
            val secs = remainingWork % 60
            currentStatusText = MyBundle.message("status.timeLeftUntilBreak", String.format("%02d:%02d", mins, secs))

            SwingUtilities.invokeLater {
                WindowManager.getInstance().getStatusBar(project)?.updateWidget("BackSaverWidget")
            }
        }


        if (currentSeconds > WORK_TIME && currentSeconds <= WORK_TIME + BREAK_TIME) {
            val remainingBreak = (WORK_TIME + BREAK_TIME) - currentSeconds
            showOverlay(MyBundle.message("overlay.break", remainingBreak))
        }

        else if (currentSeconds > WORK_TIME + BREAK_TIME) {
            resetTimer()
        }
    }

    private fun showOverlay(msg: String) {
        SwingUtilities.invokeLater {
            if (overlay == null) {
                overlay = BreakOverlay { resetTimer() }
                overlay?.isVisible = true
            }
            overlay?.updateMessage(msg)
        }
    }

    private fun resetTimer() {
        secondsPassed.set(0)

        SwingUtilities.invokeLater {
            if (overlay != null) {
                overlay?.isVisible = false
                overlay?.dispose()
                overlay = null
            }

            WindowManager.getInstance().getStatusBar(project)?.updateWidget("BackSaverWidget")
        }
    }

    inner class BackSaverWidget : StatusBarWidget, StatusBarWidget.TextPresentation {
        override fun ID(): String = "BackSaverWidget"
        override fun getPresentation() = this
        override fun getText(): String = currentStatusText
        override fun getAlignment() = Component.CENTER_ALIGNMENT
        override fun getTooltipText() = MyBundle.message("status.tooltip")
        override fun install(statusBar: StatusBar) {}
        override fun dispose() {}
        override fun getClickConsumer(): Consumer<MouseEvent> = Consumer {
            isPaused = !isPaused
            if (!isPaused) {
                currentStatusText = MyBundle.message("status.waiting")
            }
            WindowManager.getInstance().getStatusBar(project)?.updateWidget("BackSaverWidget")
        }
    }
}