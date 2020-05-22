package com.markskelton

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.intellij.ide.ui.laf.LafManagerImpl
import com.intellij.ide.ui.laf.TempUIThemeBasedLookAndFeelInfo
import com.intellij.ide.ui.laf.UIThemeBasedLookAndFeelInfo
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.vfs.VfsUtil
import com.markskelton.OneDarkThemeManager.ONE_DARK_ID
import com.markskelton.settings.ThemeSettings
import groovy.util.Node
import groovy.util.XmlNodePrinter
import java.io.BufferedOutputStream
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import javax.swing.UIManager

enum class FontVariant(val schemeValue: Int) {
  BOLD(1), ITALIC(2), BOLD_ITALIC(3), NONE(0)
}

object ThemeConstructor {
  private val gson = Gson()

  fun constructNewTheme(newSettings: ThemeSettings): UIManager.LookAndFeelInfo {
    val oneDarkLAF = LafManagerImpl.getInstance().installedLookAndFeels
      .filterIsInstance<UIThemeBasedLookAndFeelInfo>()
      .first {
        it.theme.id == ONE_DARK_ID
      }
    return TempUIThemeBasedLookAndFeelInfo(
      oneDarkLAF.theme,
      VfsUtil.findFile(getUpdatedEditorScheme(newSettings), true)
    )
  }

  private fun getUpdatedEditorScheme(themeSettings: ThemeSettings): Path {
    val newEditorSchemeFile = Paths.get(getAssetsDirectory().toAbsolutePath().toString(), "one_dark.xml")
    buildNewEditorScheme(themeSettings, newEditorSchemeFile)
    return newEditorSchemeFile
  }

  private fun buildNewEditorScheme(themeSettings: ThemeSettings, newSchemeFile: Path) {
    val colorPalette = getColorPalette(themeSettings)
    val fontVariants = getFontVariants(themeSettings)
    val editorTemplate = getEditorXMLTemplate()
    val updatedScheme = applySettingsToTemplate(
      editorTemplate,
      fontVariants,
      colorPalette
    )
    writeXmlToFile(newSchemeFile, updatedScheme)
  }

  private fun applySettingsToTemplate(
    editorTemplate: Node,
    fontVariants: FontVariant,
    colorPalette: Map<String, String>
  ): Node {
    TODO("Not yet implemented")
  }

  private fun getEditorXMLTemplate(): Node {
    TODO("Not yet implemented")
  }

  private fun getFontVariants(themeSettings: ThemeSettings): FontVariant {
    return when {
      themeSettings.isBold && themeSettings.isItalic -> FontVariant.BOLD_ITALIC
      themeSettings.isBold -> FontVariant.BOLD
      themeSettings.isItalic -> FontVariant.ITALIC
      else -> FontVariant.NONE
    }
  }

  private fun getColorPalette(themeSettings: ThemeSettings): Map<String, String> {
    val selectedPalette = if(themeSettings.isVivid) "vivid" else "normal"
    return gson.fromJson(this::class.java.getResourceAsStream(
      "$selectedPalette.palette.json"
    ).reader(), object: TypeToken<Map<String, String>>() {}.type)
  }

  private fun getAssetsDirectory(): Path {
    val configDirectory = Paths.get(PathManager.getConfigPath(), "oneDarkAssets")
    if (Files.notExists(configDirectory)) {
      Files.createDirectories(configDirectory)
    }
    return configDirectory
  }

  private fun writeXmlToFile(pluginXml: Path, parsedPluginXml: Node) {
    Files.newOutputStream(pluginXml).use {
      val outputStream = BufferedOutputStream(it)
      val writer = PrintWriter(OutputStreamWriter(outputStream, StandardCharsets.UTF_8))
      val printer = XmlNodePrinter(writer)
      printer.isPreserveWhitespace = true
      printer.print(parsedPluginXml)
    }
  }
}