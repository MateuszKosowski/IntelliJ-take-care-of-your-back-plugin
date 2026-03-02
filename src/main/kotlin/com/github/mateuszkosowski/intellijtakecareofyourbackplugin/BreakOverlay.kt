package com.github.mateuszkosowski.intellijtakecareofyourbackplugin

import java.awt.*
import javax.swing.*

class BreakOverlay(private val onSkip: () -> Unit) : JWindow() {
    private val label = JLabel(MyBundle.message("overlay.defaultMessage"), SwingConstants.CENTER)
    private val skipButton = JButton(MyBundle.message("overlay.skipButton"))

    init {
        contentPane.background = Color.BLACK

        label.foreground = Color.WHITE
        label.font = Font("Arial", Font.BOLD, 48)

        skipButton.isFocusable = false
        skipButton.background = Color.DARK_GRAY
        skipButton.foreground = Color.WHITE
        skipButton.addActionListener { onSkip() }

        layout = BorderLayout()
        add(label, BorderLayout.CENTER)

        val buttonPanel = JPanel()
        buttonPanel.background = Color.BLACK
        buttonPanel.add(skipButton)
        add(buttonPanel, BorderLayout.SOUTH)

        isAlwaysOnTop = true
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        setSize(screenSize.width, screenSize.height)
        setLocation(0, 0)
    }

    fun updateMessage(text: String) {
        label.text = text
    }
}