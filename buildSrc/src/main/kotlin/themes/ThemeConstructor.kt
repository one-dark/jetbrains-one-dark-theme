package themes

import com.google.gson.GsonBuilder
import com.google.gson.ToNumberPolicy
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import groovy.util.Node
import groovy.util.XmlNodePrinter
import groovy.util.XmlParser
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.xml.sax.ErrorHandler
import org.xml.sax.InputSource
import org.xml.sax.SAXParseException
import themes.GroupStyling.*
import java.io.BufferedOutputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

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

data class OneDarkThemeDefinition(
  val id: String,
  val name: String
)

open class ThemeConstructor : DefaultTask() {
  companion object {
    private val gson = GsonBuilder()
      .setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
      .setPrettyPrinting().create()
    private const val REGULAR = "One Dark"
    private const val ITALIC = "One Dark Italic"
    private const val VIVID = "One Dark Vivid"
    private const val VIVID_ITALIC = "One Dark Vivid Italic"

    // new UI
    private const val REGULAR_NEW_UI = "One Dark New UI"
    val THEMES = mapOf(
      "1fa63ea1-a161-4d01-b488-d4a6b2d4c10e" to REGULAR_NEW_UI,
      "f92a0fa7-1a98-47cd-b5cb-78ff67e6f4f3" to REGULAR,
      "1a92aa6f-c2f1-4994-ae01-6a78e43eeb24" to ITALIC,
      "4b6007f7-b596-4ee2-96f9-968d3d3eb392" to VIVID,
      "4f556d32-83cb-4b8b-9932-c4eccc4ce3af" to VIVID_ITALIC
    )
  }

  @TaskAction
  fun run() {
    val assetsDirectory = getAssetsDirectory()
    cleanDirectory(assetsDirectory)
    THEMES.entries.forEach {
      constructNewTheme(getSettings(it.value), OneDarkThemeDefinition(
        it.key,
        it.value,
      ))
    }
  }

  private fun getSettings(themeName: String): ThemeSettings {
    return when (themeName) {
      REGULAR -> ThemeSettings(
        false, false,
        GroupStyling.REGULAR.value,
        GroupStyling.REGULAR.value,
        GroupStyling.REGULAR.value
      )
      ITALIC -> ThemeSettings(
        false, false,
        GroupStyling.ITALIC.value,
        GroupStyling.ITALIC.value,
        GroupStyling.ITALIC.value
      )
      VIVID -> ThemeSettings(
        true, false,
        GroupStyling.REGULAR.value,
        GroupStyling.REGULAR.value,
        GroupStyling.REGULAR.value
      )
      VIVID_ITALIC -> ThemeSettings(
        true, false,
        GroupStyling.ITALIC.value,
        GroupStyling.ITALIC.value,
        GroupStyling.ITALIC.value
      )

      REGULAR_NEW_UI -> ThemeSettings(
        false, true,
        GroupStyling.REGULAR.value,
        GroupStyling.REGULAR.value,
        GroupStyling.REGULAR.value
      )
      else -> throw IllegalArgumentException("Bro, I don't know what theme is $themeName")
    }
  }

  private fun constructNewTheme(
    newSettings: ThemeSettings,
    themeDefinition: OneDarkThemeDefinition
  ) = buildScheme(newSettings, themeDefinition)

  private fun buildScheme(
    themeSettings: ThemeSettings,
    themeDefinition: OneDarkThemeDefinition
  ) {
    val assetsDirectory = getAssetsDirectory()
    val newEditorSchemeFile = Paths.get(
      assetsDirectory.toAbsolutePath().toString(),
      "${createFileName(themeDefinition.name)}.xml"
    )
    buildNewEditorScheme(themeSettings, newEditorSchemeFile, themeDefinition)
    buildThemeJson(
      themeSettings,
      themeDefinition, Paths.get(
        assetsDirectory.toAbsolutePath().toString(),
        "${createFileName(themeDefinition.name)}.theme.json"))
  }

  private fun buildThemeJson(
    themeSettings: ThemeSettings,
    themeDefinition: OneDarkThemeDefinition,
    destinationFile: Path
  ) {
    val templateFile = if (themeSettings.newUi) "oneDark-newUi.template.theme.json" else "oneDark.template.theme.json"
    val themeJsonTemplate: MutableMap<String, Any> =
      Files.newInputStream(Paths.get(
        project.rootDir.absolutePath,
        "buildSrc",
        "templates",
        templateFile
      ))
        .use {
          gson.fromJson<MutableMap<String, Any>>(JsonReader(it.reader()),
            object : TypeToken<MutableMap<String, Any>>() {}.type
          )
        }
    val themeFileName = createFileName(themeDefinition.name)
    themeJsonTemplate["editorScheme"] = "/themes/$themeFileName.xml"
    themeJsonTemplate["name"] = themeDefinition.name
    themeJsonTemplate["id"] = themeDefinition.id

    Files.newBufferedWriter(destinationFile, StandardOpenOption.CREATE_NEW)
      .use {
        it.write(gson.toJson(themeJsonTemplate))
      }
  }

