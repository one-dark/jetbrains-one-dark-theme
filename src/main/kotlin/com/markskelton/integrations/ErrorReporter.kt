package com.markskelton.integrations

import com.intellij.ide.IdeBundle
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.ErrorReportSubmitter
import com.intellij.openapi.diagnostic.IdeaLoggingEvent
import com.intellij.openapi.diagnostic.SubmittedReportInfo
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.util.io.FileUtilRt
import com.intellij.util.Consumer
import com.markskelton.settings.ThemeSettings
import com.markskelton.tools.runSafely
import com.markskelton.tools.runSafelyWithResult
import io.sentry.Sentry
import io.sentry.SentryEvent
import io.sentry.SentryLevel
import io.sentry.SentryOptions
import io.sentry.protocol.Message
import io.sentry.protocol.User
import java.awt.Component
import java.lang.management.ManagementFactory
import java.util.Properties
import java.util.stream.Collectors

class ErrorReporter : ErrorReportSubmitter() {
  override fun getReportActionText(): String = "Report Anonymously"

  override fun submit(
    events: Array<out IdeaLoggingEvent>,
    additionalInfo: String?,
    parentComponent: Component,
    consumer: Consumer<in SubmittedReportInfo>
  ): Boolean {
    return runSafelyWithResult({
      ApplicationManager.getApplication().executeOnPooledThread {
        runSafely({
          Sentry.setUser(
            User().apply {
              this.id = ThemeSettings.instance.userId
            }
          )
          Sentry.init { options: SentryOptions ->
            options.dsn =
              RestClient.performGet(
                "https://jetbrains.assets.unthrottled.io/one-dark/sentry-dsn-v2.txt"
              )
                .map { it.trim() }
                .orElse("https://0d650a631e3b4170adf6559de446fd37@o403546.ingest.sentry.io/5861304?maxmessagelength=50000")
          }
          events.forEach {
            Sentry.captureEvent(
              addSystemInfo(
                SentryEvent()
                  .apply {
                    this.level = SentryLevel.ERROR
                    this.setExtra("Additional Info", additionalInfo ?: "None")
                  }
              ).apply {
                this.message = Message().apply {
                  this.message = it.throwableText
                }
              }
            )
          }
        }) {
        }
      }
      true
    }) {
      false
    }
  }

  private fun addSystemInfo(event: SentryEvent): SentryEvent {
    val properties = System.getProperties()
    return event.apply {
      setExtra("JRE", getJRE(properties))
      setExtra("VM", getVM(properties))
      setExtra("System Info", SystemInfo.getOsNameAndVersion())
      setExtra("GC", getGC())
      setExtra("Memory", Runtime.getRuntime().maxMemory() / FileUtilRt.MEGABYTE)
      setExtra("Cores", Runtime.getRuntime().availableProcessors())
      setExtra("One Dark Config", ThemeSettings.instance.asJson())
    }
  }

  private fun getJRE(properties: Properties): String {
    val javaVersion = properties.getProperty("java.runtime.version", properties.getProperty("java.version", "unknown"))
    val arch = properties.getProperty("os.arch", "")
    return IdeBundle.message("about.box.jre", javaVersion, arch)
  }

  private fun getVM(properties: Properties): String {
    val vmVersion = properties.getProperty("java.vm.name", "unknown")
    val vmVendor = properties.getProperty("java.vendor", "unknown")
    return IdeBundle.message("about.box.vm", vmVersion, vmVendor)
  }

  private fun getGC() = ManagementFactory.getGarbageCollectorMXBeans().stream()
    .map { it.name }.collect(Collectors.joining(","))

}
