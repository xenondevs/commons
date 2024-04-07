package xyz.xenondevs.commons.collections

import java.util.*

fun <K> weakHashSet(): MutableSet<K> = Collections.newSetFromMap(WeakHashMap())

fun <K> weakHashSetOf(vararg elements: K): MutableSet<K> = weakHashSet<K>().apply { addAll(elements) }

fun <K> identityHashSet(): MutableSet<K> = Collections.newSetFromMap(IdentityHashMap())

fun <K> identityHashSetOf(vararg elements: K): MutableSet<K> = identityHashSet<K>().apply { addAll(elements) }

fun <E> Set<E>.contentEquals(other: Set<E>) = size == other.size && containsAll(other)