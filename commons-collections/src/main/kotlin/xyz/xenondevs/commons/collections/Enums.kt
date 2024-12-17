package xyz.xenondevs.commons.collections

import kotlin.enums.enumEntries

/**
 * Returns the enum entry [stepSize] after this enum entry, in declaration order.
 * Cycles back to the first entry if the end is reached.
 */
inline fun <reified E : Enum<E>> E.next(stepSize: Int = 1): E {
    val entries = enumEntries<E>()
    return entries[(ordinal + stepSize).mod(entries.size)]
}

/**
 * Returns the enum entry [stepSize] after this enum entry, in declaration order.
 * Returns `null` if the end is reached.
 */
inline fun <reified E : Enum<E>> E.nextOrNull(stepSize: Int = 1): E? {
    return enumEntries<E>().getOrNull(ordinal + stepSize)
}

/**
 * Returns the enum entry [stepSize] before this enum entry, in declaration order.
 * Cycles back to the last entry if the beginning is reached.
 */
inline fun <reified E : Enum<E>> E.previous(stepSize: Int = 1): E {
    val entries = enumEntries<E>()
    return entries[(ordinal - stepSize).mod(entries.size)]
}

/**
 * Returns the enum entry [stepSize] before this enum entry, in declaration order.
 * Returns `null` if the beginning is reached.
 */
inline fun <reified E : Enum<E>> E.previousOrNull(stepSize: Int = 1): E? {
    return enumEntries<E>().getOrNull(ordinal - stepSize)
}