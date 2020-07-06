package com.markskelton

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity

class OneDarkTheme : StartupActivity, DumbAware {

  override fun runActivity(project: Project) {
    OneDarkThemeManager.registerStartup(project)
  }
}
