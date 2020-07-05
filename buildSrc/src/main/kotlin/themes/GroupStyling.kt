package themes

enum class GroupStyling(
  val value: String
) {
  REGULAR("Regular"), ITALIC("Italic"), BOLD("Bold"), BOLD_ITALIC("Bold Italic")
}

private val styleMappings = GroupStyling.values()
  .map { it.value to it }
  .toMap()

fun String.toGroupStyle(): GroupStyling = styleMappings.getOrDefault(
  this, GroupStyling.REGULAR
)
