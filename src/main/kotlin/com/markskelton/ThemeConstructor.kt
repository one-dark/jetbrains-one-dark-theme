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
import com.markskelton.settings.*
import com.markskelton.settings.Groups.*
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
  private const val ONE_DARK_FILE_PREFIX = "one-dark-"

  fun constructNewTheme(newSettings: ThemeSettings): UIManager.LookAndFeelInfo =
    constructLookAndFeel(getUpdatedEditorScheme(newSettings))

  fun useExistingTheme(): UIManager.LookAndFeelInfo =
    constructLookAndFeel(getPreExistingTheme())

  private fun getPreExistingTheme(): Path =
    findConstructedThemes(getAssetsDirectory())
      .findFirst()
      .orElseGet {
        getUpdatedEditorScheme(ThemeSettings.instance)
      }

  private fun constructLookAndFeel(updatedEditorScheme: Path): TempUIThemeBasedLookAndFeelInfo {
    val oneDarkLAF = LafManagerImpl.getInstance().installedLookAndFeels
      .filterIsInstance<UIThemeBasedLookAndFeelInfo>()
      .first {
        it.theme.id == ONE_DARK_ID
      }
    return TempUIThemeBasedLookAndFeelInfo(
      oneDarkLAF.theme,
      VfsUtil.findFile(updatedEditorScheme, true)
    )
  }

  private fun getUpdatedEditorScheme(themeSettings: ThemeSettings): Path {
    val assetsDirectory = getAssetsDirectory()
    cleanDirectory(assetsDirectory)
    // Intellij caches files, and will not update if you change the contents of the file
    val newEditorSchemeFile = Paths.get(
      assetsDirectory.toAbsolutePath().toString(),
      "$ONE_DARK_FILE_PREFIX${UUID.randomUUID()}.xml"
    )
    buildNewEditorScheme(themeSettings, newEditorSchemeFile)
    return newEditorSchemeFile
  }

  private fun cleanDirectory(assetsDirectory: Path) {
    findConstructedThemes(assetsDirectory)
      .forEach { Files.delete(it) }
  }

  private fun findConstructedThemes(assetsDirectory: Path) = Files.walk(assetsDirectory)
    .filter { it.fileName.toString().startsWith(ONE_DARK_FILE_PREFIX) }

  private fun buildNewEditorScheme(themeSettings: ThemeSettings, newSchemeFile: Path) {
    val colorPalette = getColorPalette(themeSettings)
    val editorTemplate = getEditorXMLTemplate()
    val updatedScheme = applySettingsToTemplate(
      editorTemplate,
      themeSettings,
      colorPalette
    )
    writeXmlToFile(newSchemeFile, updatedScheme)
  }

  private fun applySettingsToTemplate(
    editorTemplate: Node,
    themeSettings: ThemeSettings,
    colorPalette: ColorPalette
  ): Node {
    val (paletteVariant, colors) = colorPalette
    val themeTemplate = editorTemplate.clone() as Node
    themeTemplate.breadthFirst()
      .map { it as Node }
      .forEach {
        when (it.name()) {
          "scheme" -> {
            it.attributes().replace("name", "One Dark Generated")
          }
          "option" -> {
            val value = it.attribute("value") as? String
            if (value?.startsWith('$') == true) {
              val (end, replacementColor) = getReplacementColor(value, '$') { templateColor ->
                colors[templateColor]
                  ?: throw IllegalArgumentException("$templateColor is not in the color definition for $paletteVariant.")
              }
              it.attributes()["value"] = buildReplacement(replacementColor, value, end)
            } else if (value?.startsWith('%') == true) {
              val (_, fontVariant) = extractValueFromTemplateString(value, '%') { fontSpec ->
                val fontSpecifications = fontSpec.split('$')
                val shouldEffectBeBold = isEffectBold(fontSpecifications, themeSettings)
                val shouldEffectBeItalic = isEffectItalic(fontSpecifications, themeSettings)
                when {
                  shouldEffectBeBold && shouldEffectBeItalic -> FontVariant.BOLD_ITALIC
                  shouldEffectBeBold -> FontVariant.BOLD
                  shouldEffectBeItalic -> FontVariant.ITALIC
                  else -> FontVariant.NONE
                }
              }
              it.attributes()["value"] = fontVariant.schemeValue
            }
          }
        }
      }

    return themeTemplate
  }

  private fun isEffectBold(
    fontSpecifications: List<String>,
    themeSettings: ThemeSettings
  ): Boolean =
    matchesThemeSetting(fontSpecifications, "bold:") {
      val relevantGroupStyle = getRelevantGroupStyle(it, themeSettings)
      relevantGroupStyle == GroupStyling.BOLD ||
        relevantGroupStyle == GroupStyling.BOLD_ITALIC
    }

  private fun getRelevantGroupStyle(it: Groups, themeSettings: ThemeSettings): GroupStyling =
    when (it) {
      ATTRIBUTES -> themeSettings.attributesStyle
      COMMENTS -> themeSettings.commentStyle
      KEYWORDS -> themeSettings.keywordStyle
    }.toGroupStyle()

  private fun isEffectItalic(
    fontSpecifications: List<String>,
    themeSettings: ThemeSettings
  ): Boolean =
    matchesThemeSetting(fontSpecifications, "italic:") {
      val relevantGroupStyle = getRelevantGroupStyle(it, themeSettings)
      relevantGroupStyle == GroupStyling.ITALIC ||
        relevantGroupStyle == GroupStyling.BOLD_ITALIC
    }

  private fun matchesThemeSetting(
    fontSpecifications: List<String>,
    prefix: String,
    isCurrentThemeSetting: (group: Groups) -> Boolean
  ): Boolean =
    fontSpecifications.any {
      it.startsWith(prefix) &&
        (it.endsWith(":always") ||
          (it.contains(":theme") &&
            isCurrentThemeSetting(
              it.substringAfter("^").toGroup()
            ))
          )
    }

  private fun buildReplacement(replacementColor: String, value: String, end: Int) =
    "$replacementColor${value.substring(end + 1)}"

  private fun getReplacementColor(
    value: String,
    templateDelemiter: Char,
    replacementSupplier: (CharSequence) -> String
  ): Pair<Int, String> {
    val (end, replacementHexColor) = extractValueFromTemplateString(value, templateDelemiter, replacementSupplier)
    val replacementColor = replacementHexColor.substring(1)
    return Pair(end, replacementColor)
  }

  private fun <T> extractValueFromTemplateString(
    value: String,
    templateDelemiter: Char,
    replacementSupplier: (CharSequence) -> T
  ): Pair<Int, T> {
    val start = value.indexOf(templateDelemiter)
    val end = value.lastIndexOf(templateDelemiter)
    val templateColor = value.subSequence(start + 1, end)
    val replacementHexColor = replacementSupplier(templateColor)
    return Pair(end, replacementHexColor)
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
