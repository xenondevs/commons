@file:JvmName("Providers")
@file:JvmMultifileClass
@file:Suppress("UNCHECKED_CAST")

package xyz.xenondevs.commons.provider

import xyz.xenondevs.commons.provider.impl.MultiUnidirectionalTransformingProvider
import xyz.xenondevs.commons.tuple.Tuple10
import xyz.xenondevs.commons.tuple.Tuple2
import xyz.xenondevs.commons.tuple.Tuple3
import xyz.xenondevs.commons.tuple.Tuple4
import xyz.xenondevs.commons.tuple.Tuple5
import xyz.xenondevs.commons.tuple.Tuple6
import xyz.xenondevs.commons.tuple.Tuple7
import xyz.xenondevs.commons.tuple.Tuple8
import xyz.xenondevs.commons.tuple.Tuple9
import java.lang.ref.WeakReference

/**
 * Creates and returns a new [Provider] that combines all values of [providers].
 */
fun <T> strongCombinedProvider(providers: List<Provider<T>>): Provider<List<T>> =
    MultiUnidirectionalTransformingProvider.of(providers, false) { it }

/**
 * Creates and returns a new [Provider] that combines the values of [a] and [b].
 */
fun <A, B> strongCombinedProvider(
    a: Provider<A>,
    b: Provider<B>
): Provider<Tuple2<A, B>> = MultiUnidirectionalTransformingProvider.of(
    listOf(a, b), false
) { Tuple2(it[0] as A, it[1] as B) }

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b] and [c].
 */
fun <A, B, C> strongCombinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>
): Provider<Tuple3<A, B, C>> = MultiUnidirectionalTransformingProvider.of(
    listOf(a, b, c), false
) { Tuple3(it[0] as A, it[1] as B, it[2] as C) }

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c] and [d].
 */
fun <A, B, C, D> strongCombinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>
): Provider<Tuple4<A, B, C, D>> = MultiUnidirectionalTransformingProvider.of(
    listOf(a, b, c, d), false
) { Tuple4(it[0] as A, it[1] as B, it[2] as C, it[3] as D) }


/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d] and [e].
 */
fun <A, B, C, D, E> strongCombinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    e: Provider<E>
): Provider<Tuple5<A, B, C, D, E>> = MultiUnidirectionalTransformingProvider.of(
    listOf(a, b, c, d, e), false
) { Tuple5(it[0] as A, it[1] as B, it[2] as C, it[3] as D, it[4] as E) }

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d], [e] and [f].
 */
fun <A, B, C, D, E, F> strongCombinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    e: Provider<E>,
    f: Provider<F>
): Provider<Tuple6<A, B, C, D, E, F>> = MultiUnidirectionalTransformingProvider.of(
    listOf(a, b, c, d, e, f), false
) { Tuple6(it[0] as A, it[1] as B, it[2] as C, it[3] as D, it[4] as E, it[5] as F) }

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d], [e], [f] and [g].
 */
fun <A, B, C, D, E, F, G> strongCombinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    e: Provider<E>,
    f: Provider<F>,
    g: Provider<G>
): Provider<Tuple7<A, B, C, D, E, F, G>> = MultiUnidirectionalTransformingProvider.of(
    listOf(a, b, c, d, e, f, g), false
) { Tuple7(it[0] as A, it[1] as B, it[2] as C, it[3] as D, it[4] as E, it[5] as F, it[6] as G) }

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d], [e], [f], [g] and [h].
 */
fun <A, B, C, D, E, F, G, H> strongCombinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    e: Provider<E>,
    f: Provider<F>,
    g: Provider<G>,
    h: Provider<H>
): Provider<Tuple8<A, B, C, D, E, F, G, H>> = MultiUnidirectionalTransformingProvider.of(
    listOf(a, b, c, d, e, f, g, h), false
) { Tuple8(it[0] as A, it[1] as B, it[2] as C, it[3] as D, it[4] as E, it[5] as F, it[6] as G, it[7] as H) }

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d], [e], [f], [g], [h] and [i].
 */
fun <A, B, C, D, E, F, G, H, I> strongCombinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    e: Provider<E>,
    f: Provider<F>,
    g: Provider<G>,
    h: Provider<H>,
    i: Provider<I>
): Provider<Tuple9<A, B, C, D, E, F, G, H, I>> = MultiUnidirectionalTransformingProvider.of(
    listOf(a, b, c, d, e, f, g, h, i), false
) { Tuple9(it[0] as A, it[1] as B, it[2] as C, it[3] as D, it[4] as E, it[5] as F, it[6] as G, it[7] as H, it[8] as I) }

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d], [e], [f], [g], [h], [i] and [j].
 */
fun <A, B, C, D, E, F, G, H, I, J> strongCombinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    e: Provider<E>,
    f: Provider<F>,
    g: Provider<G>,
    h: Provider<H>,
    i: Provider<I>,
    j: Provider<J>
): Provider<Tuple10<A, B, C, D, E, F, G, H, I, J>> = MultiUnidirectionalTransformingProvider.of(
    listOf(a, b, c, d, e, f, g, h, i, j), false
) { Tuple10(it[0] as A, it[1] as B, it[2] as C, it[3] as D, it[4] as E, it[5] as F, it[6] as G, it[7] as H, it[8] as I, it[9] as J) }

