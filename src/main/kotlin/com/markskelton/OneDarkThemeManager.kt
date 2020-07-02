package com.markskelton

import com.intellij.application.options.colors.ColorSchemeImporter
import com.intellij.application.options.schemes.SchemeNameGenerator
import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.ide.ui.LafManager
import com.intellij.ide.ui.LafManagerListener
import com.intellij.ide.ui.laf.LafManagerImpl
import com.intellij.ide.ui.laf.TempUIThemeBasedLookAndFeelInfo
import com.intellij.ide.ui.laf.UIThemeBasedLookAndFeelInfo
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.colors.EditorColorsListener
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.editor.colors.EditorColorsScheme
import com.intellij.openapi.editor.colors.impl.AbstractColorsScheme
import com.intellij.openapi.editor.colors.impl.EditorColorsSchemeImpl
import com.intellij.openapi.editor.colors.impl.EmptyColorScheme
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.options.SchemeImportUtil
import com.intellij.openapi.project.DefaultProjectFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.containers.ContainerUtil
import com.intellij.util.messages.MessageBusConnection
import com.markskelton.notification.Notifications
import com.markskelton.settings.THEME_CONFIG_TOPIC
import com.markskelton.settings.ThemeConfigListener
import com.markskelton.settings.ThemeSettings
import java.util.Optional
import java.util.UUID

object OneDarkThemeManager {
  private lateinit var messageBus: MessageBusConnection
  const val ONE_DARK_ID = "f92a0fa7-1a98-47cd-b5cb-78ff67e6f4f3"
  private const val PLUGIN_ID = "com.markskelton.one-dark-theme"

  fun registerStartup(project: Project) {
    if (!this::messageBus.isInitialized) {
      registerUser()

      attemptToDisplayUpdates()

      applyConfigurableTheme()

      subscribeToEvents()
    }
  }

  private fun registerUser() {
    if (ThemeSettings.instance.userId.isEmpty()) {
      ThemeSettings.instance.userId = UUID.randomUUID().toString()
    }
  }

  private fun attemptToDisplayUpdates() {
    getVersion().ifPresent { currentVersion ->
      if (ThemeSettings.instance.version != currentVersion) {
        ThemeSettings.instance.version = currentVersion
        ThemeSettings.instance.customSchemeSet = false
        ApplicationManager.getApplication().invokeLater {
          Notifications.displayUpdateNotification(currentVersion)
        }
      }
    }
  }

  private fun getVersion(): Optional<String> =
    PluginManagerCore.getPlugin(PluginId.getId(PLUGIN_ID)).toOptional()
      .map { it.version }

  private fun subscribeToEvents() {
    messageBus = ApplicationManager.getApplication().messageBus.connect()
    messageBus.subscribe(THEME_CONFIG_TOPIC, object : ThemeConfigListener {
      override fun themeConfigUpdated(themeSettings: ThemeSettings) {
        hasAppliedColorScheme = false
        if (isCurrentTheme()) {
          ThemeSettings.instance.customSchemeSet = false
          setOneDarkTheme {ThemeConstructor.constructNewTheme(themeSettings)}
        }
      }
    })

    messageBus.subscribe(LafManagerListener.TOPIC, LafManagerListener {
      val currentLaf = it.currentLookAndFeel
      if (currentLaf is UIThemeBasedLookAndFeelInfo) {
        when {
          isOneDarkTheme(currentLaf) -> setOneDarkTheme { ThemeConstructor.useExistingTheme() }
          else -> hasAppliedColorScheme = false
        }
      }
    })

    messageBus.subscribe(EditorColorsManager.TOPIC, EditorColorsListener {
      ThemeSettings.instance.customSchemeSet =
        it != null && !it.metaProperties.containsKey("oneDarkScheme")
    })
  }

  private fun isOneDarkTheme(uiThemeBasedLookAndFeelInfo: UIThemeBasedLookAndFeelInfo): Boolean =
    uiThemeBasedLookAndFeelInfo.theme.id == ONE_DARK_ID

  private var hasAppliedColorScheme = false
  private fun applyConfigurableTheme() {
    if (isCurrentTheme() && !ThemeSettings.instance.customSchemeSet) {
      setOneDarkTheme { ThemeConstructor.useExistingTheme() }
    }
  }

  private fun setOneDarkTheme(schemeProvider: () -> VirtualFile) {
    if (!isCurrentTheme()) {
      val oneDarkLAF = LafManagerImpl.getInstance().installedLookAndFeels
        .filterIsInstance<UIThemeBasedLookAndFeelInfo>()
        .first {
          it.theme.id == ONE_DARK_ID
        }
      LafManager.getInstance().setCurrentLookAndFeel(oneDarkLAF)
    }

    if (!hasAppliedColorScheme) {
      hasAppliedColorScheme = true
      ApplicationManager.getApplication().invokeLater {
        val importer = ColorSchemeImporter()
        val colorsManager = EditorColorsManager.getInstance()
        val names = ContainerUtil.map(colorsManager.allSchemes) { obj: EditorColorsScheme -> obj.name }
        val imported = importer.importScheme(
          DefaultProjectFactory.getInstance().defaultProject,
          schemeProvider(),
          colorsManager.globalScheme
        ) { name ->
          val preferredName = name ?: "Unnamed"
          val newName = SchemeNameGenerator.getUniqueName(preferredName) { candidate: String? ->
            names.contains(candidate)
          }
          val newScheme: AbstractColorsScheme = EditorColorsSchemeImpl(EmptyColorScheme.INSTANCE)
          newScheme.name = newName
          newScheme.setDefaultMetaInfo(EmptyColorScheme.INSTANCE)
          newScheme
        }
        if (imported != null) {
          val root = SchemeImportUtil.loadSchemeDom(schemeProvider())
          imported.readExternal(root)
          colorsManager.addColorsScheme(imported)
          colorsManager.globalScheme = imported
        }
      }
    }
  }

  fun isCurrentTheme(): Boolean =
    when (val currentLaf = LafManagerImpl.getInstance().currentLookAndFeel) {
      is UIThemeBasedLookAndFeelInfo -> currentLaf.theme.id == ONE_DARK_ID
      is TempUIThemeBasedLookAndFeelInfo -> currentLaf.theme.id == ONE_DARK_ID
      else -> false
    }
}