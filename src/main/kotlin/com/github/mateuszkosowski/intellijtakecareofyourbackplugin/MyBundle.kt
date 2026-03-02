package com.github.mateuszkosowski.intellijtakecareofyourbackplugin

import com.intellij.DynamicBundle

private const val BUNDLE = "messages.MyBundle"

object MyBundle : DynamicBundle(BUNDLE) {
    @JvmStatic
    fun message(key: String, vararg params: Any): String = getMessage(key, *params)
}

