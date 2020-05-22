package com.markskelton

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import com.markskelton.legacy.LegacyMigration

class OneDarkTheme : StartupActivity, DumbAware {
  override fun runActivity(project: Project) {
    LegacyMigration.migrateIfNecessary()
    OneDarkThemeManager.registerStartup()
  }
}
