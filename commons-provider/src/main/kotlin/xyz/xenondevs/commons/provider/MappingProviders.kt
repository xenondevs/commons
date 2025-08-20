@file:JvmName("Providers")
@file:JvmMultifileClass

package xyz.xenondevs.commons.provider

import java.lang.ref.WeakReference

/**
 * Creates and returns a new [Provider] that maps to the provider that is the value of [this][Provider].
 *
 * This function is equivalent to `provider.immediateFlatMap { it }`.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent providers
 * ([this][Provider] and the value of this).
 */
fun <R> Provider<Provider<R>>.immediateFlatten(): Provider<R> = immediateFlatMap { it }

/**
 * Creates and returns a new [Provider] that maps to the provider that is the value of [this][Provider].
 *
 * This function is equivalent to `provider.strongImmediateFlatMap { it }`.
 */
fun <R> Provider<Provider<R>>.strongImmediateFlatten(): Provider<R> = strongImmediateFlatMap { it }

/**
 * Creates and returns a new [Provider] that maps to the provider that is the value of [this][Provider].
 *
 * This function is equivalent to `provider.immediateFlatMapMutable { it }`.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent providers
 * ([this][Provider] and the value of this).
 */
fun <R> Provider<MutableProvider<R>>.immediateFlatten(): MutableProvider<R> = immediateFlatMapMutable { it }

/**
 * Creates and returns a new [Provider] that maps to the provider that is the value of [this][Provider].
 *
 * This function is equivalent to `provider.strongImmediateFlatMapMutable { it }`.
 */
fun <R> Provider<MutableProvider<R>>.strongImmediateFlatten(): MutableProvider<R> = strongImmediateFlatMapMutable { it }

/**
 * Creates and returns a new [Provider] that lazily maps to the provider that is the value of [this][Provider].
 *
 * This function is equivalent to `provider.flatMap { it }`.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent providers
 * ([this][Provider] and the value of this).
 */
fun <R> Provider<Provider<R>>.flatten(): Provider<R> = flatMap { it }

/**
 * Creates and returns a new [Provider] that lazily maps to the provider that is the value of [this][Provider].
 *
 * This function is equivalent to `provider.strongFlatMap { it }`.
 */
fun <R> Provider<Provider<R>>.strongFlatten(): Provider<R> = strongFlatMap { it }

/**
 * Creates and returns a new [Provider] that lazily maps to the provider that is the value of [this][Provider].
 *
 * This function is equivalent to `provider.flatMapMutable { it }`.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent providers
 * ([this][Provider] and the value of this).
 */
fun <R> Provider<MutableProvider<R>>.flatten(): MutableProvider<R> = flatMapMutable { it }

/**
 * Creates and returns a new [Provider] that lazily maps to the provider that is the value of [this][Provider].
 *
 * This function is equivalent to `provider.strongFlatMapMutable { it }`.
 */
fun <R> Provider<MutableProvider<R>>.strongFlatten(): MutableProvider<R> = strongFlatMapMutable { it }

/**
 * Creates and returns a new [Provider] that maps non-null values of [this][Provider]
 * using the [transform] function.
 * Null values will be passed through without transformation.
 *
 * [transform] should be a pure function.
 */
inline fun <T : Any, R> Provider<T?>.strongMapNonNull(crossinline transform: (T) -> R): Provider<R?> =
    strongMap { it?.let(transform) }

/**
 * Creates and returns a new [Provider] that maps non-null values of [this][Provider]
 * using the [transform] function.
 * Null values will be passed through without transformation.
 *
 * [transform] should be a pure function.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
inline fun <T : Any, R> Provider<T?>.mapNonNull(crossinline transform: (T) -> R): Provider<R?> =
    map { it?.let(transform) }

/**
 * Creates and returns a new [MutableProvider] that maps non-null values of [this][MutableProvider]
 * bi-directionally using the provided [transform] and [untransform] functions.
 * Null values will be passed through without transformation.
 *
 * [transform] and [untransform] should be pure functions.
 */
inline fun <T : Any, R : Any> MutableProvider<T?>.strongMapNonNull(
    crossinline transform: (T) -> R?,
    crossinline untransform: (R) -> T?
): MutableProvider<R?> = strongMap({ it?.let(transform) }, { it?.let(untransform) })

/**
 * Creates and returns a new [MutableProvider] that maps non-null values of [this][MutableProvider]
 * bi-directionally using the provided [transform] and [untransform] functions.
 * Null values will be passed through without transformation.
 *
 * [transform] and [untransform] should be pure functions.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
inline fun <T : Any, R : Any> MutableProvider<T?>.mapNonNull(
    crossinline transform: (T) -> R?,
    crossinline untransform: (R) -> T?
): MutableProvider<R?> = map({ it?.let(transform) }, { it?.let(untransform) })