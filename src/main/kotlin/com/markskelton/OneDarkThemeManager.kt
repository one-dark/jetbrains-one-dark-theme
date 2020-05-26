package com.markskelton

import com.intellij.ide.ui.LafManagerListener
import com.intellij.ide.ui.laf.LafManagerImpl
import com.intellij.ide.ui.laf.TempUIThemeBasedLookAndFeelInfo
import com.intellij.ide.ui.laf.UIThemeBasedLookAndFeelInfo
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.colors.EditorColorsListener
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.project.Project
import com.intellij.util.messages.MessageBusConnection
import com.markskelton.notification.CURRENT_VERSION
import com.markskelton.notification.Notifications
import com.markskelton.settings.THEME_CONFIG_TOPIC
import com.markskelton.settings.ThemeConfigListener
import com.markskelton.settings.ThemeSettings

object OneDarkThemeManager {
  private lateinit var messageBus: MessageBusConnection
  const val ONE_DARK_ID = "f92a0fa7-1a98-47cd-b5cb-78ff67e6f4f3"

  fun registerStartup(project: Project) {
    if (!this::messageBus.isInitialized) {
      attemptToDisplayUpdates()

      applyConfigurableTheme()

      subscribeToEvents()
    }
  }

  private fun attemptToDisplayUpdates() {
    if (ThemeSettings.instance.version != CURRENT_VERSION) {
      ThemeSettings.instance.version = CURRENT_VERSION
      ThemeSettings.instance.customSchemeSet = false
      ApplicationManager.getApplication().invokeLater {
        Notifications.displayUpdateNotification()
      }
    }
  }

  private fun subscribeToEvents() {
    messageBus = ApplicationManager.getApplication().messageBus.connect()
    messageBus.subscribe(THEME_CONFIG_TOPIC, object : ThemeConfigListener {
      override fun themeConfigUpdated(themeSettings: ThemeSettings) {
        if (isCurrentTheme()) {
          ThemeSettings.instance.customSchemeSet = false
          LafManagerImpl.getInstance().setCurrentLookAndFeel(
            ThemeConstructor.constructNewTheme(themeSettings)
          )
        }
      }
    })

    messageBus.subscribe(LafManagerListener.TOPIC, LafManagerListener {
      val currentLaf = it.currentLookAndFeel
      if (currentLaf is UIThemeBasedLookAndFeelInfo) {
        when {
          currentLaf !is TempUIThemeBasedLookAndFeelInfo &&
            isOneDarkTheme(currentLaf) -> setOneDarkTheme()
        }
      }
    })

    messageBus.subscribe(EditorColorsManager.TOPIC, EditorColorsListener {
      ThemeSettings.instance.customSchemeSet =
        it != null && !it.metaProperties.containsKey("oneDarkScheme")
    })
  }

  private fun isOneDarkTheme(uiThemeBasedLookAndFeelInfo: UIThemeBasedLookAndFeelInfo): Boolean =
    uiThemeBasedLookAndFeelInfo.theme.id == ONE_DARK_ID

  private fun applyConfigurableTheme() {
    if (isCurrentTheme() && !ThemeSettings.instance.customSchemeSet) {
      setOneDarkTheme()
    }
  }

  private fun setOneDarkTheme() {
    LafManagerImpl.getInstance().setCurrentLookAndFeel(
      ThemeConstructor.useExistingTheme()
    )
  }

  fun isCurrentTheme(): Boolean =
    when (val currentLaf = LafManagerImpl.getInstance().currentLookAndFeel) {
      is UIThemeBasedLookAndFeelInfo -> currentLaf.theme.id == ONE_DARK_ID
      is TempUIThemeBasedLookAndFeelInfo -> currentLaf.theme.id == ONE_DARK_ID
      else -> false
    }
}