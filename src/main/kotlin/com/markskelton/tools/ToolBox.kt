package com.markskelton.tools

fun <T> runSafelyWithResult(runner: () -> T, onError: (Throwable) -> T): T =
  try {
    runner()
  } catch (e: Throwable) {
    onError(e)
  }
