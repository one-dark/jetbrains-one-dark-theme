package com.markskelton.utils

import com.intellij.openapi.application.PathManager
import com.intellij.openapi.diagnostic.Logger
import com.markskelton.toOptional
import groovy.util.Node
import java.nio.file.Files
import java.nio.file.Paths
import java.util.Optional

object PlatformConfigurationManager {
  private val log = Logger.getInstance(this::class.java)

  fun readConfigurationFile(
    fileName: String,
    componentName: String
  ): Optional<Node> {
    val configurationFilePath = Paths.get(PathManager.getConfigPath(), "options", fileName)
    return if (Files.exists(configurationFilePath)) {
      Files.newInputStream(configurationFilePath)
        .use {
          readXmlInputStream(it)
        }.breadthFirst()
        .filterIsInstance<Node>()
        .first()
        .toOptional()
        .map { appNode -> appNode.children()
          .filterIsInstance<Node>()
          .firstOrNull { it.attribute("name") == componentName } }
    } else {
      log.info("Configuration file $fileName is not present")
      Optional.empty()
    }
  }
}