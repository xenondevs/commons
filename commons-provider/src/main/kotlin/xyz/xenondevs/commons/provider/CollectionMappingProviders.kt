package xyz.xenondevs.commons.provider

import java.lang.ref.WeakReference

/**
 * Creates and returns a new [Provider] that maps each element of the [Collection] obtained from [this][Provider]
 * using the [transform] function.
 *
 * [transform] should be a pure function.
 */
inline fun <T, R> Provider<Collection<T>>.mapEach(crossinline transform: (T) -> R): Provider<List<R>> =
    mapEachTo({ size -> ArrayList(size) }, transform)

/**
 * Creates and returns a new [Provider] that maps each element of the [Collection] obtained from [this][Provider]
 * using the [transform] function.
 *
 * [transform] should be a pure function.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
inline fun <T, R> Provider<Collection<T>>.weakMapEach(crossinline transform: (T) -> R): Provider<List<R>> =
    weakMapEachTo({ size -> ArrayList(size) }, transform)

/**
 * Creates and returns a new [Provider] that maps each element of the [Collection] obtained from [this][Provider]
 * using the [transform] function and adds the results to a collection created by [makeCollection].
 *
 * [makeCollection] and [transform] should be pure functions.
 */
inline fun <T, R, C : MutableCollection<in R>> Provider<Collection<T>>.mapEachTo(
    crossinline makeCollection: (size: Int) -> C,
    crossinline transform: (T) -> R
): Provider<C> = map { it.mapTo(makeCollection(it.size), transform) }

/**
 * Creates and returns a new [Provider] that maps each element of the [Collection] obtained from [this][Provider]
 * using the [transform] function and adds the results to a collection created by [makeCollection].
 *
 * [makeCollection] and [transform] should be pure functions.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
inline fun <T, R, C : MutableCollection<in R>> Provider<Collection<T>>.weakMapEachTo(
    crossinline makeCollection: (size: Int) -> C,
    crossinline transform: (T) -> R
): Provider<C> = weakMap { it.mapTo(makeCollection(it.size), transform) }

/**
 * Creates and returns a new [Provider] that maps each element of the [Collection] obtained from [this][Provider]
 * using the [transform] function and filters out all null results.
 *
 * [transform] should be a pure function.
 */
inline fun <T, R : Any> Provider<Collection<T>>.mapEachNotNull(crossinline transform: (T) -> R?): Provider<List<R>> =
    mapEachNotNullTo(::ArrayList, transform)

/**
 * Creates and returns a new [Provider] that maps each element of the [Collection] obtained from [this][Provider]
 * using the [transform] function and filters out all null results.
 *
 * [transform] should be a pure function.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
inline fun <T, R : Any> Provider<Collection<T>>.weakMapEachNotNull(crossinline transform: (T) -> R?): Provider<List<R>> =
    weakMapEachNotNullTo(::ArrayList, transform)

/**
 * Creates and returns a new [Provider] that maps each element of the [Collection] obtained from [this][Provider]
 * using the [transform] function and filters out all null results.
 * The results are added to a collection created by [makeCollection].
 *
 * [makeCollection] and [transform] should be pure functions.
 */
inline fun <T, R : Any, C : MutableCollection<in R>> Provider<Collection<T>>.mapEachNotNullTo(
    crossinline makeCollection: (size: Int) -> C,
    crossinline transform: (T) -> R?
): Provider<C> = map { it.mapNotNullTo(makeCollection(it.size), transform) }

/**
 * Creates and returns a new [Provider] that maps each element of the [Collection] obtained from [this][Provider]
 * using the [transform] function and filters out all null results.
 * The results are added to a collection created by [makeCollection].
 *
 * [makeCollection] and [transform] should be pure functions.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
inline fun <T, R : Any, C : MutableCollection<in R>> Provider<Collection<T>>.weakMapEachNotNullTo(
    crossinline makeCollection: (size: Int) -> C,
    crossinline transform: (T) -> R?
): Provider<C> = weakMap { it.mapNotNullTo(makeCollection(it.size), transform) }


/**
 * Creates and returns a new [Provider] that flat-maps the elements of the [Collection] obtained from [this][Provider]
 * into a list using the [transform] function.
 *
 * [transform] should be a pure function.
 */
