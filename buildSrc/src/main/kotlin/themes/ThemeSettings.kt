package themes


data class ThemeSettings (
  val isVivid: Boolean = false,
  val newUi: Boolean = false,
  val commentStyle: String = GroupStyling.REGULAR.value,
  val keywordStyle: String = GroupStyling.REGULAR.value,
  val attributesStyle: String = GroupStyling.REGULAR.value
)
