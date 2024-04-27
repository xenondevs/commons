package xyz.xenondevs.commons.collections

import java.util.*
import kotlin.sequences.flatMap

fun <T> Sequence<T>.sumOf(selector: (T) -> Float): Float {
    var sum = 0f
    for (element in this) {
        sum += selector(element)
    }
    return sum
}

fun <T, R> Sequence<T>.flatMap(transform: (T) -> Array<R>): Sequence<R> =
    flatMap { transform(it).asSequence() }

inline fun <reified E : Enum<E>> Sequence<E>.toEnumSet(): EnumSet<E> =
    toCollection(enumSet())
