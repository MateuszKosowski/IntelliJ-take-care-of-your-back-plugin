package com.github.mateuszkosowski.intellijtakecareofyourbackplugin.startup

import com.github.mateuszkosowski.intellijtakecareofyourbackplugin.services.MyProjectService
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.openapi.components.service

class MyProjectActivity : ProjectActivity {
    override suspend fun execute(project: Project) {
        val service = project.service<MyProjectService>()
        service.startTimer()
    }
}