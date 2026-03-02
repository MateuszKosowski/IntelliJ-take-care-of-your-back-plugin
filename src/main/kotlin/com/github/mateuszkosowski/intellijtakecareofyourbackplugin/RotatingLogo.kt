package com.github.mateuszkosowski.intellijtakecareofyourbackplugin

import java.awt.*
import javax.swing.*

class RotatingLogo(imagePath: String) : JPanel() {
    private val iconUrl = javaClass.getResource(imagePath)
    private val image: Image? = if (iconUrl != null) ImageIcon(iconUrl).image else null
    private var angle = 0.0

    private val imgWidth = image?.getWidth(null) ?: 387
    private val imgHeight = image?.getHeight(null) ?: 522

    init {
        isOpaque = false
        Timer(30) {
            angle += 0.05
            repaint()
        }.start()
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        val g2d = g as Graphics2D
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)

        val w = width
        val h = height
        if (w < 10 || h < 10) return

        g2d.translate(w / 2, h / 2)
        g2d.rotate(angle)

        image?.let {
            val diagonal = Math.hypot(imgWidth.toDouble(), imgHeight.toDouble())

            val maxAvailableSpace = Math.min(w, h).toDouble()

            val scale = (maxAvailableSpace / diagonal) * 0.9

            val scaledW = (imgWidth * scale).toInt()
            val scaledH = (imgHeight * scale).toInt()

            g2d.drawImage(it, -scaledW / 2, -scaledH / 2, scaledW, scaledH, null)
        }
    }
}