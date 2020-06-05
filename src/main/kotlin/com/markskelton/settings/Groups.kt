package com.markskelton.settings

import com.markskelton.toOptional

enum class Groups(val value: String) {
  ATTRIBUTES("attributes"), COMMENTS("comments"), KEYWORDS("keywords")
}

private val groupMappings = Groups.values()
  .map { it.value to it }
  .toMap()

fun String.toGroup(): Groups = groupMappings[this]
  .toOptional()
  .orElseThrow { IllegalStateException("Unknown grouping $this") }
