package com.markskelton

import org.apache.commons.io.IOUtils
import java.io.InputStream
import java.util.*

fun <T> T?.toOptional() = Optional.ofNullable(this)

// This is needed to support Android Studio, because I think it
// Still runs on a Java 8 Runtime, so no fancy Optional interfaces...
fun <T> Optional<T>.doOrElse(present: (T) -> Unit, notThere: () -> Unit) =
  this.map {
    it to true
  }.map {
    it.toOptional()
  }.orElseGet {
    (null to false).toOptional()
  }.ifPresent {
    if (it.second) {
      present(it.first)
    } else {
      notThere()
    }
  }

fun InputStream.readAllTheBytes(): ByteArray = IOUtils.toByteArray(this)
