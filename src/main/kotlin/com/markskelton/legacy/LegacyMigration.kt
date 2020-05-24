package com.markskelton.legacy

import com.intellij.ide.ui.laf.LafManagerImpl
import com.intellij.ide.ui.laf.UIThemeBasedLookAndFeelInfo
import com.intellij.openapi.application.ApplicationManager
import com.markskelton.OneDarkThemeManager
import com.markskelton.notification.Notifications
import com.markskelton.settings.THEME_CONFIG_TOPIC
import com.markskelton.settings.ThemeSettings

enum class LegacyThemes {
  ITALIC, VIVID, VIVID_ITALIC
}

object LegacyMigration {
  private val legacyThemes = mapOf(
    "1a92aa6f-c2f1-4994-ae01-6a78e43eeb24" to LegacyThemes.ITALIC,
    "4b6007f7-b596-4ee2-96f9-968d3d3eb392" to LegacyThemes.VIVID,
    "4f556d32-83cb-4b8b-9932-c4eccc4ce3af" to LegacyThemes.VIVID_ITALIC
  )

  fun isLegacyTheme(laf: UIThemeBasedLookAndFeelInfo): Boolean = legacyThemes.containsKey(laf.theme.id)

  fun migrateIfNecessary() {
    migrateUser()
  }

  fun migrateAndNotifyUserOfDeprecation() {
    ApplicationManager.getApplication().invokeLater {
      Notifications.displayDeprecationMessage()
    }
    migrateUser()
  }

  private fun migrateUser() {
    val legacyTheme = LafManagerImpl.getInstance().currentLookAndFeel
    if (legacyTheme is UIThemeBasedLookAndFeelInfo && isLegacyTheme(legacyTheme)) {
      LafManagerImpl.getInstance().setCurrentLookAndFeel(LafManagerImpl.getInstance().installedLookAndFeels
        .filterIsInstance<UIThemeBasedLookAndFeelInfo>()
        .first {
          it.theme.id == OneDarkThemeManager.ONE_DARK_ID
        }
      )
      when (legacyThemes[legacyTheme.theme.id]) {
        LegacyThemes.VIVID_ITALIC -> applyVividItalicSettings()
        LegacyThemes.VIVID -> applyVividSettings()
        LegacyThemes.ITALIC -> applyItalicSettings()
      }
      ApplicationManager.getApplication().invokeLater {
        ApplicationManager.getApplication().messageBus.syncPublisher(
          THEME_CONFIG_TOPIC
        ).themeConfigUpdated(ThemeSettings.instance)
      }
    }
  }

  private fun applyItalicSettings() {
    ThemeSettings.instance.isItalic = true
    ThemeSettings.instance.isVivid = false
  }

  private fun applyVividSettings() {
    ThemeSettings.instance.isVivid = true
    ThemeSettings.instance.isItalic = false
  }

  private fun applyVividItalicSettings() {
    ThemeSettings.instance.isItalic = true
    ThemeSettings.instance.isVivid = true
  }
}