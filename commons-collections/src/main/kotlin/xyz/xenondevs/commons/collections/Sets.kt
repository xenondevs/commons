@file:Suppress("NOTHING_TO_INLINE")

package xyz.xenondevs.commons.collections

inline fun <E> Set<E>.contentEquals(other: Set<E>) = size == other.size && containsAll(other)