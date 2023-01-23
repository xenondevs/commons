package xyz.xenondevs.commons.gson

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonSerializer
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import java.io.Reader
import java.lang.reflect.Type

@PublishedApi
internal inline fun <reified T> typeOf(): Type = object : TypeToken<T>() {}.type

inline fun <reified T> Gson.fromJson(json: String?): T? {
    if (json == null) return null
    return fromJson(json, typeOf<T>())
}

inline fun <reified T> Gson.fromJson(jsonElement: JsonElement?): T? {
    if (jsonElement == null) return null
    return fromJson(jsonElement, typeOf<T>())
}

inline fun <reified T> Gson.fromJson(reader: Reader): T? {
    return fromJson(reader, typeOf<T>())
}

inline fun <reified T> GsonBuilder.registerTypeAdapter(typeAdapter: JsonSerializer<T>): GsonBuilder {
    return registerTypeAdapter(typeOf<T>(), typeAdapter)
}

inline fun <reified T> GsonBuilder.registerTypeAdapter(typeAdapter: JsonDeserializer<T>): GsonBuilder {
    return registerTypeAdapter(typeOf<T>(), typeAdapter)
}

@JvmName("registerTypeAdapter1")
inline fun <reified T, A> GsonBuilder.registerTypeAdapter(typeAdapter: A): GsonBuilder where A : JsonSerializer<T>, A : JsonDeserializer<T> {
    return registerTypeAdapter(typeOf<T>(), typeAdapter)
}

inline fun <reified T> GsonBuilder.registerTypeAdapter(typeAdapter: TypeAdapter<T>): GsonBuilder {
    return registerTypeAdapter(typeOf<T>(), typeAdapter)
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