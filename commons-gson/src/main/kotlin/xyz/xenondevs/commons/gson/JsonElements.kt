@file:Suppress("NOTHING_TO_INLINE")

package xyz.xenondevs.commons.gson

import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import com.google.gson.JsonPrimitive
import java.io.File
import java.io.InputStream
import java.nio.file.Path
import kotlin.io.path.reader
import kotlin.io.path.writeText

inline fun File.parseJson(): JsonElement {
    try {
        return reader().use(JsonParser::parseReader)
    } catch (t: Throwable) {
        throw JsonParseException("Could not parse json file: $this", t)
    }
}

inline fun Path.parseJson(): JsonElement {
    try {
        return reader().use(JsonParser::parseReader)
    } catch (t: Throwable) {
        throw JsonParseException("Could not parse json file: $this", t)
    }
}

inline fun InputStream.parseJson(): JsonElement = use { JsonParser.parseReader(it.reader()) }

inline fun JsonElement.writeToFile(file: File) = file.writeText(toString())
inline fun JsonElement.writeToFile(path: Path) = path.writeText(toString())

inline fun JsonElement.isString(): Boolean = this is JsonPrimitive && isString
inline fun JsonElement.isBoolean(): Boolean = this is JsonPrimitive && isBoolean
inline fun JsonElement.isNumber(): Boolean = this is JsonPrimitive && isNumber