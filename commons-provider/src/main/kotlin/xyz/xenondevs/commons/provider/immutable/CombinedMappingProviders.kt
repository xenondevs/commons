package xyz.xenondevs.commons.provider.immutable

import xyz.xenondevs.commons.provider.AbstractProvider
import xyz.xenondevs.commons.provider.Provider

/**
 * Creates and returns a new [Provider] that combines all values of [providers]
 * and immediately maps them to [R] using [mapValue].
 */
fun <T, R> combinedProvider(providers: List<Provider<T>>, mapValue: (List<T>) -> R): Provider<R> =
    CombinedMappingProvider(providers, mapValue)

/**
 * Creates and returns a new [Provider] that combines the values of [a] and [b]
 * and immediately maps them to [R] using [mapValue].
 */
fun <A, B, R> combinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    mapValue: (A, B) -> R
): Provider<R> = CombinedMappingProvider2(a, b, mapValue)

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b] and [c]
 * and immediately maps them to [R] using [mapValue].
 */
fun <A, B, C, R> combinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    mapValue: (A, B, C) -> R
): Provider<R> = CombinedMappingProvider3(a, b, c, mapValue)

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c] and [d]
 * and immediately maps them to [R] using [mapValue].
 */
fun <A, B, C, D, R> combinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    mapValue: (A, B, C, D) -> R
): Provider<R> = CombinedMappingProvider4(a, b, c, d, mapValue)

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d] and [e]
 * and immediately maps them to [R] using [mapValue].
 */
fun <A, B, C, D, E, R> combinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    e: Provider<E>,
    mapValue: (A, B, C, D, E) -> R
): Provider<R> = CombinedMappingProvider5(a, b, c, d, e, mapValue)

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d], [e] and [f]
 * and immediately maps them to [R] using [mapValue].
 */
fun <A, B, C, D, E, F, R> combinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    e: Provider<E>,
    f: Provider<F>,
    mapValue: (A, B, C, D, E, F) -> R
): Provider<R> = CombinedMappingProvider6(a, b, c, d, e, f, mapValue)

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d], [e], [f] and [g]
 * and immediately maps them to [R] using [mapValue].
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
): Provider<R> = CombinedMappingProvider7(a, b, c, d, e, f, g, mapValue)

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d], [e], [f], [g] and [h]
 * and immediately maps them to [R] using [mapValue].
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
): Provider<R> = CombinedMappingProvider8(a, b, c, d, e, f, g, h, mapValue)

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d], [e], [f], [g], [h] and [i]
 * and immediately maps them to [R] using [mapValue].
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
): Provider<R> = CombinedMappingProvider9(a, b, c, d, e, f, g, h, i, mapValue)

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d], [e], [f], [g], [h], [i] and [j]
 * and immediately maps them to [R] using [mapValue].
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
): Provider<R> = CombinedMappingProvider10(a, b, c, d, e, f, g, h, i, j, mapValue)

private class CombinedMappingProvider<T, R>(
    private val providers: List<Provider<T>>,
    private val mapValue: (List<T>) -> R
) : AbstractProvider<R>() {
    
    init {
        providers.forEach { it.addChild(this) }
    }
    
    override fun loadValue(): R = mapValue(providers.map { it.get() })
    
}

private class CombinedMappingProvider2<A, B, R>(
    private val a: Provider<A>,
    private val b: Provider<B>,
    private val mapValue: (A, B) -> R
) : AbstractProvider<R>() {
    
    init {
        a.addChild(this)
        b.addChild(this)
    }
    
    override fun loadValue(): R = mapValue(a.get(), b.get())
    
}

private class CombinedMappingProvider3<A, B, C, R>(
    private val a: Provider<A>,
    private val b: Provider<B>,
    private val c: Provider<C>,
    private val mapValue: (A, B, C) -> R
) : AbstractProvider<R>() {
    
    init {
        a.addChild(this)
        b.addChild(this)
        c.addChild(this)
    }
    
    override fun loadValue(): R = mapValue(a.get(), b.get(), c.get())
    
}

private class CombinedMappingProvider4<A, B, C, D, R>(
    private val a: Provider<A>,
    private val b: Provider<B>,
    private val c: Provider<C>,
    private val d: Provider<D>,
    private val mapValue: (A, B, C, D) -> R
) : AbstractProvider<R>() {
    
    init {
        a.addChild(this)
        b.addChild(this)
        c.addChild(this)
        d.addChild(this)
    }
    
    override fun loadValue(): R = mapValue(a.get(), b.get(), c.get(), d.get())
    
}

