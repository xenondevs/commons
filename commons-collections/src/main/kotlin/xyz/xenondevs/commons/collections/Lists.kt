package xyz.xenondevs.commons.collections

fun <E> List<E>.contentEquals(other: List<E>) = size == other.size && containsAll(other)

fun <T> MutableList<T>.rotateRight() {
    val last = removeAt(size - 1)
    add(0, last)
}

fun <T> MutableList<T>.rotateLeft() {
    val first = removeAt(0)
    add(first)
}

fun <T> List<T>.after(element: T, step: Int = 1): T =
    get((indexOf(element) + step).mod(size))

fun <T> List<T>.before(element: T, step: Int = 1): T =
    get((indexOf(element) - step).mod(size))