@file:JvmName("Providers")
@file:JvmMultifileClass

package xyz.xenondevs.commons.provider

import java.lang.ref.WeakReference

/**
 * Creates and returns a new [Provider] that returns a fallback [value] if the value of [this][Provider] is null.
 */
fun <T> Provider<T?>.strongOrElse(value: T): Provider<T> =
    strongMap { it ?: value }

/**
 * Creates and returns a new [Provider] that returns a fallback [value] if the value of [this][Provider] is null.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
fun <T> Provider<T?>.orElse(value: T): Provider<T> =
    map { it ?: value }

/**
 * Creates and returns a new [Provider] that returns a fallback value obtained through [provider] if the value of [this][Provider] is null.
 */
fun <T> Provider<T?>.strongOrElse(provider: Provider<T>): Provider<T> =
    strongImmediateFlatMap { it?.let(::provider) ?: provider }

/**
 * Creates and returns a new [Provider] that returns a fallback value obtained through [provider] if the value of [this][Provider] is null.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent providers ([this][MutableProvider] and [provider]).
 */
fun <T> Provider<T?>.orElse(provider: Provider<T>): Provider<T> =
    combinedProvider(this, provider) { a, b -> a ?: b }

/**
 * If [provider] is null, returns [this][Provider]. If [provider] is not null, creates and returns a new [Provider] that returns a fallback value obtained
 * through [provider] if the value of [this][Provider] is null.
 */
@JvmName("strongOrElseNullable")
fun <T> Provider<T?>.strongOrElse(provider: Provider<T>?): Provider<T?> =
    if (provider != null) strongOrElse(provider) else this

/**
 * If [provider] is null, returns [this][Provider]. If [provider] is not null, creates and returns a new [Provider] that returns a fallback value obtained
 * through [provider] if the value of [this][Provider] is null.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent providers ([this][MutableProvider] and [provider]).
 */
@JvmName("orElseNullable")
fun <T> Provider<T?>.orElse(provider: Provider<T>?): Provider<T?> =
    if (provider != null) orElse(provider) else this

/**
 * Creates and returns a new [Provider] that returns a fallback value obtained through [provider] if the value of [this][Provider] is null.
 *
 * [lazyValue] should be a pure function.
 */
fun <T> Provider<T?>.strongOrElseLazily(lazyValue: () -> T): Provider<T> {
    val lazy = lazy(lazyValue)
    return map { it ?: lazy.value }
}

/**
 * Creates and returns a new [Provider] that returns a fallback value obtained through [provider] if the value of [this][Provider] is null.
 *
 * [lazyValue] should be a pure function.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
fun <T> Provider<T?>.orElseLazily(lazyValue: () -> T): Provider<T> {
    val lazy = lazy(lazyValue)
    return map { it ?: lazy.value }
}

/**
 * Creates a new [Provider] that returns a fallback value which is re-created through the [newValue] lambda every time the value of [this][Provider] is set to null.
 *
 * [newValue] should be a pure function.
 */
fun <T> Provider<T?>.strongOrElseNew(newValue: () -> T): Provider<T> =
    strongMap { it ?: newValue() }

/**
 * Creates a new [Provider] that returns a fallback value which is re-created through the [newValue] lambda every time the value of [this][Provider] is set to null.
 *
 * [newValue] should be a pure function.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
fun <T> Provider<T?>.orElseNew(newValue: () -> T): Provider<T> =
    map { it ?: newValue() }

/**
 * Creates a new [MutableProvider] that returns a fallback [value] if the value of [this][MutableProvider] is null.
 * Conversely, if the returned provider's value is set a value equal to [value], the value of [this][MutableProvider] will be set to null.
 */
fun <T> MutableProvider<T?>.strongOrElse(value: T): MutableProvider<T> =
    strongMap({ it ?: value }, { it?.takeUnless { it == value } })

/**
 * Creates a new [MutableProvider] that returns a fallback [value] if the value of [this][MutableProvider] is null.
 * Conversely, if the returned provider's value is set a value equal to [value], the value of [this][MutableProvider] will be set to null.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
fun <T> MutableProvider<T?>.orElse(value: T): MutableProvider<T> =
    map({ it ?: value }, { it?.takeUnless { it == value } })

/**
 * Creates a new [MutableProvider] that returns a fallback value obtained through the [lazyValue] lambda if the value of [this][MutableProvider] is null.
 * Conversely, if the returned provider's value is set to a value equal to the one obtained through [lazyValue], the value of [this][MutableProvider] will be set to null.
 *
 * [lazyValue] should be a pure function.
 */
fun <T : Any> MutableProvider<T?>.strongOrElseLazily(lazyValue: () -> T): MutableProvider<T> {
    val lazy = lazy(lazyValue)
    return strongMap({ it ?: lazy.value }, { it.takeUnless { it == lazy.value } })
}

/**
 * Creates a new [MutableProvider] that returns a fallback value obtained through the [lazyValue] lambda if the value of [this][MutableProvider] is null.
 * Conversely, if the returned provider's value is set to a value equal to the one obtained through [lazyValue], the value of [this][MutableProvider] will be set to null.
 *
 * [lazyValue] should be a pure function.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
fun <T : Any> MutableProvider<T?>.orElseLazily(lazyValue: () -> T): MutableProvider<T> {
    val lazy = lazy(lazyValue)
    return map({ it ?: lazy.value }, { it.takeUnless { it == lazy.value } })
}

/**
 * Creates a new [MutableProvider] that returns a fallback value which is re-created through the [newValue] lambda every time the value of [this][MutableProvider] is set to null.
 * Conversely, if the returned provider's value is set to a value equal to one returned by [newValue], the value of [this][MutableProvider] will be set to null.
 *
 * For mutable data types, it is required that the [newValue] lambda returns a new instance every time it is called, and that all of those instances are [equal][Any.equals] to each other.
 *
 * [newValue] should be a pure function.
 */
fun <T> MutableProvider<T?>.strongOrElseNew(newValue: () -> T): MutableProvider<T> =
    strongMap({ it ?: newValue() }, { it?.takeUnless { it == newValue() } })

/**
 * Creates a new [MutableProvider] that returns a fallback value which is re-created through the [newValue] lambda every time the value of [this][MutableProvider] is set to null.
 * Conversely, if the returned provider's value is set to a value equal to one returned by [newValue], the value of [this][MutableProvider] will be set to null.
 *
 * For mutable data types, it is required that the [newValue] lambda returns a new instance every time it is called, and that all of those instances are [equal][Any.equals] to each other.
 *
 * [newValue] should be a pure function.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
fun <T> MutableProvider<T?>.orElseNew(newValue: () -> T): MutableProvider<T> =
    map({ it ?: newValue() }, { it?.takeUnless { it == newValue() } })
