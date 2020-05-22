package com.markskelton.legacy

enum class LegacyThemes {
ITALIC, VIVID, VIVID_ITALIC
}

object LegacyMigration {
  private val legacyThemes = mapOf(
    "1a92aa6f-c2f1-4994-ae01-6a78e43eeb24" to LegacyThemes.ITALIC,
    "4b6007f7-b596-4ee2-96f9-968d3d3eb392" to LegacyThemes.VIVID,
    "4f556d32-83cb-4b8b-9932-c4eccc4ce3af" to LegacyThemes.VIVID_ITALIC
  )

  fun migrateIfNecessary() {
    // todo: this!
  }
}