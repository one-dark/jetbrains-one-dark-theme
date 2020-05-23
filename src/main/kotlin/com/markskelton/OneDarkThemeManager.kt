package com.markskelton

import com.intellij.ide.ui.LafManagerListener
import com.intellij.ide.ui.laf.LafManagerImpl
import com.intellij.ide.ui.laf.TempUIThemeBasedLookAndFeelInfo
import com.intellij.ide.ui.laf.UIThemeBasedLookAndFeelInfo
import com.intellij.openapi.application.ApplicationManager
import com.intellij.util.messages.MessageBusConnection
import com.markskelton.legacy.LegacyMigration.isLegacyTheme
import com.markskelton.settings.THEME_CONFIG_TOPIC
import com.markskelton.settings.ThemeConfigListener
import com.markskelton.settings.ThemeSettings

object OneDarkThemeManager {
  private lateinit var messageBus: MessageBusConnection
  const val ONE_DARK_ID = "f92a0fa7-1a98-47cd-b5cb-78ff67e6f4f3"

  fun registerStartup() {
    if (!this::messageBus.isInitialized) {
      applyConfigurableTheme()
      messageBus = ApplicationManager.getApplication().messageBus.connect()
      messageBus.subscribe(THEME_CONFIG_TOPIC, object : ThemeConfigListener {
        override fun themeConfigUpdated(themeSettings: ThemeSettings) {
          if (isCurrentTheme()) {
            LafManagerImpl.getInstance().setCurrentLookAndFeel(
              ThemeConstructor.constructNewTheme(themeSettings)
            )
          }
        }
      })

      messageBus.subscribe(LafManagerListener.TOPIC, LafManagerListener {
        val currentLaf = it.currentLookAndFeel
        if (currentLaf is UIThemeBasedLookAndFeelInfo &&
          currentLaf !is TempUIThemeBasedLookAndFeelInfo
          && isOneDarkTheme(currentLaf)) {
          setConstructedOneDarkTheme()
        }
      })
    }
  }

  private fun isOneDarkTheme(uiThemeBasedLookAndFeelInfo: UIThemeBasedLookAndFeelInfo): Boolean {
    return uiThemeBasedLookAndFeelInfo.theme.id == ONE_DARK_ID || isLegacyTheme(uiThemeBasedLookAndFeelInfo)
  }

  private fun applyConfigurableTheme() {
    if (isCurrentTheme()) {
      setConstructedOneDarkTheme()
    }
  }

  private fun setConstructedOneDarkTheme() {
    LafManagerImpl.getInstance().setCurrentLookAndFeel(
      ThemeConstructor.constructNewTheme(ThemeSettings.instance)
    )
  }

  fun isCurrentTheme(): Boolean =
    when (val currentLaf = LafManagerImpl.getInstance().currentLookAndFeel) {
      is UIThemeBasedLookAndFeelInfo -> currentLaf.theme.id == ONE_DARK_ID
      is TempUIThemeBasedLookAndFeelInfo -> currentLaf.theme.id == ONE_DARK_ID
      else -> false
    }
}