package com.markskelton

import com.intellij.application.options.colors.ColorSchemeImporter
import com.intellij.application.options.schemes.SchemeNameGenerator
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.editor.colors.EditorColorsScheme
import com.intellij.openapi.editor.colors.impl.AbstractColorsScheme
import com.intellij.openapi.editor.colors.impl.EditorColorsSchemeImpl
import com.intellij.openapi.editor.colors.impl.EmptyColorScheme
import com.intellij.openapi.options.SchemeImportUtil
import com.intellij.openapi.project.DefaultProjectFactory
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.containers.ContainerUtil

object SchemeImporter {
  private val log = Logger.getInstance(this::class.java)

  fun importScheme(schemeProvider: () -> VirtualFile) {
    try {
      val importer = ColorSchemeImporter()
      val colorsManager = EditorColorsManager.getInstance()
      val names = ContainerUtil.map(colorsManager.allSchemes) { obj: EditorColorsScheme -> obj.name }
      val schemeVirtualFile = schemeProvider()
      val importedColorScheme = importer.importScheme(
        DefaultProjectFactory.getInstance().defaultProject,
        schemeVirtualFile,
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
      if (importedColorScheme != null) {
        val root = SchemeImportUtil.loadSchemeDom(schemeVirtualFile)
        importedColorScheme.readExternal(root)
        colorsManager.addColorsScheme(importedColorScheme)
        colorsManager.globalScheme = importedColorScheme
      }
    } catch (e: Throwable) {
      log.warn("Unable to set generated theme for reasons", e)
    }
  }
}