  private fun createFileName(themeName: String): String =
    themeName.toLowerCase().replace(' ', '_')

  private fun cleanDirectory(assetsDirectory: Path) {
    findResources(assetsDirectory)
      .filter { Files.isDirectory(it).not() }
      .forEach { Files.delete(it) }
  }

  private fun findResources(assetsDirectory: Path) =
    Files.walk(assetsDirectory)

  private fun buildNewEditorScheme(
    themeSettings: ThemeSettings,
    newSchemeFile: Path,
    oneDarkThemeDefinition: OneDarkThemeDefinition
  ) {
    val colorPalette = getColorPalette(themeSettings)
    val editorTemplate = getEditorXMLTemplate()
    val updatedScheme = applySettingsToTemplate(
      editorTemplate,
      themeSettings,
      colorPalette,
      oneDarkThemeDefinition
    )
    writeXmlToFile(newSchemeFile, updatedScheme)
  }

  private fun applySettingsToTemplate(
    editorTemplate: Node,
    themeSettings: ThemeSettings,
    colorPalette: ColorPalette,
    oneDarkThemeDefinition: OneDarkThemeDefinition
  ): Node {
    val (paletteVariant, colors) = colorPalette
    val themeTemplate = editorTemplate.clone() as Node
    themeTemplate.breadthFirst()
      .map { it as Node }
      .forEach {
        when (it.name()) {
          "scheme" -> {
            it.attributes().replace("name", oneDarkThemeDefinition.name)
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
    matchesThemeSetting(fontSpecifications, "bold") {
      val relevantGroupStyle = getRelevantGroupStyle(it, themeSettings)
      relevantGroupStyle == BOLD ||
        relevantGroupStyle == BOLD_ITALIC
    }

  private fun isEffectItalic(
    fontSpecifications: List<String>,
    themeSettings: ThemeSettings
  ): Boolean =
    matchesThemeSetting(fontSpecifications, "italic") {
      val relevantGroupStyle = getRelevantGroupStyle(it, themeSettings)
      relevantGroupStyle == GroupStyling.ITALIC ||
        relevantGroupStyle == BOLD_ITALIC
    }

  private fun getRelevantGroupStyle(it: Groups, themeSettings: ThemeSettings): GroupStyling =
    when (it) {
      Groups.ATTRIBUTES -> themeSettings.attributesStyle
      Groups.COMMENTS -> themeSettings.commentStyle
      Groups.KEYWORDS -> themeSettings.keywordStyle
    }.toGroupStyle()

  private fun matchesThemeSetting(
    fontSpecifications: List<String>,
    prefix: String,
    isCurrentThemeSetting: (group: Groups) -> Boolean
  ): Boolean =
    fontSpecifications.any {
      it.startsWith(prefix) ||
        (it.startsWith("theme") &&
          isCurrentThemeSetting(
            it.substringAfter("^").toGroup()
          ))
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
    Files.newInputStream(Paths.get(
      project.rootDir.absolutePath,
      "buildSrc",
      "templates",
      "one-dark.template.xml"
    )).use { input ->
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
    return ColorPalette(
      if (themeSettings.isVivid) ColorVariant.VIVID else ColorVariant.NORMAL,
      Files.newInputStream(Paths.get(
        project.rootDir.absolutePath,
        "buildSrc",
        "templates",
        "$selectedPalette.palette.json"
      )).use {
        gson.fromJson<MutableMap<String, String>>(JsonReader(it.reader()), object : TypeToken<MutableMap<String, String>>() {}.type)
      }
    )
  }

  private fun getAssetsDirectory(): Path {
    val configDirectory = Paths.get(
      project.rootDir.absolutePath,
      "src",
      "main",
      "resources",
      "themes"
    )
    if (Files.notExists(configDirectory)) {
      Files.createDirectories(configDirectory)
    }
    return configDirectory
  }

  private fun writeXmlToFile(pluginXml: Path, parsedPluginXml: Node) {
    Files.newOutputStream(pluginXml, StandardOpenOption.CREATE_NEW).use {
      val outputStream = BufferedOutputStream(it)
      val writer = PrintWriter(OutputStreamWriter(outputStream, StandardCharsets.UTF_8))
      val printer = XmlNodePrinter(writer)
      printer.isPreserveWhitespace = true
      printer.print(parsedPluginXml)
    }
  }
}
