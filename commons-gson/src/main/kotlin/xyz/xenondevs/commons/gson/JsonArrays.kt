@file:Suppress("NOTHING_TO_INLINE")

package xyz.xenondevs.commons.gson

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject

inline fun JsonArray.addAll(vararg numbers: Number) = numbers.forEach(this::add)
inline fun JsonArray.addAll(vararg booleans: Boolean) = booleans.forEach(this::add)
inline fun JsonArray.addAll(vararg chars: Char) = chars.forEach(this::add)
inline fun JsonArray.addAll(vararg strings: String) = strings.forEach(this::add)
inline fun JsonArray.addAll(vararg elements: JsonElement) = elements.forEach(this::add)
inline fun JsonArray.addAll(intArray: IntArray) = intArray.forEach(this::add)
inline fun JsonArray.addAll(longArray: LongArray) = longArray.forEach(this::add)
inline fun JsonArray.addAll(floatArray: FloatArray) = floatArray.forEach(this::add)
inline fun JsonArray.addAll(doubleArray: DoubleArray) = doubleArray.forEach(this::add)

@JvmName("addAllBooleanArray")
inline fun JsonArray.addAll(booleanArray: BooleanArray) = booleanArray.forEach(this::add)
@JvmName("addAllCharArray")
inline fun JsonArray.addAll(charArray: CharArray) = charArray.forEach(this::add)
@JvmName("addAllStringArray")
inline fun JsonArray.addAll(stringArray: Array<String>) = stringArray.forEach(this::add)
@JvmName("addAllElementsArray")
inline fun JsonArray.addAll(elementArray: Array<JsonElement>) = elementArray.forEach(this::add)
@JvmName("addAllNumbers")
inline fun JsonArray.addAll(numbers: Iterable<Number>) = numbers.forEach(this::add)
@JvmName("addAllBooleans")
inline fun JsonArray.addAll(booleans: Iterable<Boolean>) = booleans.forEach(this::add)
@JvmName("addAllChars")
inline fun JsonArray.addAll(chars: Iterable<Char>) = chars.forEach(this::add)
@JvmName("addAllStrings")
inline fun JsonArray.addAll(strings: Iterable<String>) = strings.forEach(this::add)
@JvmName("addAllElements")
inline fun JsonArray.addAll(elements: Iterable<JsonElement>) = elements.forEach(this::add)

inline fun JsonArray.getAllStrings(): List<String> {
    return getAllStringsTo(ArrayList())
}

inline fun <C : MutableCollection<in String>> JsonArray.getAllStringsTo(destination: C): C {
    return mapNotNullTo(destination) { if (it.isString()) it.asString else null }
}

inline fun JsonArray.getAllInts(): List<Int> {
    return getAllIntsTo(ArrayList())
}

inline fun <C : MutableCollection<in Int>> JsonArray.getAllIntsTo(destination: C): C {
    return mapNotNullTo(destination) { if (it.isNumber()) it.asInt else null }
}

inline fun JsonArray.getAllLongs(): List<Long> {
    return getAllLongsTo(ArrayList())
}

inline fun <C : MutableCollection<in Long>> JsonArray.getAllLongsTo(destination: C): C {
    return mapNotNullTo(destination) { if (it.isNumber()) it.asLong else null }
}

inline fun JsonArray.getAllFloats(): List<Float> {
    return getAllFloatsTo(ArrayList())
}

inline fun <C : MutableCollection<in Float>> JsonArray.getAllFloatsTo(destination: C): C {
    return mapNotNullTo(destination) { if (it.isNumber()) it.asFloat else null }
}

inline fun JsonArray.getAllDoubles(): List<Double> {
    return getAllDoublesTo(ArrayList())
}

inline fun <C : MutableCollection<in Double>> JsonArray.getAllDoublesTo(destination: C): C {
    return mapNotNullTo(destination) { if (it.isNumber()) it.asDouble else null }
}

inline fun JsonArray.getAllBooleans(): List<Boolean> {
    return getAllBooleansTo(ArrayList())
}

inline fun <C : MutableCollection<in Boolean>> JsonArray.getAllBooleansTo(destination: C): C {
    return mapNotNullTo(destination) { if (it.isBoolean()) it.asBoolean else null }
}

inline fun JsonArray.getAllChars(): List<Char> {
    return getAllCharsTo(ArrayList())
}

inline fun <C : MutableCollection<in Char>> JsonArray.getAllCharsTo(destination: C): C {
    return mapNotNullTo(destination) { if (it.isString()) it.asString.firstOrNull() else null }
}

inline fun JsonArray.getAllJsonObjects(): List<JsonObject> {
    return getAllJsonObjectsTo(ArrayList())
}

inline fun <C : MutableCollection<in JsonObject>> JsonArray.getAllJsonObjectsTo(destination: C): C {
    return filterIsInstanceTo(destination, JsonObject::class.java)
}

inline fun JsonArray.getAllJsonArrays(): List<JsonArray> {
    return getAllJsonArraysTo(ArrayList())
}

inline fun <C : MutableCollection<in JsonArray>> JsonArray.getAllJsonArraysTo(destination: C): C {
    return filterIsInstanceTo(destination, JsonArray::class.java)
}