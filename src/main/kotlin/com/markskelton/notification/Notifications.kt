package com.markskelton.notification

import com.intellij.ide.plugins.PluginManagerCore.getPlugin
import com.intellij.ide.plugins.PluginManagerCore.getPluginOrPlatformByClassName
import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationListener
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.options.ShowSettingsUtil
import com.markskelton.settings.ThemeSettingsUI

val UPDATE_MESSAGE: String = """
      What's New?<br>
      <ul>
        <li>Theme is now configurable through settings.</li>
      </ul>
      <br>Please see the <a href="https://github.com/one-dark/jetbrains-one-dark-theme/blob/master/CHANGELOG.md">Changelog</a> for more details.
      <br>
      Thank you for choosing the One Dark Theme!<br>
""".trimIndent()

const val CURRENT_VERSION = "4.0.0"

object Notifications {

  private val notificationGroup = NotificationGroup(
    "One-Dark Theme",
    NotificationDisplayType.BALLOON,
    false,
    "One-Dark Theme"
  )

  fun displayUpdateNotification() {
    val pluginName =
      getPlugin(
        getPluginOrPlatformByClassName(Notifications::class.java.canonicalName)
      )?.name
    notificationGroup.createNotification(
      "$pluginName updated to v$CURRENT_VERSION",
      UPDATE_MESSAGE,
      NotificationType.INFORMATION,
      NotificationListener.URL_OPENING_LISTENER
    ).notify(null)
  }

  fun displayDeprecationMessage() {
    notificationGroup.createNotification(
      "Theme is Deprecated",
      """The other variants of the One-Dark theme are currently deprecated. 
        |For convenience, this theme's settings have been automatically applied. 
|Please use the '${ThemeSettingsUI.THEME_SETTINGS_DISPLAY_NAME}' menu in the settings to configure One-Dark in the future.""".trimMargin(),
      NotificationType.WARNING,
      null
    ).addAction(SettingsAction("Show Settings"))
      .notify(null)
  }
}

class SettingsAction(text: String) : NotificationAction(text) {
  override fun actionPerformed(e: AnActionEvent, notification: Notification) {
    ShowSettingsUtil.getInstance().showSettingsDialog(e.project, ThemeSettingsUI.THEME_SETTINGS_DISPLAY_NAME)
  }
}