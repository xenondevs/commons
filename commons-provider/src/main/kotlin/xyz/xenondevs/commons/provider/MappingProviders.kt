@file:OptIn(UnstableProviderApi::class)

package xyz.xenondevs.commons.provider

import kotlin.concurrent.withLock

/**
 * Creates and returns a new [Provider] that maps the value of [this][Provider]
 * using the [transform] function.
 */
fun <T, R> Provider<T>.map(transform: (T) -> R): Provider<R> =
    MappingProvider(this as AbstractProvider<T>, transform)

/**
 * Creates and returns a new [Provider] that maps to the value of the [Provider]
 * returned by [transform].
 */
@Suppress("UNCHECKED_CAST")
@JvmName("flatMapToProvider")
fun <T, R> Provider<T>.flatMap(transform: (T) -> Provider<R>): Provider<R> =
    FlatMappingProvider(this as AbstractProvider<T>, transform as (T) -> AbstractProvider<R>)

/**
 * Creates and returns a new [Provider] that maps to the value of the [MutableProvider]
 * returned by [transform].
 */
@Suppress("UNCHECKED_CAST")
@JvmName("flatMapToMutableProvider")
fun <T, R> Provider<T>.flatMapMutable(transform: (T) -> MutableProvider<R>): MutableProvider<R> = // naming this function flatMap would lead to a resolution ambiguity with flatMap(transform: (a: T) -> Provider<R>)
    FlatMappingProvider(this as AbstractProvider<T>, transform as (T) -> AbstractProvider<R>)

/**
 * Creates and returns a new [Provider] that maps non-null values of [this][Provider]
 * using the [transform] function.
 * Null values will be passed through without transformation.
 */
inline fun <T : Any, R> Provider<T?>.mapNonNull(crossinline transform: (T) -> R): Provider<R?> =
    map { it?.let(transform) }

/**
 * Creates and returns a new [Provider] that maps each element of the [Collection] obtained from [this][Provider]
 * using the [transform] function.
 */
inline fun <T, R> Provider<Collection<T>>.mapEach(crossinline transform: (T) -> R): Provider<List<R>> =
    mapEachTo({ size -> ArrayList(size) }, transform)

/**
 * Creates and returns a new [Provider] that maps each element of the [Collection] obtained from [this][Provider]
 * using the [transform] function and adds the results to a collection created by [makeCollection].
 */
inline fun <T, R, C : MutableCollection<in R>> Provider<Collection<T>>.mapEachTo(
    crossinline makeCollection: (size: Int) -> C,
    crossinline transform: (T) -> R
): Provider<C> = map { it.mapTo(makeCollection(it.size), transform) }

/**
 * Creates and returns a new [Provider] that maps each element of the [Collection] obtained from [this][Provider]
 * using the [transform] function and filters out all null results.
 */
inline fun <T, R : Any> Provider<Collection<T>>.mapEachNotNull(crossinline transform: (T) -> R?): Provider<List<R>> =
    mapEachNotNullTo(::ArrayList, transform)

/**
 * Creates and returns a new [Provider] that maps each element of the [Collection] obtained from [this][Provider]
 * using the [transform] function and filters out all null results.
 * The results are added to a collection created by [makeCollection].
 */
inline fun <T, R : Any, C : MutableCollection<in R>> Provider<Collection<T>>.mapEachNotNullTo(
    crossinline makeCollection: (size: Int) -> C,
    crossinline transform: (T) -> R?
): Provider<C> = map { it.mapNotNullTo(makeCollection(it.size), transform) }

/**
 * Creates and returns a new [Provider] that flat-maps the elements of the [Collection] obtained from [this][Provider]
 * into a list using the [transform] function.
 */
@JvmName("flatMapToList")
inline fun <T, R> Provider<Collection<T>>.flatMapCollection(crossinline transform: (T) -> Iterable<R>): Provider<List<R>> =
    flatMapCollectionTo({ size -> ArrayList(size) }, transform)

/**
 * Creates and returns a new [Provider] that flat-maps the elements of the [Collection] obtained from [this][Provider]
 * into a collection created by [makeCollection] using the [transform] function.
 */
inline fun <T, R, C : MutableCollection<in R>> Provider<Collection<T>>.flatMapCollectionTo(
    crossinline makeCollection: (size: Int) -> C,
    crossinline transform: (T) -> Iterable<R>
): Provider<C> = map { it.flatMapTo(makeCollection(it.size), transform) }

/**
 * Creates and returns a new [Provider] that flattens the [List] of [Lists][List] obtained from [this][Provider].
 */
