package com.markskelton

import com.intellij.ide.ui.laf.LafManagerImpl
import com.markskelton.settings.ThemeSettings
import javax.swing.UIManager

object ThemeConstructor {

  fun constructNewTheme(newSettings: ThemeSettings): UIManager.LookAndFeelInfo {
    val currentLaf = LafManagerImpl.getInstance().currentLookAndFeel
    return LafManagerImpl.getInstance().installedLookAndFeels.first {
      it != currentLaf
    }
  }
}