package xyz.xenondevs.commons.gson

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject

fun JsonArray.addAll(vararg numbers: Number) = numbers.forEach(this::add)
fun JsonArray.addAll(vararg booleans: Boolean) = booleans.forEach(this::add)
fun JsonArray.addAll(vararg chars: Char) = chars.forEach(this::add)
fun JsonArray.addAll(vararg strings: String) = strings.forEach(this::add)
fun JsonArray.addAll(vararg elements: JsonElement) = elements.forEach(this::add)
fun JsonArray.addAll(intArray: IntArray) = intArray.forEach(this::add)
fun JsonArray.addAll(longArray: LongArray) = longArray.forEach(this::add)
fun JsonArray.addAll(floatArray: FloatArray) = floatArray.forEach(this::add)
fun JsonArray.addAll(doubleArray: DoubleArray) = doubleArray.forEach(this::add)

@JvmName("addAllBooleanArray")
fun JsonArray.addAll(booleanArray: BooleanArray) = booleanArray.forEach(this::add)
@JvmName("addAllCharArray")
fun JsonArray.addAll(charArray: CharArray) = charArray.forEach(this::add)
@JvmName("addAllStringArray")
fun JsonArray.addAll(stringArray: Array<String>) = stringArray.forEach(this::add)
@JvmName("addAllElementsArray")
fun JsonArray.addAll(elementArray: Array<JsonElement>) = elementArray.forEach(this::add)
@JvmName("addAllNumbers")
fun JsonArray.addAll(numbers: Iterable<Number>) = numbers.forEach(this::add)
@JvmName("addAllBooleans")
fun JsonArray.addAll(booleans: Iterable<Boolean>) = booleans.forEach(this::add)
@JvmName("addAllChars")
fun JsonArray.addAll(chars: Iterable<Char>) = chars.forEach(this::add)
@JvmName("addAllStrings")
fun JsonArray.addAll(strings: Iterable<String>) = strings.forEach(this::add)
@JvmName("addAllElements")
fun JsonArray.addAll(elements: Iterable<JsonElement>) = elements.forEach(this::add)

fun BooleanArray.toJsonArray() = JsonArray().also { it.addAll(this) }
fun CharArray.toJsonArray() = JsonArray().also { it.addAll(this) }
fun IntArray.toJsonArray() = JsonArray().also { it.addAll(this) }
fun LongArray.toJsonArray() = JsonArray().also { it.addAll(this) }
fun FloatArray.toJsonArray() = JsonArray().also { it.addAll(this) }
fun DoubleArray.toJsonArray() = JsonArray().also { it.addAll(this) }
fun Array<String>.toJsonArray() = JsonArray().also { it.addAll(this) }
fun Array<JsonElement>.toJsonArray() = JsonArray().also { it.addAll(this) }
@JvmName("toJsonArrayNumbers")
fun Iterable<Number>.toJsonArray() = JsonArray().also { it.addAll(this) }
@JvmName("toJsonArrayBooleans")
fun Iterable<Boolean>.toJsonArray() = JsonArray().also { it.addAll(this) }
@JvmName("toJsonArrayChars")
fun Iterable<Char>.toJsonArray() = JsonArray().also { it.addAll(this) }
@JvmName("toJsonArrayStrings")
fun Iterable<String>.toJsonArray() = JsonArray().also { it.addAll(this) }
@JvmName("toJsonArrayElements")
fun Iterable<JsonElement>.toJsonArray() = JsonArray().also { it.addAll(this) }

fun JsonArray.getAllStrings(): List<String> {
    return getAllStringsTo(ArrayList())
}

fun <C : MutableCollection<in String>> JsonArray.getAllStringsTo(destination: C): C {
    return mapNotNullTo(destination) { if (it.isString()) it.asString else null }
}

fun JsonArray.getAllInts(): List<Int> {
    return getAllIntsTo(ArrayList())
}

fun <C : MutableCollection<in Int>> JsonArray.getAllIntsTo(destination: C): C {
    return mapNotNullTo(destination) { if (it.isNumber()) it.asInt else null }
}

fun JsonArray.getAllLongs(): List<Long> {
    return getAllLongsTo(ArrayList())
}

fun <C : MutableCollection<in Long>> JsonArray.getAllLongsTo(destination: C): C {
    return mapNotNullTo(destination) { if (it.isNumber()) it.asLong else null }
}

fun JsonArray.getAllFloats(): List<Float> {
    return getAllFloatsTo(ArrayList())
}

fun <C : MutableCollection<in Float>> JsonArray.getAllFloatsTo(destination: C): C {
    return mapNotNullTo(destination) { if (it.isNumber()) it.asFloat else null }
}

fun JsonArray.getAllDoubles(): List<Double> {
    return getAllDoublesTo(ArrayList())
}

fun <C : MutableCollection<in Double>> JsonArray.getAllDoublesTo(destination: C): C {
    return mapNotNullTo(destination) { if (it.isNumber()) it.asDouble else null }
}

fun JsonArray.getAllBooleans(): List<Boolean> {
    return getAllBooleansTo(ArrayList())
}

fun <C : MutableCollection<in Boolean>> JsonArray.getAllBooleansTo(destination: C): C {
    return mapNotNullTo(destination) { if (it.isBoolean()) it.asBoolean else null }
}

fun JsonArray.getAllChars(): List<Char> {
    return getAllCharsTo(ArrayList())
}

fun <C : MutableCollection<in Char>> JsonArray.getAllCharsTo(destination: C): C {
    return mapNotNullTo(destination) { if (it.isString()) it.asString.firstOrNull() else null }
}

fun JsonArray.getAllJsonObjects(): List<JsonObject> {
    return getAllJsonObjectsTo(ArrayList())
}

fun <C : MutableCollection<in JsonObject>> JsonArray.getAllJsonObjectsTo(destination: C): C {
    return filterIsInstanceTo(destination, JsonObject::class.java)
}

fun JsonArray.getAllJsonArrays(): List<JsonArray> {
    return getAllJsonArraysTo(ArrayList())
}

fun <C : MutableCollection<in JsonArray>> JsonArray.getAllJsonArraysTo(destination: C): C {
    return filterIsInstanceTo(destination, JsonArray::class.java)
}