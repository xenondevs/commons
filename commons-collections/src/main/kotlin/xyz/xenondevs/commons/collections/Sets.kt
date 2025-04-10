package xyz.xenondevs.commons.collections

import java.util.*
import java.util.concurrent.ConcurrentHashMap

fun <K> weakHashSet(): MutableSet<K> = Collections.newSetFromMap(WeakHashMap())

fun <K> weakHashSet(initialCapacity: Int): MutableSet<K> = Collections.newSetFromMap(WeakHashMap(initialCapacity))

fun <K> weakHashSetOf(vararg elements: K): MutableSet<K> = weakHashSet<K>().apply { addAll(elements) }

fun <K> identityHashSet(): MutableSet<K> = Collections.newSetFromMap(IdentityHashMap())

fun <K> identityHashSetOf(vararg elements: K): MutableSet<K> = identityHashSet<K>().apply { addAll(elements) }

fun <K> concurrentHashSet(): MutableSet<K> = Collections.newSetFromMap(ConcurrentHashMap())

fun <K> concurrentHashSetOf(vararg elements: K): MutableSet<K> = concurrentHashSet<K>().apply { addAll(elements) }

inline fun <reified E : Enum<E>> enumSet(): EnumSet<E> = EnumSet.noneOf(E::class.java)

inline fun <reified E : Enum<E>> enumSetOf(vararg elements: E): EnumSet<E> = enumSet<E>().apply { addAll(elements) }

fun <E> Set<E>.contentEquals(other: Set<E>) = size == other.size && containsAll(other)