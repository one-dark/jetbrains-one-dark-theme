package com.markskelton.settings

import com.intellij.ide.BrowserUtil.browse
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.layout.panel
import java.net.URI
import javax.swing.JComponent
import javax.swing.JLabel

data class ThemeSettingsModel(
    var isBold: Boolean,
    var isVivid: Boolean,
    var isItalic: Boolean
)

class ThemeSettingsUI : SearchableConfigurable {

  companion object {
    const val THEME_SETTINGS_DISPLAY_NAME = "One Dark Theme Settings"
    private const val REPOSITORY = "https://github.com/one-dark/jetbrains-one-dark-theme"
    val CHANGELOG_URI = URI("$REPOSITORY/blob/master/CHANGELOG.md")
    val ISSUES_URI = URI("$REPOSITORY/issues")
    val MARKETPLACE_URI = URI("https://plugins.jetbrains.com/plugin/11938-one-dark-theme")
  }

  override fun getId(): String = "com.markskelton.ThemeSettings"

  override fun getDisplayName(): String =
      THEME_SETTINGS_DISPLAY_NAME

  private val initialThemeSettingsModel = ThemeSettingsModel(
      ThemeSettings.instance.isBold,
      ThemeSettings.instance.isVivid,
      ThemeSettings.instance.isItalic
  )

  private val themeSettingsModel = initialThemeSettingsModel.copy()

  override fun isModified(): Boolean {
    return initialThemeSettingsModel != themeSettingsModel
  }

  override fun apply() {
    ApplicationManager.getApplication().messageBus.syncPublisher(
        THEME_CONFIG_TOPIC
    ).themeConfigUpdated(ThemeSettings.instance)
  }

  override fun createComponent(): JComponent? =
      createSettingsPane()

  private fun createSettingsPane(): DialogPanel {
    val directoryIcon = JLabel()
//    directoryIcon.icon = ImageIcon(javaClass.getResource("/icons/settings/directoryIcon.png"))
    val fileIcon = JLabel()
//    fileIcon.icon = ImageIcon(javaClass.getResource("/icons/settings/fileIcon.png"))
    val psiIcon = JLabel()
//    psiIcon.icon = ImageIcon(javaClass.getResource("/icons/settings/psiIcon.png"))
    return panel {
      titledRow("Main Settings") {
        row {
          cell {
            directoryIcon()
            checkBox(
                "Bold Characters",
                themeSettingsModel.isBold,
                comment = "Uses bold fonts for certain language keywords",
                actionListener = { _, component ->
                  themeSettingsModel.isBold = component.isSelected
                }
            )
          }
        }
        row {
          cell {
            fileIcon()
            checkBox(
                "Italic Characters",
                themeSettingsModel.isItalic,
                comment = "Uses italic font for language keywords and comments",
                actionListener = { _, component ->
                  themeSettingsModel.isItalic = component.isSelected
                }
            )
          }
        }
        row {
          cell {
            psiIcon()
            checkBox(
                "Vivid Pallette",
                themeSettingsModel.isVivid,
                comment = "Uses the One-Dark vivid color pallette",
                actionListener = { _, component ->
                  themeSettingsModel.isVivid = component.isSelected
                }
            )
          }
        }
      }
      titledRow("Miscellaneous Items") {
        row {
          cell {
            button("View Issues") {
              browse(ISSUES_URI)
            }
            button("View Changelog") {
              browse(CHANGELOG_URI)
            }
            button("Marketplace Homepage") {
              browse(MARKETPLACE_URI)
            }
          }
        }
      }
    }
  }
}