package com.markskelton.legacy

import com.intellij.configurationStore.FileBasedStorage
import com.intellij.ide.ui.laf.LafManagerImpl
import com.intellij.ide.ui.laf.UIThemeBasedLookAndFeelInfo
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.components.RoamingType
import com.markskelton.OneDarkThemeManager
import com.markskelton.doOrElse
import com.markskelton.settings.GroupStyling
import com.markskelton.settings.THEME_CONFIG_TOPIC
import com.markskelton.settings.ThemeSettings
import com.markskelton.toOptional
import com.markskelton.utils.readXmlInputStream
import groovy.util.Node
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

enum class LegacyThemes {
  ITALIC, VIVID, VIVID_ITALIC
}

object LegacyMigration {
  private val legacyThemes = mapOf(
    "1a92aa6f-c2f1-4994-ae01-6a78e43eeb24" to LegacyThemes.ITALIC,
    "4b6007f7-b596-4ee2-96f9-968d3d3eb392" to LegacyThemes.VIVID,
    "4f556d32-83cb-4b8b-9932-c4eccc4ce3af" to LegacyThemes.VIVID_ITALIC
  )

  fun migrateIfNecessary() {
    migrateUser()
  }

  private fun migrateUser() {
    Files.newInputStream(Paths.get(PathManager.getConfigPath(), "options", "laf.xml"))
      .use {
        readXmlInputStream(it)
      }.breadthFirst()
      .filterIsInstance<Node>()
      .first { it.attribute("name") == "LafManager" }
      .toOptional()
      .map { it.children().filterIsInstance<Node>().first { child -> child.attribute("themeId") != null } }
      .map { it.attribute("themeId") }
      .filter { it is String }
      .map { it as String }
      .filter { legacyThemes.containsKey(it) }
      .ifPresent { legacyThemeId ->
        attemptToMigrateToOneDarkTheme(legacyThemeId)
      }
  }

  private fun attemptToMigrateToOneDarkTheme(legacyThemeId: String, attempt: Int = 0) {
    if (attempt < 3) {
      getOneDarkTheme()
        .doOrElse({ oneDarkTheme ->
          LafManagerImpl.getInstance().setCurrentLookAndFeel(oneDarkTheme)
          when (legacyThemes[legacyThemeId]) {
            LegacyThemes.VIVID_ITALIC -> applyVividItalicSettings()
            LegacyThemes.VIVID -> applyVividSettings()
            LegacyThemes.ITALIC -> applyItalicSettings()
          }
          ApplicationManager.getApplication().invokeLater {
            ApplicationManager.getApplication().messageBus.syncPublisher(
              THEME_CONFIG_TOPIC
            ).themeConfigUpdated(ThemeSettings.instance)
          }
        }) {
          ApplicationManager.getApplication().invokeLater {
            attemptToMigrateToOneDarkTheme(legacyThemeId, attempt + 1)
          }
        }
    }
  }

  private fun getOneDarkTheme(): Optional<UIThemeBasedLookAndFeelInfo> =
    LafManagerImpl.getInstance().installedLookAndFeels
      .filterIsInstance<UIThemeBasedLookAndFeelInfo>()
      .firstOrNull {
        it.theme.id == OneDarkThemeManager.ONE_DARK_ID
      }.toOptional()

  private fun applyItalicSettings() {
    ThemeSettings.instance.attributesStyle = GroupStyling.ITALIC.value
    ThemeSettings.instance.commentStyle = GroupStyling.ITALIC.value
    ThemeSettings.instance.keywordStyle = GroupStyling.ITALIC.value
    ThemeSettings.instance.isVivid = false
  }

  private fun applyVividSettings() {
    ThemeSettings.instance.attributesStyle = GroupStyling.REGULAR.value
    ThemeSettings.instance.commentStyle = GroupStyling.REGULAR.value
    ThemeSettings.instance.keywordStyle = GroupStyling.REGULAR.value
    ThemeSettings.instance.isVivid = true
  }

  private fun applyVividItalicSettings() {
    ThemeSettings.instance.attributesStyle = GroupStyling.ITALIC.value
    ThemeSettings.instance.commentStyle = GroupStyling.ITALIC.value
    ThemeSettings.instance.keywordStyle = GroupStyling.ITALIC.value
    ThemeSettings.instance.isVivid = true
  }
}
