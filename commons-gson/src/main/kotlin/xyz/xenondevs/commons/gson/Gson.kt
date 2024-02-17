package xyz.xenondevs.commons.gson

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.TypeAdapter
import java.io.Reader
import java.lang.reflect.Type
import kotlin.reflect.javaType
import kotlin.reflect.typeOf

@OptIn(ExperimentalStdlibApi::class)
@PublishedApi
internal inline fun <reified T> javaTypeOf(): Type = typeOf<T>().javaType

inline fun <reified T> Gson.fromJson(json: String?): T? {
    if (json == null) return null
    return fromJson(json, javaTypeOf<T>())
}

inline fun <reified T> Gson.fromJson(jsonElement: JsonElement?): T? {
    if (jsonElement == null) return null
    return fromJson(jsonElement, javaTypeOf<T>())
}

inline fun <reified T> Gson.fromJson(reader: Reader): T? {
    return fromJson(reader, javaTypeOf<T>())
}

inline fun <reified T> Gson.toJsonTreeTyped(src: T): JsonElement {
    return toJsonTree(src, javaTypeOf<T>())
}

inline fun <reified T> JsonSerializationContext.serializeTyped(src: T): JsonElement {
    return serialize(src, javaTypeOf<T>())
}

inline fun <reified T> JsonDeserializationContext.deserializeTyped(json: JsonElement): T {
    return deserialize(json, javaTypeOf<T>())
}

inline fun <reified T> GsonBuilder.registerTypeAdapter(typeAdapter: JsonSerializer<T>): GsonBuilder {
    return registerTypeAdapter(javaTypeOf<T>(), typeAdapter)
}

inline fun <reified T> GsonBuilder.registerTypeAdapter(typeAdapter: JsonDeserializer<T>): GsonBuilder {
    return registerTypeAdapter(javaTypeOf<T>(), typeAdapter)
}

@JvmName("registerTypeAdapter1")
inline fun <reified T, A> GsonBuilder.registerTypeAdapter(typeAdapter: A): GsonBuilder where A : JsonSerializer<T>, A : JsonDeserializer<T> {
    return registerTypeAdapter(javaTypeOf<T>(), typeAdapter)
}

inline fun <reified T> GsonBuilder.registerTypeAdapter(typeAdapter: TypeAdapter<T>): GsonBuilder {
    return registerTypeAdapter(javaTypeOf<T>(), typeAdapter)
}

inline fun <reified T> GsonBuilder.registerTypeHierarchyAdapter(typeAdapter: JsonSerializer<T>): GsonBuilder {
    return registerTypeHierarchyAdapter(T::class.java, typeAdapter)
}

inline fun <reified T> GsonBuilder.registerTypeHierarchyAdapter(typeAdapter: JsonDeserializer<T>): GsonBuilder {
    return registerTypeHierarchyAdapter(T::class.java, typeAdapter)
}

@JvmName("registerTypeHierarchyAdapter1")
inline fun <reified T, A> GsonBuilder.registerTypeHierarchyAdapter(typeAdapter: A): GsonBuilder where A : JsonSerializer<T>, A : JsonDeserializer<T> {
    return registerTypeHierarchyAdapter(T::class.java, typeAdapter)
}

inline fun <reified T> GsonBuilder.registerTypeHierarchyAdapter(typeAdapter: TypeAdapter<T>): GsonBuilder {
    return registerTypeHierarchyAdapter(T::class.java, typeAdapter)
}