private class CombinedMappingProvider5<A, B, C, D, E, R>(
    private val a: Provider<A>,
    private val b: Provider<B>,
    private val c: Provider<C>,
    private val d: Provider<D>,
    private val e: Provider<E>,
    private val mapValue: (A, B, C, D, E) -> R
) : AbstractProvider<R>() {
    
    init {
        a.addChild(this)
        b.addChild(this)
        c.addChild(this)
        d.addChild(this)
        e.addChild(this)
    }
    
    override fun loadValue(): R = mapValue(a.get(), b.get(), c.get(), d.get(), e.get())
    
}

private class CombinedMappingProvider6<A, B, C, D, E, F, R>(
    private val a: Provider<A>,
    private val b: Provider<B>,
    private val c: Provider<C>,
    private val d: Provider<D>,
    private val e: Provider<E>,
    private val f: Provider<F>,
    private val mapValue: (A, B, C, D, E, F) -> R
) : AbstractProvider<R>() {
    
    init {
        a.addChild(this)
        b.addChild(this)
        c.addChild(this)
        d.addChild(this)
        e.addChild(this)
        f.addChild(this)
    }
    
    override fun loadValue(): R = mapValue(a.get(), b.get(), c.get(), d.get(), e.get(), f.get())
    
}

private class CombinedMappingProvider7<A, B, C, D, E, F, G, R>(
    private val a: Provider<A>,
    private val b: Provider<B>,
    private val c: Provider<C>,
    private val d: Provider<D>,
    private val e: Provider<E>,
    private val f: Provider<F>,
    private val g: Provider<G>,
    private val mapValue: (A, B, C, D, E, F, G) -> R
) : AbstractProvider<R>() {
    
    init {
        a.addChild(this)
        b.addChild(this)
        c.addChild(this)
        d.addChild(this)
        e.addChild(this)
        f.addChild(this)
        g.addChild(this)
    }
    
    override fun loadValue(): R = mapValue(a.get(), b.get(), c.get(), d.get(), e.get(), f.get(), g.get())
    
}

private class CombinedMappingProvider8<A, B, C, D, E, F, G, H, R>(
    private val a: Provider<A>,
    private val b: Provider<B>,
    private val c: Provider<C>,
    private val d: Provider<D>,
    private val e: Provider<E>,
    private val f: Provider<F>,
    private val g: Provider<G>,
    private val h: Provider<H>,
    private val mapValue: (A, B, C, D, E, F, G, H) -> R
) : AbstractProvider<R>() {
    
    init {
        a.addChild(this)
        b.addChild(this)
        c.addChild(this)
        d.addChild(this)
        e.addChild(this)
        f.addChild(this)
        g.addChild(this)
        h.addChild(this)
    }
    
    override fun loadValue(): R = mapValue(a.get(), b.get(), c.get(), d.get(), e.get(), f.get(), g.get(), h.get())
    
}

private class CombinedMappingProvider9<A, B, C, D, E, F, G, H, I, R>(
    private val a: Provider<A>,
    private val b: Provider<B>,
    private val c: Provider<C>,
    private val d: Provider<D>,
    private val e: Provider<E>,
    private val f: Provider<F>,
    private val g: Provider<G>,
    private val h: Provider<H>,
    private val i: Provider<I>,
    private val mapValue: (A, B, C, D, E, F, G, H, I) -> R
) : AbstractProvider<R>() {
    
    init {
        a.addChild(this)
        b.addChild(this)
        c.addChild(this)
        d.addChild(this)
        e.addChild(this)
        f.addChild(this)
        g.addChild(this)
        h.addChild(this)
        i.addChild(this)
    }
    
    override fun loadValue(): R = mapValue(a.get(), b.get(), c.get(), d.get(), e.get(), f.get(), g.get(), h.get(), i.get())
    
}

private class CombinedMappingProvider10<A, B, C, D, E, F, G, H, I, J, R>(
    private val a: Provider<A>,
    private val b: Provider<B>,
    private val c: Provider<C>,
    private val d: Provider<D>,
    private val e: Provider<E>,
    private val f: Provider<F>,
    private val g: Provider<G>,
    private val h: Provider<H>,
    private val i: Provider<I>,
    private val j: Provider<J>,
    private val mapValue: (A, B, C, D, E, F, G, H, I, J) -> R
) : AbstractProvider<R>() {
    
    init {
        a.addChild(this)
        b.addChild(this)
        c.addChild(this)
        d.addChild(this)
        e.addChild(this)
        f.addChild(this)
        g.addChild(this)
        h.addChild(this)
        i.addChild(this)
        j.addChild(this)
    }
    
    override fun loadValue(): R = mapValue(a.get(), b.get(), c.get(), d.get(), e.get(), f.get(), g.get(), h.get(), i.get(), j.get())
    
}