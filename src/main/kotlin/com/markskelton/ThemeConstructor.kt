package com.markskelton

import com.intellij.ide.ui.laf.LafManagerImpl
import com.intellij.ide.ui.laf.TempUIThemeBasedLookAndFeelInfo
import com.intellij.ide.ui.laf.UIThemeBasedLookAndFeelInfo
import com.intellij.openapi.vfs.VfsUtil
import com.markskelton.OneDarkThemeManager.ONE_DARK_ID
import com.markskelton.settings.ThemeSettings
import java.nio.file.Paths
import javax.swing.UIManager

object ThemeConstructor {

  fun constructNewTheme(newSettings: ThemeSettings): UIManager.LookAndFeelInfo {
    val oneDarkLAF = LafManagerImpl.getInstance().installedLookAndFeels
      .filterIsInstance<UIThemeBasedLookAndFeelInfo>()
      .first {
        it.theme.id == ONE_DARK_ID
      }
    val editorPath = Paths.get("/home/alex/workspace/doki-theme-jetbrains/src/main/resources/doki/themes/danganronpa/Mioda_Ibuki_Dark.xml")
    return TempUIThemeBasedLookAndFeelInfo(
      oneDarkLAF.theme,
      VfsUtil.findFile(editorPath, true)
    )
  }
}