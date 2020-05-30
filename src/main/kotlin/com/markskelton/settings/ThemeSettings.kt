package com.markskelton.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

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
        instance.isBold,
        instance.isVivid,
        instance.isItalic
      )
    }
  }

  var version: String = "0.0.0"
  var isBold: Boolean = false
  var isVivid: Boolean = false
  var isItalic: Boolean = false
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