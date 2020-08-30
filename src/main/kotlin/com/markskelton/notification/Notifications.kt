package com.markskelton.notification

import com.intellij.ide.plugins.PluginManagerCore.getPlugin
import com.intellij.ide.plugins.PluginManagerCore.getPluginOrPlatformByClassName
import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationListener
import com.intellij.notification.NotificationType

val UPDATE_MESSAGE: String = """
      What's New?<br>
      <ul>
        <li>Better consistency across various languages and platforms.</li>
      </ul>
      <br>Please see the <a href="https://github.com/one-dark/jetbrains-one-dark-theme/blob/master/CHANGELOG.md">Changelog</a> for more details.
      <br>
      Thank you for choosing the One Dark Theme!<br>
""".trimIndent()

object Notifications {

  private val notificationGroup = NotificationGroup(
    "One Dark Theme",
    NotificationDisplayType.BALLOON,
    false,
    "One Dark Theme"
  )

  fun displayUpdateNotification(versionNumber: String) {
    val pluginName =
      getPlugin(
        getPluginOrPlatformByClassName(Notifications::class.java.canonicalName)
      )?.name
    notificationGroup.createNotification(
      "$pluginName updated to v$versionNumber",
      UPDATE_MESSAGE,
      NotificationType.INFORMATION,
      NotificationListener.URL_OPENING_LISTENER
    )
      .notify(null)
  }
}