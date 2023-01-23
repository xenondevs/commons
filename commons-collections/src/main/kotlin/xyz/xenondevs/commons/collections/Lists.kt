@file:Suppress("NOTHING_TO_INLINE")

package xyz.xenondevs.commons.collections

inline fun <E> List<E>.contentEquals(other: List<E>) = size == other.size && containsAll(other)

inline fun <T> MutableList<T>.rotateRight() {
    val last = removeAt(size - 1)
    add(0, last)
}

inline fun <T> MutableList<T>.rotateLeft() {
    val first = removeAt(0)
    add(first)
}