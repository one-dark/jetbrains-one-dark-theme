package themes

import toOptional


enum class Groups(val value: String) {
  ATTRIBUTES("attributes"), COMMENTS("comments"), KEYWORDS("keywords"), IDENTIFIERS("identifiers")
}

private val groupMappings = Groups.values()
  .map { it.value to it }
  .toMap()

fun String.toGroup(): Groups = groupMappings[this]
  .toOptional()
  .orElseThrow { IllegalStateException("Unknown grouping $this") }
