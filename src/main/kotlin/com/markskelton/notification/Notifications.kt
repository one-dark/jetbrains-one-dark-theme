package com.markskelton.notification

import com.intellij.ide.plugins.PluginManagerCore.getPlugin
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationListener
import com.intellij.notification.NotificationType
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.util.IconLoader
import com.markskelton.OneDarkThemeManager.PLUGIN_ID
import org.intellij.lang.annotations.Language

@Language("HTML")
val UPDATE_MESSAGE: String = """
      What's New?<br>
      <ul>
          <li>2023.2 Build Support</li>
      </ul>
      <br>Please see the <a href='https://github.com/one-dark/jetbrains-one-dark-theme/blob/master/CHANGELOG.md'>Changelog</a> for more details.
      <br>
      Thank you for choosing the One Dark Theme!<br>
""".trimIndent()

object Notifications {

  private val NOTIFICATION_ICON = IconLoader.getIcon(
    "/icons/one-dark-logo.svg",
    Notifications::class.java
  )

  private val notificationGroup = NotificationGroupManager.getInstance().getNotificationGroup("One Dark Theme")

  fun displayUpdateNotification(versionNumber: String) {
    val pluginName =
      getPlugin(PluginId.getId(PLUGIN_ID))?.name ?: "One Dark Theme"
    notificationGroup.createNotification(
      UPDATE_MESSAGE,
      NotificationType.INFORMATION
    )
      .setTitle("$pluginName updated to v$versionNumber")
      .setListener(NotificationListener.UrlOpeningListener(false))
      .setIcon(NOTIFICATION_ICON)
      .notify(null)
  }
}
