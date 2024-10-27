package xyz.xenondevs.commons.provider

import java.lang.ref.WeakReference

/**
 * Creates and returns a new [Provider] that throws an [IllegalArgumentException]
 * with a message generated by [message] if [condition] fails.
 *
 * [condition] and [message] should be pure functions.
 */
inline fun <T> Provider<T>.require(
    crossinline condition: (T) -> Boolean,
    crossinline message: (T) -> String
): Provider<T> = map { require(condition(it)) { message(it) }; it }

/**
 * Creates and returns a new [Provider] that throws an [IllegalArgumentException]
 * with a message generated by [message] if [condition] fails.
 *
 * [condition] and [message] should be pure functions.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
inline fun <T> Provider<T>.weakRequire(
    crossinline condition: (T) -> Boolean,
    crossinline message: (T) -> String
): Provider<T> = weakMap { require(condition(it)) { message(it) }; it }

/**
 * Creates and returns a new [Provider] that throws an [IllegalArgumentException]
 * with [message] if the value is `null`.
 */
fun <T : Any> Provider<T?>.requireNotNull(message: String = "Required value was null."): Provider<T> =
    requireNotNull { message }

/**
 * Creates and returns a new [Provider] that throws an [IllegalArgumentException]
 * with [message] if the value is `null`.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
fun <T : Any> Provider<T?>.weakRequireNotNull(message: String = "Required value was null."): Provider<T> =
    weakRequireNotNull { message }

/**
 * Creates and returns a new [Provider] that throws an [IllegalArgumentException]
 * with a message generated by [message] if the value is `null`.
 *
 * [message] should be a pure function.
 */
inline fun <T : Any> Provider<T?>.requireNotNull(crossinline message: () -> String): Provider<T> =
    map { requireNotNull(it, message); it }

/**
 * Creates and returns a new [Provider] that throws an [IllegalArgumentException]
 * with a message generated by [message] if the value is `null`.
 *
 * [message] should be a pure function.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
inline fun <T : Any> Provider<T?>.weakRequireNotNull(crossinline message: () -> String): Provider<T> =
    weakMap { requireNotNull(it, message); it }