@file:Suppress("UNCHECKED_CAST")

package xyz.xenondevs.commons.provider.immutable

import xyz.xenondevs.commons.provider.Provider

fun <T : Any, R> Provider<T>.map(transform: (T) -> R): Provider<R> {
    val provider = MapEverythingProvider(this, transform)
    addChild(provider)
    return provider
}

fun <T, R> Provider<T?>.mapNonNull(transform: (T & Any) -> R): Provider<R?> {
    val provider = MapNonNullProvider(this, transform)
    addChild(provider)
    return provider
}

fun <T, R> Provider<out Collection<T>>.mapEach(transform: (T) -> R): Provider<List<R>> {
    val provider = MapEachProvider(this, ::ArrayList, transform) as Provider<List<R>>
    addChild(provider)
    return provider
}

fun <T, R, C : MutableCollection<R>> Provider<out Collection<T>>.mapEachTo(makeCollection: () -> C, transform: (T) -> R): Provider<C> {
    val provider = MapEachProvider(this, makeCollection, transform)
    addChild(provider)
    return provider
}

fun <T, R : Any> Provider<out Collection<T>>.mapEachNotNull(transform: (T) -> R?): Provider<List<R>> {
    val provider = MapEachNotNullProvider(this, ::ArrayList, transform) as Provider<List<R>>
    addChild(provider)
    return provider
}

fun <T, R : Any, C : MutableCollection<R>> Provider<out Collection<T>>.mapEachNotNullTo(makeCollection: () -> C, transform: (T) -> R?): Provider<C> {
    val provider = MapEachNotNullProvider(this, makeCollection, transform)
    addChild(provider)
    return provider
}

fun <T> Provider<T?>.orElse(value: T): Provider<T> {
    val provider = FallbackValueProvider(this, value)
    addChild(provider)
    return provider
}

// naming this function orElse would lead to a resolution ambiguity with orElse(value: T)
fun <T> Provider<T?>.orElseLazily(lazyValue: () -> T): Provider<T> {
    return orElse(provider(lazyValue))
}

fun <T> Provider<T?>.orElse(provider: Provider<T>): Provider<T> {
    val result = FallbackProviderProvider(this, provider)
    provider.addChild(result)
    addChild(result)
    return result
}

fun <T> Provider<T?>.requireNonNull(message: String): Provider<T & Any> {
    val provider = MapEverythingProvider(this) {
        check(it != null) { message }
        it
    }
    addChild(provider)
    return provider
}

fun <T, R> Provider<List<T>>.flatMap(transform: (T) -> List<R>): Provider<List<R>> {
    val provider = FlatMapProvider(this, transform)
    addChild(provider)
    return provider
}

fun <T> Provider<List<List<T>>>.flatten(): Provider<List<T>> {
    val provider = FlatMapProvider(this) { it }
    addChild(provider)
    return provider
}

fun <K, V> Provider<List<Map<K, V>>>.merged(): Provider<Map<K, V>> {
    val provider = MergeMapsProvider(this, ::LinkedHashMap)
    addChild(provider)
    return provider
}

fun <K, V> Provider<List<Map<K, V>>>.merged(createMap: () -> MutableMap<K, V>): Provider<Map<K, V>> {
    val provider = MergeMapsProvider(this, createMap)
    addChild(provider)
    return provider
}

private class MapEverythingProvider<T, R>(
    private val provider: Provider<T>,
    private val transform: (T) -> R
) : Provider<R>() {
    override fun loadValue(): R {
        return transform(provider.get())
    }
}

private class MapNonNullProvider<T, R>(
    private val provider: Provider<T>,
    private val transform: (T & Any) -> R
) : Provider<R?>() {
    override fun loadValue(): R? {
        return provider.get()?.let(transform)
    }
}

private class MapEachProvider<T, R, C : MutableCollection<R>>(
    private val provider: Provider<out Collection<T>>,
    private val makeCollection: () -> C,
    private val transform: (T) -> R
) : Provider<C>() {
    override fun loadValue(): C {
        return provider.get().mapTo(makeCollection(), transform)
    }
}

private class MapEachNotNullProvider<T, R : Any, C : MutableCollection<R>>(
    private val provider: Provider<out Collection<T>>,
    private val makeCollection: () -> C,
    private val transform: (T) -> R?
) : Provider<C>() {
    override fun loadValue(): C {
        return provider.get().mapNotNullTo(makeCollection(), transform)
    }
}

private class FallbackValueProvider<T>(
    private val provider: Provider<T?>,
    private val fallback: T
) : Provider<T>() {
    override fun loadValue(): T {
        return provider.get() ?: fallback
    }
}

private class FallbackProviderProvider<T>(
    private val provider: Provider<T?>,
    private val fallback: Provider<T>
) : Provider<T>() {
    override fun loadValue(): T {
        return (provider.get() ?: fallback.get())
    }
}

private class FlatMapProvider<T, R>(
    private val provider: Provider<List<T>>,
    private val transform: (T) -> List<R>
) : Provider<List<R>>() {
    override fun loadValue(): List<R> {
        return provider.get().flatMap(transform)
    }
}

private class MergeMapsProvider<K, V>(
    private val provider: Provider<List<Map<K, V>>>,
    private val createMap: () -> MutableMap<K, V>
) : Provider<Map<K, V>>() {
    override fun loadValue(): Map<K, V> {
        val newMap = createMap()
        provider.get().forEach { newMap.putAll(it) }
        return newMap
    }
}