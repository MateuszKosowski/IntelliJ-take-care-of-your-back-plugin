package com.github.mateuszkosowski.intellijtakecareofyourbackplugin

import java.awt.*
import javax.swing.*
import javax.swing.border.LineBorder
import javax.swing.plaf.basic.BasicButtonUI

class BreakOverlay(private val onSkip: () -> Unit) : JWindow() {
    private val label = JLabel(MyBundle.message("overlay.defaultMessage"), SwingConstants.CENTER)
    private val skipButton = JButton(MyBundle.message("overlay.skipButton").uppercase())

    init {
        contentPane.background = Color.BLACK
        layout = BorderLayout()

        val centerPanel = JPanel(GridBagLayout())
        centerPanel.isOpaque = false

        val gbc = GridBagConstraints()
        gbc.gridy = 0
        gbc.fill = GridBagConstraints.NONE


        val boxDimension = Dimension(450, 600)

        gbc.gridx = 0
        gbc.weightx = 1.0
        centerPanel.add(Box.createHorizontalGlue(), gbc)

        // --- JAVA CONTAINER---
        gbc.gridx = 1
        gbc.weightx = 0.0

        val javaWrapper = JPanel(BorderLayout())
        javaWrapper.isOpaque = false
        javaWrapper.preferredSize = boxDimension

        val javaLogo = RotatingLogo("/img/java-programming-language-java-logo-free-png.png")
        javaWrapper.add(javaLogo, BorderLayout.CENTER)

        centerPanel.add(javaWrapper, gbc)

        // --- TEXT ---
        gbc.gridx = 2
        gbc.weightx = 0.0
        gbc.insets = Insets(0, 40, 0, 40)

        label.foreground = Color.WHITE
        label.font = Font("Arial", Font.BOLD, 48)
        centerPanel.add(label, gbc)

        // --- CAT CONTAINER ---
        gbc.gridx = 3
        gbc.weightx = 0.0
        gbc.insets = Insets(0, 0, 0, 0)

        val catWrapper = JPanel(GridBagLayout())
        catWrapper.isOpaque = false
        catWrapper.preferredSize = boxDimension

        val catUrl = javaClass.getResource("/img/cat-dance-dancing-cat.gif")
        if (catUrl != null) {
            val catLabel = JLabel("<html><img src='${catUrl}' width='250' height='440'></html>")
            catWrapper.add(catLabel)
        }

        centerPanel.add(catWrapper, gbc)

        gbc.gridx = 4
        gbc.weightx = 1.0
        centerPanel.add(Box.createHorizontalGlue(), gbc)

        add(centerPanel, BorderLayout.CENTER)

        // --- Button ---
        val buttonPanel = JPanel(GridBagLayout())
        buttonPanel.background = Color.BLACK
        buttonPanel.border = BorderFactory.createEmptyBorder(0, 0, 100, 0)

        skipButton.isFocusable = false
        skipButton.font = Font("Impact", Font.PLAIN, 32)
        skipButton.setUI(BasicButtonUI())
        skipButton.horizontalAlignment = SwingConstants.CENTER
        skipButton.isContentAreaFilled = true
        skipButton.isOpaque = true
        skipButton.background = Color(255, 215, 0)
        skipButton.foreground = Color.BLACK

        skipButton.preferredSize = Dimension(400, 80)

        skipButton.border = LineBorder(Color.BLACK, 4)

        skipButton.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)

        skipButton.addActionListener { onSkip() }

        val bGbc = GridBagConstraints()
        bGbc.gridy = 0
        bGbc.gridx = 0
        buttonPanel.add(skipButton, bGbc)

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