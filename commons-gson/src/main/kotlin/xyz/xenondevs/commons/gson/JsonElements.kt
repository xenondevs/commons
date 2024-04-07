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

fun File.parseJson(): JsonElement {
    try {
        return reader().use(JsonParser::parseReader)
    } catch (t: Throwable) {
        throw JsonParseException("Could not parse json file: $this", t)
    }
}

fun Path.parseJson(): JsonElement {
    try {
        return reader().use(JsonParser::parseReader)
    } catch (t: Throwable) {
        throw JsonParseException("Could not parse json file: $this", t)
    }
}

fun InputStream.parseJson(): JsonElement = use { JsonParser.parseReader(it.reader()) }

fun JsonElement.writeToFile(file: File) = file.writeText(toString())
fun JsonElement.writeToFile(path: Path) = path.writeText(toString())

fun JsonElement.isString(): Boolean = this is JsonPrimitive && isString
fun JsonElement.isBoolean(): Boolean = this is JsonPrimitive && isBoolean
fun JsonElement.isNumber(): Boolean = this is JsonPrimitive && isNumber