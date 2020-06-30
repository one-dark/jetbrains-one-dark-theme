package com.markskelton.utils

import groovy.util.Node
import groovy.util.XmlParser
import org.xml.sax.ErrorHandler
import org.xml.sax.InputSource
import org.xml.sax.SAXParseException
import java.io.InputStream
import java.io.InputStreamReader

fun readXmlInputStream(input: InputStream): Node {
  val inputSource = InputSource(InputStreamReader(input, "UTF-8"))
  val parser = XmlParser(false, true, true)
  parser.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)
  parser.errorHandler = object : ErrorHandler {
    override fun warning(exception: SAXParseException?) {}

    override fun error(exception: SAXParseException?) {}

    override fun fatalError(exception: SAXParseException) {
      throw exception
    }
  }
  return parser.parse(inputSource)
}