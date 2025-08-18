@file:JvmName("Providers")
@file:JvmMultifileClass
@file:Suppress("UNCHECKED_CAST")

package xyz.xenondevs.commons.provider

import xyz.xenondevs.commons.provider.impl.MultiUnidirectionalTransformingProvider
import java.lang.ref.WeakReference

/**
 * Creates and returns a new [Provider] that combines all values of [providers]
 * and immediately maps them to [R] using [mapValue].
 * 
 * [mapValue] should be a pure function.
 */
fun <T, R> strongCombinedProvider(providers: List<Provider<T>>, mapValue: (List<T>) -> R): Provider<R> =
    MultiUnidirectionalTransformingProvider.of(providers, false, mapValue)

/**
 * Creates and returns a new [Provider] that combines the values of [a] and [b]
 * and immediately maps them to [R] using [mapValue].
 *
 * [mapValue] should be a pure function.
 */
fun <A, B, R> strongCombinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    mapValue: (A, B) -> R
): Provider<R> = MultiUnidirectionalTransformingProvider.of(
    listOf(a, b), false
) { mapValue(it[0] as A, it[1] as B) }

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b] and [c]
 * and immediately maps them to [R] using [mapValue].
 *
 * [mapValue] should be a pure function.
 */
fun <A, B, C, R> strongCombinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    mapValue: (A, B, C) -> R
): Provider<R> = MultiUnidirectionalTransformingProvider.of(
    listOf(a, b, c), false
) { mapValue(it[0] as A, it[1] as B, it[2] as C) }

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c] and [d]
 * and immediately maps them to [R] using [mapValue].
 *
 * [mapValue] should be a pure function.
 */
fun <A, B, C, D, R> strongCombinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    mapValue: (A, B, C, D) -> R
): Provider<R> = MultiUnidirectionalTransformingProvider.of(
    listOf(a, b, c, d), false
) { mapValue(it[0] as A, it[1] as B, it[2] as C, it[3] as D) }

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d] and [e]
 * and immediately maps them to [R] using [mapValue].
 *
 * [mapValue] should be a pure function.
 */
fun <A, B, C, D, E, R> strongCombinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    e: Provider<E>,
    mapValue: (A, B, C, D, E) -> R
): Provider<R> = MultiUnidirectionalTransformingProvider.of(
    listOf(a, b, c, d, e), false
) { mapValue(it[0] as A, it[1] as B, it[2] as C, it[3] as D, it[4] as E) }

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d], [e] and [f]
 * and immediately maps them to [R] using [mapValue].
 *
 * [mapValue] should be a pure function.
 */
fun <A, B, C, D, E, F, R> strongCombinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    e: Provider<E>,
    f: Provider<F>,
    mapValue: (A, B, C, D, E, F) -> R
): Provider<R> = MultiUnidirectionalTransformingProvider.of(
    listOf(a, b, c, d, e, f), false
) { mapValue(it[0] as A, it[1] as B, it[2] as C, it[3] as D, it[4] as E, it[5] as F) }

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d], [e], [f] and [g]
 * and immediately maps them to [R] using [mapValue].
 *
 * [mapValue] should be a pure function.
 */
fun <A, B, C, D, E, F, G, R> strongCombinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    e: Provider<E>,
    f: Provider<F>,
    g: Provider<G>,
    mapValue: (A, B, C, D, E, F, G) -> R
): Provider<R> = MultiUnidirectionalTransformingProvider.of(
    listOf(a, b, c, d, e, f, g), false
) { mapValue(it[0] as A, it[1] as B, it[2] as C, it[3] as D, it[4] as E, it[5] as F, it[6] as G) }

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d], [e], [f], [g] and [h]
 * and immediately maps them to [R] using [mapValue].
 *
 * [mapValue] should be a pure function.
 */
fun <A, B, C, D, E, F, G, H, R> strongCombinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    e: Provider<E>,
    f: Provider<F>,
    g: Provider<G>,
    h: Provider<H>,
    mapValue: (A, B, C, D, E, F, G, H) -> R
): Provider<R> = MultiUnidirectionalTransformingProvider.of(
    listOf(a, b, c, d, e, f, g, h), false
) { mapValue(it[0] as A, it[1] as B, it[2] as C, it[3] as D, it[4] as E, it[5] as F, it[6] as G, it[7] as H) }

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d], [e], [f], [g], [h] and [i]
 * and immediately maps them to [R] using [mapValue].
 *
 * [mapValue] should be a pure function.
 */
fun <A, B, C, D, E, F, G, H, I, R> strongCombinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    e: Provider<E>,
    f: Provider<F>,
    g: Provider<G>,
    h: Provider<H>,
    i: Provider<I>,
    mapValue: (A, B, C, D, E, F, G, H, I) -> R
): Provider<R> = MultiUnidirectionalTransformingProvider.of(
    listOf(a, b, c, d, e, f, g, h, i), false
) { mapValue(it[0] as A, it[1] as B, it[2] as C, it[3] as D, it[4] as E, it[5] as F, it[6] as G, it[7] as H, it[8] as I) }

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d], [e], [f], [g], [h], [i] and [j]
 * and immediately maps them to [R] using [mapValue].
 *
 * [mapValue] should be a pure function.
 */
fun <A, B, C, D, E, F, G, H, I, J, R> strongCombinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    e: Provider<E>,
    f: Provider<F>,
    g: Provider<G>,
    h: Provider<H>,
    i: Provider<I>,
    j: Provider<J>,
    mapValue: (A, B, C, D, E, F, G, H, I, J) -> R
): Provider<R> = MultiUnidirectionalTransformingProvider.of(
    listOf(a, b, c, d, e, f, g, h, i, j), false
) { mapValue(it[0] as A, it[1] as B, it[2] as C, it[3] as D, it[4] as E, it[5] as F, it[6] as G, it[7] as H, it[8] as I, it[9] as J) }

