package com.markskelton

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.intellij.ide.ui.laf.LafManagerImpl
import com.intellij.ide.ui.laf.TempUIThemeBasedLookAndFeelInfo
import com.intellij.ide.ui.laf.UIThemeBasedLookAndFeelInfo
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.vfs.VfsUtil
import com.markskelton.OneDarkThemeManager.ONE_DARK_ID
import com.markskelton.settings.ThemeSettings
import groovy.util.Node
import groovy.util.XmlNodePrinter
import groovy.util.XmlParser
import org.xml.sax.ErrorHandler
import org.xml.sax.InputSource
import org.xml.sax.SAXParseException
import java.io.BufferedOutputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import javax.swing.UIManager

enum class ColorVariant {
  VIVID, NORMAL
}

enum class FontVariant(val schemeValue: Int) {
  BOLD(1), ITALIC(2), BOLD_ITALIC(3), NONE(0)
}

data class ColorPalette(
  val variant: ColorVariant,
  val colors: Map<String, String>
)

object ThemeConstructor {
  private val gson = Gson()
  private val logger = Logger.getInstance(this::class.java)

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
    val assetsDirectory = getAssetsDirectory()
    cleanDirectory(assetsDirectory)
    // Intellij caches files, and will not update if you change the file
    val newEditorSchemeFile = Paths.get(assetsDirectory.toAbsolutePath().toString(), "one-dark-${UUID.randomUUID()}.xml")
    buildNewEditorScheme(themeSettings, newEditorSchemeFile)
    return newEditorSchemeFile
  }

  private fun cleanDirectory(assetsDirectory: Path) {
    Files.walk(assetsDirectory)
      .filter { it.fileName.toString().startsWith("one-dark-") }
      .forEach { Files.delete(it) }
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
    fontVariant: FontVariant,
    colorPalette: ColorPalette
  ): Node {
    val (paletteVariant, colors) = colorPalette
    val themeTemplate = editorTemplate.clone() as Node
    themeTemplate.breadthFirst()
      .map { it as Node }
      .forEach {
        when (it.name()) {
          "scheme" -> {
            it.attributes().replace("name", "One Dark $paletteVariant $fontVariant")
          }
          "option" -> {
            val value = it.attribute("value") as? String
            if (value?.contains('$') == true) {
              val (end, replacementColor) = getReplacementColor(value, '$') { templateColor ->
                colors[templateColor]
                  ?: throw IllegalArgumentException("$templateColor is not in the color definition for $paletteVariant.")
              }
              it.attributes()["value"] = buildReplacement(replacementColor, value, end)
            }
          }
        }
      }

    return themeTemplate
  }

  private fun buildReplacement(replacementColor: String, value: String, end: Int) =
    "$replacementColor${value.substring(end + 1)}"

  private fun getReplacementColor(
    value: String,
    templateDelemiter: Char,
    replacementSupplier: (CharSequence) -> String
  ): Pair<Int, String> {
    val start = value.indexOf(templateDelemiter)
    val end = value.lastIndexOf(templateDelemiter)
    val templateColor = value.subSequence(start + 1, end)
    val replacementHexColor = replacementSupplier(templateColor)
    val replacementColor = replacementHexColor.substring(1)
    return Pair(end, replacementColor)
  }

  private fun getEditorXMLTemplate(): Node =
    this::class.java.getResourceAsStream("/templates/one-dark.template.xml").use { input ->
      val inputSource = InputSource(InputStreamReader(input, "UTF-8"))
      val parser = XmlParser(false, true, true)
      parser.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)
      parser.errorHandler = object : ErrorHandler {
        override fun warning(exception: SAXParseException?) {}

        override fun error(exception: SAXParseException?) {}

        override fun fatalError(exception: SAXParseException) {
          throw exception
        }
      }
      parser.parse(inputSource)
    }

  private fun getFontVariants(themeSettings: ThemeSettings): FontVariant {
    return when {
      themeSettings.isBold && themeSettings.isItalic -> FontVariant.BOLD_ITALIC
      themeSettings.isBold -> FontVariant.BOLD
      themeSettings.isItalic -> FontVariant.ITALIC
      else -> FontVariant.NONE
    }
  }

  private fun getColorPalette(themeSettings: ThemeSettings): ColorPalette {
    val selectedPalette = if (themeSettings.isVivid) "vivid" else "normal"
    logger.info("Building theme with $selectedPalette palette.")
    return ColorPalette(
      if (themeSettings.isVivid) ColorVariant.VIVID else ColorVariant.NORMAL,
      gson.fromJson(this::class.java.getResourceAsStream(
        "/templates/$selectedPalette.palette.json"
      ).reader(), object : TypeToken<Map<String, String>>() {}.type)
    )
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