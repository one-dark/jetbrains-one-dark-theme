package com.markskelton

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import io.sentry.Sentry

class OneDarkTheme : StartupActivity, DumbAware {

  init {
    Sentry.init("https://cb598170e51a44adbf0079abe2d79624@o403546.ingest.sentry.io/5267019?maxmessagelength=50000")
  }


  override fun runActivity(project: Project) {
    OneDarkThemeManager.registerStartup(project)
  }
}