fun <T> Provider<Iterable<Iterable<T>>>.flattenIterables(): Provider<List<T>> =
    map { it.flatten() }

/**
 * Creates and returns a new [Provider] that merges all [Maps][Map] obtained from [this][Provider] into a single [Map].
 */
fun <K, V> Provider<List<Map<K, V>>>.mergeMaps(): Provider<Map<K, V>> =
    mergeMapsTo(::HashMap)

/**
 * Creates and returns a new [Provider] that merges all [Maps][Map] obtained from [this][Provider] into a single [Map],
 * which is created by the [makeMap] function.
 */
fun <K, V, M : MutableMap<in K, in V>> Provider<List<Map<K, V>>>.mergeMapsTo(makeMap: (size: Int) -> M): Provider<M> =
    map { maps ->
        val size = maps.sumOf { it.size }
        val map = makeMap(size)
        maps.forEach(map::putAll)
        map
    }

/**
 * Creates and returns a new [Provider] that throws an [IllegalArgumentException]
 * with a message generated by [message] if [condition] fails.
 */
inline fun <T> Provider<T>.require(
    crossinline condition: (T) -> Boolean,
    crossinline message: (T) -> String
): Provider<T> = map { require(condition(it)) { message(it) }; it }

/**
 * Creates and returns a new [Provider] that throws an [IllegalArgumentException]
 * with [message] if the value is `null`.
 */
fun <T : Any> Provider<T?>.requireNotNull(message: String = "Required value was null."): Provider<T> =
    requireNotNull { message }

/**
 * Creates and returns a new [Provider] that throws an [IllegalArgumentException]
 * with a message generated by [message] if the value is `null`.
 */
inline fun <T : Any> Provider<T?>.requireNotNull(crossinline message: () -> String): Provider<T> =
    map { requireNotNull(it, message); it }

private class MappingProvider<P, T>(
    private val parent: AbstractProvider<P>,
    private val transform: (P) -> T
) : AbstractProvider<T>(parent.lock) {
    
    init {
        lock.withLock {
            addInactiveParent(parent)
            parent.addChild(this)
        }
    }
    
    override fun pull(): T {
        return transform(parent.get())
    }
    
}

private class FlatMappingProvider<P, T>(
    private val parent: AbstractProvider<P>,
    private val transform: (P) -> AbstractProvider<T>
) : AbstractProvider<T>(parent.lock) {
    
    private var provider: AbstractProvider<T>
    
    init {
        lock.withLock {
            addInactiveParent(parent)
            parent.addChild(this)
            
            provider = transform(parent.get())
            provider.changeLock(lock)
            provider.addChild(this)
            addParent(provider, ignored = setOf(parent)) { it }
            
            parent.subscribeWeak(this) { thisRef, parentValue ->
                thisRef.handleProviderUpdate(transform(parentValue))
            }
        }
    }
    
    private fun handleProviderUpdate(new: AbstractProvider<T>) {
        if (new == provider)
            return
        
        provider.removeChild(this)
        removeParent(provider)
        
        provider = new
        provider.changeLock(lock)
        provider.addChild(this)
        addParent(provider, ignored = setOf(parent)) { it }
    }
    
    override fun pull(): T {
        return provider.get()
    }
    
}

/**
 * Creates and returns a new [MutableProvider] that maps the value of [this][MutableProvider]
 * bi-directionally using the provided [transform] and [untransform] functions.
 */
fun <T, R> MutableProvider<T>.map(transform: (T) -> R, untransform: (R) -> T): MutableProvider<R> =
    MutableMappingProvider(this as AbstractProvider<T>, transform, untransform)

/**
 * Creates and returns a new [MutableProvider] that maps non-null values of [this][MutableProvider]
 * bi-directionally using the provided [transform] and [untransform] functions.
 * Null values will be passed through without transformation.
 */
inline fun <T : Any, R : Any> MutableProvider<T?>.mapNonNull(
    crossinline transform: (T) -> R?,
    crossinline untransform: (R) -> T?
): MutableProvider<R?> = map({ it?.let(transform) }, { it?.let(untransform) })

private class MutableMappingProvider<P, T>(
    private val parent: AbstractProvider<P>,
    private val transform: (P) -> T,
    private val untransform: (T) -> P
) : AbstractProvider<T>(parent.lock) {
    
    init {
        lock.withLock {
            addParent(parent) { untransform(it) }
            parent.addChild(this)
        }
    }
    
    override fun pull(): T {
        return transform(parent.get())
    }
    
}