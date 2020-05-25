package com.markskelton.settings

import com.intellij.ide.BrowserUtil.browse
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.layout.panel
import com.markskelton.settings.ThemeSettings.Companion.constructSettingModel
import java.net.URI
import javax.swing.JComponent

data class ThemeSettingsModel(
  var isBold: Boolean,
  var isVivid: Boolean,
  var isItalic: Boolean
)

class ThemeSettingsUI : DumbAware, SearchableConfigurable {

  companion object {
    const val THEME_SETTINGS_DISPLAY_NAME = "One Dark Theme"
    private const val REPOSITORY = "https://github.com/one-dark/jetbrains-one-dark-theme"
    private val CHANGELOG_URI = URI("$REPOSITORY/blob/master/CHANGELOG.md#changelog")
    private val ISSUES_URI = URI("$REPOSITORY/issues")
    private val MARKETPLACE_URI = URI("https://plugins.jetbrains.com/plugin/11938-one-dark-theme/reviews")
  }

  override fun getId(): String = "com.markskelton.ThemeSettings"

  override fun getDisplayName(): String =
    THEME_SETTINGS_DISPLAY_NAME

  private val initialThemeSettingsModel = constructSettingModel()

  private val themeSettingsModel = initialThemeSettingsModel.copy()

  override fun isModified(): Boolean {
    return initialThemeSettingsModel != themeSettingsModel
  }

  override fun apply() {
    persistChanges()
    ApplicationManager.getApplication().messageBus.syncPublisher(
      THEME_CONFIG_TOPIC
    ).themeConfigUpdated(ThemeSettings.instance)
  }

  private fun persistChanges() {
    registerSettingsChange(themeSettingsModel.isBold, {
      ThemeSettings.instance.isBold
    }) {
      ThemeSettings.instance.isBold = it
    }
    registerSettingsChange(themeSettingsModel.isVivid, {
      ThemeSettings.instance.isVivid
    }) {
      ThemeSettings.instance.isVivid = it
    }
    registerSettingsChange(themeSettingsModel.isItalic, {
      ThemeSettings.instance.isItalic
    }) {
      ThemeSettings.instance.isItalic = it
    }
  }

  override fun createComponent(): JComponent? =
    createSettingsPane()

  private fun createSettingsPane(): DialogPanel =
    panel {
      titledRow("Main Settings") {
//        row {
//          cell {
//            checkBox(
//              "Bold Characters",
//              themeSettingsModel.isBold,
//              comment = "Uses bold fonts for certain language keywords",
//              actionListener = { _, component ->
//                themeSettingsModel.isBold = component.isSelected
//              }
//            )
//          }
//        }
        row {
          cell {
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
            checkBox(
              "Vivid Palette",
              themeSettingsModel.isVivid,
              comment = "Uses the One Dark vivid color palette",
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
            button("Report an Issue") {
              browse(ISSUES_URI)
            }
            button("View Changelog") {
              browse(CHANGELOG_URI)
            }
            button("Leave a Review") {
              browse(MARKETPLACE_URI)
            }
          }
        }
      }
    }
}

fun <T> registerSettingsChange(setValue: T, getStoredValue: () -> T, onChanged: (T) -> Unit) {
  if (getStoredValue() != setValue) {
    onChanged(setValue)
  }
}
