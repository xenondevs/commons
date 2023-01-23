@file:Suppress("NOTHING_TO_INLINE")

package xyz.xenondevs.commons.gson

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject

inline operator fun JsonObject.set(key: String, value: JsonElement) = add(key, value)

inline fun JsonObject.hasString(key: String): Boolean = has(key) && get(key).isString()
inline fun JsonObject.hasNumber(key: String): Boolean = has(key) && get(key).isNumber()
inline fun JsonObject.hasBoolean(key: String): Boolean = has(key) && get(key).isBoolean()
inline fun JsonObject.hasObject(key: String): Boolean = has(key) && get(key) is JsonObject
inline fun JsonObject.hasArray(key: String): Boolean = has(key) && get(key) is JsonArray

inline fun JsonObject.getOrNull(key: String): JsonElement? = if (has(key)) get(key) else null
inline fun JsonObject.getStringOrNull(key: String): String? = if (hasString(key)) get(key).asString else null
inline fun JsonObject.getBooleanOrNull(key: String): Boolean? = if (hasBoolean(key)) get(key).asBoolean else null
inline fun JsonObject.getNumberOrNull(key: String): Number? = if (hasNumber(key)) get(key).asNumber else null
inline fun JsonObject.getByteOrNull(key: String): Byte? = if (hasNumber(key)) get(key).asByte else null
inline fun JsonObject.getShortOrNull(key: String): Short? = if (hasNumber(key)) get(key).asShort else null
inline fun JsonObject.getIntOrNull(key: String): Int? = if (hasNumber(key)) get(key).asInt else null
inline fun JsonObject.getLongOrNull(key: String): Long? = if (hasNumber(key)) get(key).asLong else null
inline fun JsonObject.getFloatOrNull(key: String): Float? = if (hasNumber(key)) get(key).asFloat else null
inline fun JsonObject.getDoubleOrNull(key: String): Double? = if (hasNumber(key)) get(key).asDouble else null
inline fun JsonObject.getObjectOrNull(key: String): JsonObject? = if (hasObject(key)) get(key).asJsonObject else null
inline fun JsonObject.getArrayOrNull(key: String): JsonArray? = if (hasArray(key)) get(key).asJsonArray else null

inline fun JsonObject.getString(key: String): String = if (hasString(key)) get(key).asString else throw NoSuchElementException("No String with key $key found!")
inline fun JsonObject.getBoolean(key: String): Boolean = if (hasBoolean(key)) get(key).asBoolean else throw NoSuchElementException("No Boolean with key $key found!")
inline fun JsonObject.getNumber(key: String): Number = if (hasNumber(key)) get(key).asNumber else throw NoSuchElementException("No Number with key $key found!")
inline fun JsonObject.getByte(key: String): Byte = if (hasNumber(key)) get(key).asByte else throw NoSuchElementException("No Number with key $key found!")
inline fun JsonObject.getShort(key: String): Short = if (hasNumber(key)) get(key).asShort else throw NoSuchElementException("No Number with key $key found!")
inline fun JsonObject.getInt(key: String): Int = if (hasNumber(key)) get(key).asInt else throw NoSuchElementException("No Number with key $key found!")
inline fun JsonObject.getLong(key: String): Long = if (hasNumber(key)) get(key).asLong else throw NoSuchElementException("No Number with key $key found!")
inline fun JsonObject.getFloat(key: String): Float = if (hasNumber(key)) get(key).asFloat else throw NoSuchElementException("No Number with key $key found!")
inline fun JsonObject.getDouble(key: String): Double = if (hasNumber(key)) get(key).asDouble else throw NoSuchElementException("No Number with key $key found!")
inline fun JsonObject.getObject(key: String): JsonObject = if (hasObject(key)) get(key).asJsonObject else throw NoSuchElementException("No JsonObject with key $key found!")
inline fun JsonObject.getArray(key: String): JsonArray = if (hasArray(key)) get(key).asJsonArray else throw NoSuchElementException("No JsonArray with key $key found!")

inline fun <reified T : JsonElement> JsonObject.getOrPut(key: String, defaultValue: () -> T): T {
    var value = getOrNull(key)
    if (value !is T) {
        value = defaultValue()
        add(key, value)
    }
    
    return value
}