/**
 * Creates and returns a new [Provider] that combines all values of [providers].
 *
 * The returned provider will only be stored in a [WeakReference] in the parent providers.
 */
fun <T> combinedProvider(providers: List<Provider<T>>): Provider<List<T>> =
    MultiUnidirectionalTransformingProvider.of(providers, true) { it }

/**
 * Creates and returns a new [Provider] that combines the values of [a] and [b].
 *
 * The returned provider will only be stored in a [WeakReference] in the parent providers.
 */
fun <A, B> combinedProvider(
    a: Provider<A>,
    b: Provider<B>
): Provider<Tuple2<A, B>> = MultiUnidirectionalTransformingProvider.of(
    listOf(a, b), true
) { Tuple2(it[0] as A, it[1] as B) }

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b] and [c].
 *
 * The returned provider will only be stored in a [WeakReference] in the parent providers.
 */
fun <A, B, C> combinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>
): Provider<Tuple3<A, B, C>> = MultiUnidirectionalTransformingProvider.of(
    listOf(a, b, c), true
) { Tuple3(it[0] as A, it[1] as B, it[2] as C) }

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c] and [d].
 *
 * The returned provider will only be stored in a [WeakReference] in the parent providers.
 */
fun <A, B, C, D> combinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>
): Provider<Tuple4<A, B, C, D>> = MultiUnidirectionalTransformingProvider.of(
    listOf(a, b, c, d), true
) { Tuple4(it[0] as A, it[1] as B, it[2] as C, it[3] as D) }

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d] and [e].
 *
 * The returned provider will only be stored in a [WeakReference] in the parent providers.
 */
fun <A, B, C, D, E> combinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    e: Provider<E>
): Provider<Tuple5<A, B, C, D, E>> = MultiUnidirectionalTransformingProvider.of(
    listOf(a, b, c, d, e), true
) { Tuple5(it[0] as A, it[1] as B, it[2] as C, it[3] as D, it[4] as E) }

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d], [e] and [f].
 *
 * The returned provider will only be stored in a [WeakReference] in the parent providers.
 */
fun <A, B, C, D, E, F> combinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    e: Provider<E>,
    f: Provider<F>
): Provider<Tuple6<A, B, C, D, E, F>> = MultiUnidirectionalTransformingProvider.of(
    listOf(a, b, c, d, e, f), true
) { Tuple6(it[0] as A, it[1] as B, it[2] as C, it[3] as D, it[4] as E, it[5] as F) }

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d], [e], [f] and [g].
 *
 * The returned provider will only be stored in a [WeakReference] in the parent providers.
 */
fun <A, B, C, D, E, F, G> combinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    e: Provider<E>,
    f: Provider<F>,
    g: Provider<G>
): Provider<Tuple7<A, B, C, D, E, F, G>> = MultiUnidirectionalTransformingProvider.of(
    listOf(a, b, c, d, e, f, g), true
) { Tuple7(it[0] as A, it[1] as B, it[2] as C, it[3] as D, it[4] as E, it[5] as F, it[6] as G) }

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d], [e], [f], [g] and [h].
 *
 * The returned provider will only be stored in a [WeakReference] in the parent providers.
 */
fun <A, B, C, D, E, F, G, H> combinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    e: Provider<E>,
    f: Provider<F>,
    g: Provider<G>,
    h: Provider<H>
): Provider<Tuple8<A, B, C, D, E, F, G, H>> = MultiUnidirectionalTransformingProvider.of(
    listOf(a, b, c, d, e, f, g, h), true
) { Tuple8(it[0] as A, it[1] as B, it[2] as C, it[3] as D, it[4] as E, it[5] as F, it[6] as G, it[7] as H) }

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d], [e], [f], [g], [h] and [i].
 *
 * The returned provider will only be stored in a [WeakReference] in the parent providers.
 */
fun <A, B, C, D, E, F, G, H, I> combinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    e: Provider<E>,
    f: Provider<F>,
    g: Provider<G>,
    h: Provider<H>,
    i: Provider<I>
): Provider<Tuple9<A, B, C, D, E, F, G, H, I>> = MultiUnidirectionalTransformingProvider.of(
    listOf(a, b, c, d, e, f, g, h, i), true
) { Tuple9(it[0] as A, it[1] as B, it[2] as C, it[3] as D, it[4] as E, it[5] as F, it[6] as G, it[7] as H, it[8] as I) }

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d], [e], [f], [g], [h], [i] and [j].
 *
 * The returned provider will only be stored in a [WeakReference] in the parent providers.
 */
fun <A, B, C, D, E, F, G, H, I, J> combinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    e: Provider<E>,
    f: Provider<F>,
    g: Provider<G>,
    h: Provider<H>,
    i: Provider<I>,
    j: Provider<J>
): Provider<Tuple10<A, B, C, D, E, F, G, H, I, J>> = MultiUnidirectionalTransformingProvider.of(
    listOf(a, b, c, d, e, f, g, h, i, j), true
) { Tuple10(it[0] as A, it[1] as B, it[2] as C, it[3] as D, it[4] as E, it[5] as F, it[6] as G, it[7] as H, it[8] as I, it[9] as J) }