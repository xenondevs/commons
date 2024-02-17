@file:Suppress("NOTHING_TO_INLINE")

package xyz.xenondevs.commons.collections

import java.util.*

inline fun <K> weakHashSet(): MutableSet<K> = Collections.newSetFromMap(WeakHashMap())

inline fun <K> weakHashSetOf(vararg elements: K): MutableSet<K> = weakHashSet<K>().apply { addAll(elements) }

inline fun <E> Set<E>.contentEquals(other: Set<E>) = size == other.size && containsAll(other)