@JvmName("flatMapCollection")
inline fun <T, R> Provider<Collection<T>>.flatMapCollection(crossinline transform: (T) -> Iterable<R>): Provider<List<R>> =
    flatMapCollectionTo({ size -> ArrayList(size) }, transform)

/**
 * Creates and returns a new [Provider] that flat-maps the elements of the [Collection] obtained from [this][Provider]
 * into a list using the [transform] function.
 *
 * [transform] should be a pure function.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
@JvmName("weakFlatMapCollection")
inline fun <T, R> Provider<Collection<T>>.weakFlatMapCollection(crossinline transform: (T) -> Iterable<R>): Provider<List<R>> =
    weakFlatMapCollectionTo({ size -> ArrayList(size) }, transform)

/**
 * Creates and returns a new [Provider] that flat-maps the elements of the [Collection] obtained from [this][Provider]
 * into a collection created by [makeCollection] using the [transform] function.
 *
 * [makeCollection] and [transform] should be pure functions.
 */
inline fun <T, R, C : MutableCollection<in R>> Provider<Collection<T>>.flatMapCollectionTo(
    crossinline makeCollection: (size: Int) -> C,
    crossinline transform: (T) -> Iterable<R>
): Provider<C> = map { it.flatMapTo(makeCollection(it.size), transform) }

/**
 * Creates and returns a new [Provider] that flat-maps the elements of the [Collection] obtained from [this][Provider]
 * into a collection created by [makeCollection] using the [transform] function.
 *
 * [makeCollection] and [transform] should be pure functions.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
inline fun <T, R, C : MutableCollection<in R>> Provider<Collection<T>>.weakFlatMapCollectionTo(
    crossinline makeCollection: (size: Int) -> C,
    crossinline transform: (T) -> Iterable<R>
): Provider<C> = weakMap { it.flatMapTo(makeCollection(it.size), transform) }

/**
 * Creates and returns a new [Provider] that flattens the [List] of [Lists][List] obtained from [this][Provider].
 */
fun <T> Provider<Iterable<Iterable<T>>>.flattenIterables(): Provider<List<T>> =
    map { it.flatten() }

/**
 * Creates and returns a new [Provider] that flattens the [List] of [Lists][List] obtained from [this][Provider].
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
fun <T> Provider<Iterable<Iterable<T>>>.weakFlattenIterables(): Provider<List<T>> =
    weakMap { it.flatten() }

/**
 * Creates and returns a new [Provider] that merges all [Maps][Map] obtained from [this][Provider] into a single [Map].
 */
fun <K, V> Provider<List<Map<K, V>>>.mergeMaps(): Provider<Map<K, V>> =
    mergeMapsTo(::HashMap)

/**
 * Creates and returns a new [Provider] that merges all [Maps][Map] obtained from [this][Provider] into a single [Map].
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
fun <K, V> Provider<List<Map<K, V>>>.weakMergeMaps(): Provider<Map<K, V>> =
    weakMergeMapsTo(::HashMap)

/**
 * Creates and returns a new [Provider] that merges all [Maps][Map] obtained from [this][Provider] into a single [Map],
 * which is created by the [makeMap] function.
 *
 * [makeMap] should be a pure function.
 */
fun <K, V, M : MutableMap<in K, in V>> Provider<List<Map<K, V>>>.mergeMapsTo(makeMap: (size: Int) -> M): Provider<M> =
    map { maps ->
        val size = maps.sumOf { it.size }
        val map = makeMap(size)
        maps.forEach(map::putAll)
        map
    }

/**
 * Creates and returns a new [Provider] that merges all [Maps][Map] obtained from [this][Provider] into a single [Map],
 * which is created by the [makeMap] function.
 *
 * [makeMap] should be a pure function.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
fun <K, V, M : MutableMap<in K, in V>> Provider<List<Map<K, V>>>.weakMergeMapsTo(makeMap: (size: Int) -> M): Provider<M> =
    weakMap { maps ->
        val size = maps.sumOf { it.size }
        val map = makeMap(size)
        maps.forEach(map::putAll)
        map
    }