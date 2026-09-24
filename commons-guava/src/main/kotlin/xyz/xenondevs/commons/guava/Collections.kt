package xyz.xenondevs.commons.guava

import com.google.common.collect.MapMaker
import java.util.*
import java.util.concurrent.ConcurrentMap

private fun capacityFor(numMappings: Int): Int {
    require(numMappings >= 0)
    return ((numMappings.toLong() * 4 + 2) / 3).coerceAtMost(Int.MAX_VALUE.toLong()).toInt()
}

fun <K : Any, V : Any> concurrentWeakIdentityMap(): ConcurrentMap<K, V> =
    MapMaker().weakKeys().makeMap<K, V>()

fun <K : Any, V : Any> concurrentWeakIdentityMap(numMappings: Int): ConcurrentMap<K, V> =
    MapMaker().weakKeys().initialCapacity(capacityFor(numMappings)).makeMap<K, V>()

fun <K : Any, V : Any> concurrentWeakIdentityMapOf(vararg entries: Pair<K, V>): ConcurrentMap<K, V> =
    concurrentWeakIdentityMap<K, V>(entries.size).apply { entries.forEach { put(it.first, it.second) } }

fun <T : Any> concurrentWeakIdentitySet(): MutableSet<T> =
    Collections.newSetFromMap(concurrentWeakIdentityMap())

fun <T : Any> concurrentWeakIdentitySet(numMappings: Int): MutableSet<T> =
    Collections.newSetFromMap(concurrentWeakIdentityMap<T, Boolean>(numMappings))

fun <T : Any> concurrentWeakIdentitySetOf(vararg elements: T): MutableSet<T> =
    concurrentWeakIdentitySet<T>(elements.size).apply { addAll(elements) }
