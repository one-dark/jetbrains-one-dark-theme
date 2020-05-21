package com.markskelton

import com.intellij.openapi.application.ApplicationManager
import com.intellij.util.messages.MessageBusConnection
import com.markskelton.settings.THEME_CONFIG_TOPIC
import com.markskelton.settings.ThemeConfigListener
import com.markskelton.settings.ThemeSettings

object OneDarkThemeManager {
  private lateinit var messageBus: MessageBusConnection

  fun registerStartup() {
    if (!this::messageBus.isInitialized) {
      messageBus = ApplicationManager.getApplication().messageBus.connect()
      messageBus.subscribe(THEME_CONFIG_TOPIC, object : ThemeConfigListener {
        override fun themeConfigUpdated(themeSettings: ThemeSettings) {
          println("Theme settings changed! $themeSettings")
        }
      })
    }
  }
}