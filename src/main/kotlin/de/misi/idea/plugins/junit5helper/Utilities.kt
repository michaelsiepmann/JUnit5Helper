package de.misi.idea.plugins.junit5helper

import java.text.Normalizer
import java.util.regex.Pattern

fun String.removeAccents(): String = Pattern.compile("\\p{InCombiningDiacriticalMarks}+").matcher(Normalizer.normalize(this, Normalizer.Form.NFD)).replaceAll("")

fun String.upperFirstChar(): String = convertFirstChar(this, String::toUpperCase)

fun String.lowerFirstChar(): String = convertFirstChar(this, String::toLowerCase)

private fun convertFirstChar(text: String, converter: (String.() -> String)): String {
    if (text.isEmpty()) {
        return text
    }
    if (text.length == 1) {
        return text.converter()
    }
    return text.substring(0, 1).converter() + text.substring(1)
}