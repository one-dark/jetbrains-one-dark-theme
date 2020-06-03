package com.markskelton.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

enum class GroupStyling(
  val value: String
) {
  REGULAR("Regular"), ITALIC("Italic"), BOLD("Bold"), BOLD_ITALIC("Bold Italic")
}
private val styleMappings = GroupStyling.values()
  .map { it.value to it }
  .toMap()
fun String.toGroupStyle(): GroupStyling = styleMappings.getOrDefault(
  this, GroupStyling.REGULAR
)

@State(
    name = "OneDarkConfig",
    storages = [Storage("one_dark_config.xml")]
)
class ThemeSettings : PersistentStateComponent<ThemeSettings>, Cloneable {
  companion object {
    val instance: ThemeSettings
      get() = ServiceManager.getService(ThemeSettings::class.java)

    fun constructSettingModel(): ThemeSettingsModel {
      return ThemeSettingsModel(
        instance.commentStyle.toGroupStyle(),
        instance.keywordStyle.toGroupStyle(),
        instance.attributesStyle.toGroupStyle(),
        instance.isVivid
      )
    }
  }

  var version: String = "0.0.0"
  var userId: String = ""
  var isBold: Boolean = false
  var isVivid: Boolean = false
  var isItalic: Boolean = false
  var commentStyle: String = GroupStyling.REGULAR.value
  var keywordStyle: String = GroupStyling.REGULAR.value
  var attributesStyle: String = GroupStyling.REGULAR.value
  var customSchemeSet: Boolean = false

  override fun getState(): ThemeSettings? =
      XmlSerializerUtil.createCopy(this)

  override fun loadState(state: ThemeSettings) {
    XmlSerializerUtil.copyBean(state, this)
  }

  fun asJson(): Map<String, Any> = mapOf(
      "version" to version,
      "isBold" to isBold,
      "isVivid" to isVivid,
      "isItalic" to isItalic
  )
}
