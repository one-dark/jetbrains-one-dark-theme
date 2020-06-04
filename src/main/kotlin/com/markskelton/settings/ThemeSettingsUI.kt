package com.markskelton.settings

import com.intellij.ide.BrowserUtil.browse
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.layout.panel
import com.markskelton.settings.ThemeSettings.Companion.constructSettingModel
import java.net.URI
import java.util.*
import javax.swing.DefaultComboBoxModel
import javax.swing.JComponent

data class ThemeSettingsModel(
  var commentStyle: GroupStyling,
  var keywordStyle: GroupStyling,
  var attributesStyle: GroupStyling,
  var isVivid: Boolean
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

  private var initialThemeSettingsModel = constructSettingModel()

  private val themeSettingsModel = initialThemeSettingsModel.copy()

  override fun isModified(): Boolean {
    return initialThemeSettingsModel != themeSettingsModel
  }

  override fun apply() {
    persistChanges()
    ApplicationManager.getApplication().messageBus.syncPublisher(
      THEME_CONFIG_TOPIC
    ).themeConfigUpdated(ThemeSettings.instance)
    initialThemeSettingsModel = themeSettingsModel.copy()
  }

  private fun persistChanges() {
    registerSettingsChange(themeSettingsModel.isVivid, {
      ThemeSettings.instance.isVivid
    }) {
      ThemeSettings.instance.isVivid = it
    }
    registerSettingsChange(themeSettingsModel.attributesStyle.value, {
      ThemeSettings.instance.attributesStyle
    }) {
      ThemeSettings.instance.attributesStyle = it
    }
    registerSettingsChange(themeSettingsModel.commentStyle.value, {
      ThemeSettings.instance.commentStyle
    }) {
      ThemeSettings.instance.commentStyle = it
    }
    registerSettingsChange(themeSettingsModel.keywordStyle.value, {
      ThemeSettings.instance.keywordStyle
    }) {
      ThemeSettings.instance.keywordStyle = it
    }
  }

  override fun createComponent(): JComponent? =
    createSettingsPane()

  private fun createSettingsPane(): DialogPanel =
    panel {
      titledRow("Font Styling") {
        row("Attributes") {
            buildComboBox(themeSettingsModel.attributesStyle) {
              themeSettingsModel.attributesStyle = it
            }().focused()
        }
        row("Comments") {
            buildComboBox(themeSettingsModel.commentStyle) {
              themeSettingsModel.commentStyle = it
            }()
        }
        row("Keywords") {
            buildComboBox(themeSettingsModel.keywordStyle) {
              themeSettingsModel.keywordStyle = it
            }()
        }
      }
      titledRow("Color Settings") {
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

  private fun buildComboBox(
    initialValue: GroupStyling,
    handler: (e: GroupStyling) -> Unit
  ): ComboBox<String> {
    val womboComboBox = ComboBox(DefaultComboBoxModel(
      Vector(GroupStyling.values().map { it.value }))
    )
    womboComboBox.model.selectedItem = initialValue.value
    womboComboBox.addActionListener {
      handler((womboComboBox.model.selectedItem as String).toGroupStyle())
    }
    return womboComboBox
  }
}

fun <T> registerSettingsChange(
  setValue: T,
  getStoredValue: () -> T,
  onChanged: (T) -> Unit
) {
  if (getStoredValue() != setValue) {
    onChanged(setValue)
  }
}
