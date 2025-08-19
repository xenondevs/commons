package xyz.xenondevs.commons.provider.util

import java.util.*

/**
 * Creates a new empty weak hash set with the specified [initialCapacity].
 */
fun <K> weakHashSet(initialCapacity: Int): MutableSet<K> =
    Collections.newSetFromMap(WeakHashMap(initialCapacity))

/**
 * Creates a copy of [this][Collection] using [createCollection] with an additional [element] added to it.
 * Returns itself if the [element] is already contained in the collection.
 */
internal fun <E> Collection<E>.with(element: E, createCollection: (Int) -> MutableCollection<E>): Collection<E> {
    if (element in this)
        return this
    
    val newCollection = createCollection(size + 1)
    newCollection += this
    newCollection += element
    return newCollection
}

/**
 * Creates a copy of [this][Collection] using [createCollection] with the [element] removed from it.
 * Returns itself if the [element] is not contained in the collection.
 */
internal fun <E> Collection<E>.without(element: E, createCollection: (Int) -> MutableCollection<E>): Collection<E> {
    if (element !in this)
        return this
    
    val newCollection = createCollection(size - 1)
    for (e in this) {
        if (e != element)
            newCollection += e
    }
    return newCollection
}

/**
 * Creates a copy of [this][Map] using [createMap] with an additional [value] added to the set at the given [key].
 * Returns itself if the [value] is already present in the set at the [key].
 */
internal fun <K, V> Map<K, Set<V>>.with(
    key: K,
    value: V,
    createMap: (Int) -> MutableMap<K, Set<V>>,
    createSet: (Int) -> MutableSet<V>
): Map<K, Set<V>> {
    val currentSet = this[key]
    
    // create new set and map if necessary
    val newMap: MutableMap<K, Set<V>>
    val newSet: MutableSet<V>
    if (currentSet != null) {
        if (value in currentSet)
            return this // no change required
        
        newMap = createMap(size)
        newSet = createSet(currentSet.size + 1)
        newSet += currentSet
    } else {
        newMap = createMap(size + 1)
        newSet = createSet(1)
    }
    newSet += value
    
    // populate new map
    newMap[key] = newSet
    for ((k, v) in this) {
        if (k == key)
            continue
        newMap[k] = v
    }
    
    return newMap
}

/**
 * Creates a copy of [this][Map] using [createMap] with the [value] removed from the set at the given [key].
 * If removing the [value] results in an empty list, the [key] is removed from the map.
 * Returns itself if [key] or [value] are not present in the map or set, respectively.
 */
internal fun <K, V> Map<K, Set<V>>.without(
    key: K,
    value: V,
    createMap: (Int) -> MutableMap<K, Set<V>>,
    createSet: (Int) -> MutableSet<V>
): Map<K, Set<V>> {
    val currentSet = this[key]
    if (currentSet.isNullOrEmpty())
        return this
    if (value !in currentSet)
        return this
    
    val newMap: MutableMap<K, Set<V>>
    if (currentSet.size == 1) {
        // set can be completely removed
        newMap = createMap(size - 1)
    } else {
        val newSet = createSet(currentSet.size - 1)
        for (v in currentSet) {
            if (v == value)
                continue
            newSet += v
        }
        
        newMap = createMap(size)
        newMap[key] = newSet
    }
    
    // add remaining unchanged sets
    for ((k, v) in this) {
        if (k == key)
            continue
        newMap[k] = v
    }
    
    return newMap
}