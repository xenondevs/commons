@file:Suppress("UNCHECKED_CAST")

package xyz.xenondevs.commons.provider.immutable

import xyz.xenondevs.commons.provider.Provider

fun <T : Any, R> Provider<T>.map(transform: (T) -> R): Provider<R> {
    return MapEverythingProvider(this, transform).also(::addChild)
}

@Deprecated("Use mapNonNull instead", ReplaceWith("mapNonNull(transform)"))
@JvmName("map1")
fun <T, R> Provider<T?>.map(transform: (T & Any) -> R): Provider<R?> = mapNonNull(transform)

fun <T, R> Provider<T?>.mapNonNull(transform: (T & Any) -> R): Provider<R?> {
    return MapNonNullProvider(this, transform).also(::addChild)
}

fun <T, R> Provider<out Collection<T>>.mapEach(transform: (T) -> R): Provider<List<R>> {
    return MapEachProvider(this, ::ArrayList, transform).also(::addChild) as Provider<List<R>>
}

fun <T, R, C : MutableCollection<R>> Provider<out Collection<T>>.mapEachTo(makeCollection: () -> C, transform: (T) -> R): Provider<C> {
    return MapEachProvider(this, makeCollection, transform).also(::addChild)
}

fun <T, R : Any> Provider<out Collection<T>>.mapEachNotNull(transform: (T) -> R?): Provider<List<R>> {
    return MapEachNotNullProvider(this, ::ArrayList, transform).also(::addChild) as Provider<List<R>>
}

fun <T, R : Any, C : MutableCollection<R>> Provider<out Collection<T>>.mapEachNotNullTo(makeCollection: () -> C, transform: (T) -> R?): Provider<C> {
    return MapEachNotNullProvider(this, makeCollection, transform).also(::addChild)
}

fun <T> Provider<T?>.orElse(value: T & Any): Provider<T & Any> {
    return FallbackValueProvider(this, value).also(::addChild)
}

fun <T> Provider<T?>.orElseNullable(value: T?): Provider<T?> {
    return NullableFallbackValueProvider(this, value).also(::addChild)
}

fun <T> Provider<T?>.orElse(provider: Provider<T & Any>): Provider<T & Any> {
    return FallbackProviderProvider(this, provider).also(::addChild)
}

@JvmName("else1")
fun <T> Provider<T?>.orElse(provider: Provider<T?>): Provider<T?> {
    return NullableFallbackProviderProvider(this, provider).also(::addChild)
}

fun <T> Provider<T?>.requireNonNull(message: String): Provider<T & Any> {
    return MapEverythingProvider(this) {
        check(it != null) { message }
        it
    }.also(::addChild)
}

fun <T, R> Provider<List<T>>.flatMap(transform: (T) -> List<R>): Provider<List<R>> {
    return FlatMapProvider(this, transform).also(::addChild)
}

fun <T> Provider<List<List<T>>>.flatten(): Provider<List<T>> {
    return FlatMapProvider(this) { it }.also(::addChild)
}

fun <K, V> Provider<List<Map<K, V>>>.merged(): Provider<Map<K, V>> {
    return MergeMapsProvider(this, ::LinkedHashMap).also(::addChild)
}

fun <K, V> Provider<List<Map<K, V>>>.merged(createMap: () -> MutableMap<K, V>): Provider<Map<K, V>> {
    return MergeMapsProvider(this, createMap).also(::addChild)
}

private class MapEverythingProvider<T, R>(
    private val provider: Provider<T>,
    private val transform: (T) -> R
) : Provider<R>() {
    override fun loadValue(): R {
        return transform(provider.value)
    }
}

private class MapNonNullProvider<T, R>(
    private val provider: Provider<T>,
    private val transform: (T & Any) -> R
) : Provider<R?>() {
    override fun loadValue(): R? {
        return provider.value?.let(transform)
    }
}

private class MapEachProvider<T, R, C : MutableCollection<R>>(
    private val provider: Provider<out Collection<T>>,
    private val makeCollection: () -> C,
    private val transform: (T) -> R
) : Provider<C>() {
    override fun loadValue(): C {
        return provider.value.mapTo(makeCollection(), transform)
    }
}

private class MapEachNotNullProvider<T, R : Any, C : MutableCollection<R>>(
    private val provider: Provider<out Collection<T>>,
    private val makeCollection: () -> C,
    private val transform: (T) -> R?
) : Provider<C>() {
    override fun loadValue(): C {
        return provider.value.mapNotNullTo(makeCollection(), transform)
    }
}

private class FallbackValueProvider<T>(
    private val provider: Provider<T>,
    private val fallback: T & Any
) : Provider<T & Any>() {
    override fun loadValue(): T & Any {
        return provider.value ?: fallback
    }
}

private class NullableFallbackValueProvider<T>(
    private val provider: Provider<T>,
    private val fallback: T?
) : Provider<T?>() {
    override fun loadValue(): T? {
        return provider.value ?: fallback
    }
}

private class FallbackProviderProvider<T>(
    private val provider: Provider<T?>,
    private val fallback: Provider<T & Any>
) : Provider<T & Any>() {
    override fun loadValue(): T & Any {
        return (provider.value ?: fallback.value)
    }
}

private class NullableFallbackProviderProvider<T>(
    private val provider: Provider<T?>,
    private val fallback: Provider<T?>
) : Provider<T?>() {
    override fun loadValue(): T? {
        return (provider.value) ?: fallback.value
    }
}

private class FlatMapProvider<T, R>(
    private val provider: Provider<List<T>>,
    private val transform: (T) -> List<R>
) : Provider<List<R>>() {
    override fun loadValue(): List<R> {
        return provider.value.flatMap(transform)
    }
}

private class MergeMapsProvider<K, V>(
    private val provider: Provider<List<Map<K, V>>>,
    private val createMap: () -> MutableMap<K, V>
) : Provider<Map<K, V>>() {
    override fun loadValue(): Map<K, V> {
        val newMap = createMap()
        provider.value.forEach { newMap.putAll(it) }
        return newMap
    }
}