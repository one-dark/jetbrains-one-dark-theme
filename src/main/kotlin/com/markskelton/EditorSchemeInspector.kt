package com.markskelton

import com.intellij.openapi.editor.colors.EditorColorsManager

object EditorSchemeInspector {
  fun isEditorSchemeOneDark(): Boolean {
    val editorSchemeName = EditorColorsManager.getInstance().globalScheme.displayName
    return editorSchemeName == ONE_DARK_GENERATED_NAME || editorSchemeName == "One Dark"
  }
}