/**
 * Creates and returns a new [Provider] that combines all values of [providers]
 * and immediately maps them to [R] using [mapValue].
 *
 * [mapValue] should be a pure function.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent providers.
 */
fun <T, R> combinedProvider(providers: List<Provider<T>>, mapValue: (List<T>) -> R): Provider<R> =
    MultiUnidirectionalTransformingProvider.of(providers, true, mapValue)

/**
 * Creates and returns a new [Provider] that combines the values of [a] and [b]
 * and immediately maps them to [R] using [mapValue].
 *
 * [mapValue] should be a pure function.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent providers.
 */
fun <A, B, R> combinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    mapValue: (A, B) -> R
): Provider<R> = MultiUnidirectionalTransformingProvider.of(
    listOf(a, b), true
) { mapValue(it[0] as A, it[1] as B) }

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b] and [c]
 * and immediately maps them to [R] using [mapValue].
 *
 * [mapValue] should be a pure function.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent providers.
 */
fun <A, B, C, R> combinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    mapValue: (A, B, C) -> R
): Provider<R> = MultiUnidirectionalTransformingProvider.of(
    listOf(a, b, c), true
) { mapValue(it[0] as A, it[1] as B, it[2] as C) }

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c] and [d]
 * and immediately maps them to [R] using [mapValue].
 *
 * [mapValue] should be a pure function.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent providers.
 */
fun <A, B, C, D, R> combinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    mapValue: (A, B, C, D) -> R
): Provider<R> = MultiUnidirectionalTransformingProvider.of(
    listOf(a, b, c, d), true
) { mapValue(it[0] as A, it[1] as B, it[2] as C, it[3] as D) }

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d] and [e]
 * and immediately maps them to [R] using [mapValue].
 *
 * [mapValue] should be a pure function.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent providers.
 */
fun <A, B, C, D, E, R> combinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    e: Provider<E>,
    mapValue: (A, B, C, D, E) -> R
): Provider<R> = MultiUnidirectionalTransformingProvider.of(
    listOf(a, b, c, d, e), true
) { mapValue(it[0] as A, it[1] as B, it[2] as C, it[3] as D, it[4] as E) }

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d], [e] and [f]
 * and immediately maps them to [R] using [mapValue].
 *
 * [mapValue] should be a pure function.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent providers.
 */
fun <A, B, C, D, E, F, R> combinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    e: Provider<E>,
    f: Provider<F>,
    mapValue: (A, B, C, D, E, F) -> R
): Provider<R> = MultiUnidirectionalTransformingProvider.of(
    listOf(a, b, c, d, e, f), true
) { mapValue(it[0] as A, it[1] as B, it[2] as C, it[3] as D, it[4] as E, it[5] as F) }

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d], [e], [f] and [g]
 * and immediately maps them to [R] using [mapValue].
 *
 * [mapValue] should be a pure function.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent providers.
 */
fun <A, B, C, D, E, F, G, R> combinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    e: Provider<E>,
    f: Provider<F>,
    g: Provider<G>,
    mapValue: (A, B, C, D, E, F, G) -> R
): Provider<R> = MultiUnidirectionalTransformingProvider.of(
    listOf(a, b, c, d, e, f, g), true
) { mapValue(it[0] as A, it[1] as B, it[2] as C, it[3] as D, it[4] as E, it[5] as F, it[6] as G) }

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d], [e], [f], [g] and [h]
 * and immediately maps them to [R] using [mapValue].
 *
 * [mapValue] should be a pure function.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent providers.
 */
fun <A, B, C, D, E, F, G, H, R> combinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    e: Provider<E>,
    f: Provider<F>,
    g: Provider<G>,
    h: Provider<H>,
    mapValue: (A, B, C, D, E, F, G, H) -> R
): Provider<R> = MultiUnidirectionalTransformingProvider.of(
    listOf(a, b, c, d, e, f, g, h), true
) { mapValue(it[0] as A, it[1] as B, it[2] as C, it[3] as D, it[4] as E, it[5] as F, it[6] as G, it[7] as H) }

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d], [e], [f], [g], [h] and [i]
 * and immediately maps them to [R] using [mapValue].
 *
 * [mapValue] should be a pure function.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent providers.
 */
fun <A, B, C, D, E, F, G, H, I, R> combinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    e: Provider<E>,
    f: Provider<F>,
    g: Provider<G>,
    h: Provider<H>,
    i: Provider<I>,
    mapValue: (A, B, C, D, E, F, G, H, I) -> R
): Provider<R> = MultiUnidirectionalTransformingProvider.of(
    listOf(a, b, c, d, e, f, g, h, i), true
) { mapValue(it[0] as A, it[1] as B, it[2] as C, it[3] as D, it[4] as E, it[5] as F, it[6] as G, it[7] as H, it[8] as I) }

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d], [e], [f], [g], [h], [i] and [j]
 * and immediately maps them to [R] using [mapValue].
 *
 * [mapValue] should be a pure function.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent providers.
 */
fun <A, B, C, D, E, F, G, H, I, J, R> combinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    e: Provider<E>,
    f: Provider<F>,
    g: Provider<G>,
    h: Provider<H>,
    i: Provider<I>,
    j: Provider<J>,
    mapValue: (A, B, C, D, E, F, G, H, I, J) -> R
): Provider<R> = MultiUnidirectionalTransformingProvider.of(
    listOf(a, b, c, d, e, f, g, h, i, j), true
) { mapValue(it[0] as A, it[1] as B, it[2] as C, it[3] as D, it[4] as E, it[5] as F, it[6] as G, it[7] as H, it[8] as I, it[9] as J) }