@file:OptIn(UnstableProviderApi::class)

package xyz.xenondevs.commons.provider

import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * Creates and returns a new [Provider] that combines all values of [providers]
 * and immediately maps them to [R] using [mapValue].
 */
@Suppress("UNCHECKED_CAST")
fun <T, R> combinedProvider(providers: List<Provider<T>>, mapValue: (List<T>) -> R): Provider<R> =
    CombinedMappingProvider(providers as List<AbstractProvider<T>>, mapValue)

/**
 * Creates and returns a new [Provider] that combines the values of [a] and [b]
 * and immediately maps them to [R] using [mapValue].
 */
fun <A, B, R> combinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    mapValue: (A, B) -> R
): Provider<R> = CombinedMappingProvider2(
    a as AbstractProvider<A>,
    b as AbstractProvider<B>,
    mapValue
)

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b] and [c]
 * and immediately maps them to [R] using [mapValue].
 */
fun <A, B, C, R> combinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    mapValue: (A, B, C) -> R
): Provider<R> = CombinedMappingProvider3(
    a as AbstractProvider<A>,
    b as AbstractProvider<B>,
    c as AbstractProvider<C>,
    mapValue
)

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
): Provider<R> = CombinedMappingProvider4(
    a as AbstractProvider<A>,
    b as AbstractProvider<B>,
    c as AbstractProvider<C>,
    d as AbstractProvider<D>,
    mapValue
)

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
): Provider<R> = CombinedMappingProvider5(
    a as AbstractProvider<A>,
    b as AbstractProvider<B>,
    c as AbstractProvider<C>,
    d as AbstractProvider<D>,
    e as AbstractProvider<E>,
    mapValue
)

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
): Provider<R> = CombinedMappingProvider6(
    a as AbstractProvider<A>,
    b as AbstractProvider<B>,
    c as AbstractProvider<C>,
    d as AbstractProvider<D>,
    e as AbstractProvider<E>,
    f as AbstractProvider<F>,
    mapValue
)

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
): Provider<R> = CombinedMappingProvider7(
    a as AbstractProvider<A>,
    b as AbstractProvider<B>,
    c as AbstractProvider<C>,
    d as AbstractProvider<D>,
    e as AbstractProvider<E>,
    f as AbstractProvider<F>,
    g as AbstractProvider<G>,
    mapValue
)

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
): Provider<R> = CombinedMappingProvider8(
    a as AbstractProvider<A>,
    b as AbstractProvider<B>,
    c as AbstractProvider<C>,
    d as AbstractProvider<D>,
    e as AbstractProvider<E>,
    f as AbstractProvider<F>,
    g as AbstractProvider<G>,
    h as AbstractProvider<H>,
    mapValue
)

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
): Provider<R> = CombinedMappingProvider9(
    a as AbstractProvider<A>,
    b as AbstractProvider<B>,
    c as AbstractProvider<C>,
    d as AbstractProvider<D>,
    e as AbstractProvider<E>,
    f as AbstractProvider<F>,
    g as AbstractProvider<G>,
    h as AbstractProvider<H>,
    i as AbstractProvider<I>,
    mapValue
)

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
): Provider<R> = CombinedMappingProvider10(
    a as AbstractProvider<A>,
    b as AbstractProvider<B>,
    c as AbstractProvider<C>,
    d as AbstractProvider<D>,
    e as AbstractProvider<E>,
    f as AbstractProvider<F>,
    g as AbstractProvider<G>,
    h as AbstractProvider<H>,
    i as AbstractProvider<I>,
    j as AbstractProvider<J>,
    mapValue
)

private class CombinedMappingProvider<T, R>(
    private val providers: List<AbstractProvider<T>>,
    private val mapValue: (List<T>) -> R
) : AbstractProvider<R>(ReentrantLock()) {
    
    init {
        for (provider in providers) {
            provider.changeLock(lock)
        }
        
        lock.withLock {
            for (provider in providers) {
                addInactiveParent(provider)
                provider.addChild(this)
            }
        }
    }
    
    override fun pull(): R = mapValue(providers.map { it.get() })
    
}

private class CombinedMappingProvider2<A, B, R>(
    private val a: AbstractProvider<A>,
    private val b: AbstractProvider<B>,
    private val mapValue: (A, B) -> R
) : AbstractProvider<R>(ReentrantLock()) {
    
    init {
        changeLocks(lock, a, b)
        lock.withLock {
            addInactiveParents(a, b)
            addAsChildTo(a, b)
        }
    }
    
    override fun pull(): R = mapValue(a.get(), b.get())
    
}

private class CombinedMappingProvider3<A, B, C, R>(
    private val a: AbstractProvider<A>,
    private val b: AbstractProvider<B>,
    private val c: AbstractProvider<C>,
    private val mapValue: (A, B, C) -> R
) : AbstractProvider<R>(ReentrantLock()) {
    
    init {
        changeLocks(lock, a, b, c)
        lock.withLock {
            addInactiveParents(a, b, c)
            addAsChildTo(a, b, c)
        }
    }
    
    override fun pull(): R = mapValue(a.get(), b.get(), c.get())
    
}

private class CombinedMappingProvider4<A, B, C, D, R>(
    private val a: AbstractProvider<A>,
    private val b: AbstractProvider<B>,
    private val c: AbstractProvider<C>,
    private val d: AbstractProvider<D>,
    private val mapValue: (A, B, C, D) -> R
) : AbstractProvider<R>(ReentrantLock()) {
    
    init {
        changeLocks(lock, a, b, c, d)
        lock.withLock {
            addInactiveParents(a, b, c, d)
            addAsChildTo(a, b, c, d)
        }
    }
    
    override fun pull(): R = mapValue(a.get(), b.get(), c.get(), d.get())
    
}

private class CombinedMappingProvider5<A, B, C, D, E, R>(
    private val a: AbstractProvider<A>,
    private val b: AbstractProvider<B>,
    private val c: AbstractProvider<C>,
    private val d: AbstractProvider<D>,
    private val e: AbstractProvider<E>,
    private val mapValue: (A, B, C, D, E) -> R
) : AbstractProvider<R>(ReentrantLock()) {
    
    init {
        changeLocks(lock, a, b, c, d, e)
        lock.withLock {
            addInactiveParents(a, b, c, d, e)
            addAsChildTo(a, b, c, d, e)
        }
    }
    
    override fun pull(): R = mapValue(a.get(), b.get(), c.get(), d.get(), e.get())
    
}

private class CombinedMappingProvider6<A, B, C, D, E, F, R>(
    private val a: AbstractProvider<A>,
    private val b: AbstractProvider<B>,
    private val c: AbstractProvider<C>,
    private val d: AbstractProvider<D>,
    private val e: AbstractProvider<E>,
    private val f: AbstractProvider<F>,
    private val mapValue: (A, B, C, D, E, F) -> R
) : AbstractProvider<R>(ReentrantLock()) {
    
    init {
        changeLocks(lock, a, b, c, d, e, f)
        lock.withLock {
            addInactiveParents(a, b, c, d, e, f)
            addAsChildTo(a, b, c, d, e, f)
        }
    }
    
    override fun pull(): R = mapValue(a.get(), b.get(), c.get(), d.get(), e.get(), f.get())
    
}

private class CombinedMappingProvider7<A, B, C, D, E, F, G, R>(
    private val a: AbstractProvider<A>,
    private val b: AbstractProvider<B>,
    private val c: AbstractProvider<C>,
    private val d: AbstractProvider<D>,
    private val e: AbstractProvider<E>,
    private val f: AbstractProvider<F>,
    private val g: AbstractProvider<G>,
    private val mapValue: (A, B, C, D, E, F, G) -> R
) : AbstractProvider<R>(ReentrantLock()) {
    
    init {
        changeLocks(lock, a, b, c, d, e, f, g)
        lock.withLock {
            addInactiveParents(a, b, c, d, e, f, g)
            addAsChildTo(a, b, c, d, e, f, g)
        }
    }
    
    override fun pull(): R = mapValue(a.get(), b.get(), c.get(), d.get(), e.get(), f.get(), g.get())
    
}

private class CombinedMappingProvider8<A, B, C, D, E, F, G, H, R>(
    private val a: AbstractProvider<A>,
    private val b: AbstractProvider<B>,
    private val c: AbstractProvider<C>,
    private val d: AbstractProvider<D>,
    private val e: AbstractProvider<E>,
    private val f: AbstractProvider<F>,
    private val g: AbstractProvider<G>,
    private val h: AbstractProvider<H>,
    private val mapValue: (A, B, C, D, E, F, G, H) -> R
) : AbstractProvider<R>(ReentrantLock()) {
    
    init {
        changeLocks(lock, a, b, c, d, e, f, g, h)
        lock.withLock {
            addInactiveParents(a, b, c, d, e, f, g, h)
            addAsChildTo(a, b, c, d, e, f, g, h)
        }
    }
    
    override fun pull(): R = mapValue(a.get(), b.get(), c.get(), d.get(), e.get(), f.get(), g.get(), h.get())
    
}

private class CombinedMappingProvider9<A, B, C, D, E, F, G, H, I, R>(
    private val a: AbstractProvider<A>,
    private val b: AbstractProvider<B>,
    private val c: AbstractProvider<C>,
    private val d: AbstractProvider<D>,
    private val e: AbstractProvider<E>,
    private val f: AbstractProvider<F>,
    private val g: AbstractProvider<G>,
    private val h: AbstractProvider<H>,
    private val i: AbstractProvider<I>,
    private val mapValue: (A, B, C, D, E, F, G, H, I) -> R
) : AbstractProvider<R>(ReentrantLock()) {
    
    init {
        changeLocks(lock, a, b, c, d, e, f, g, h, i)
        lock.withLock {
            addInactiveParents(a, b, c, d, e, f, g, h, i)
            addAsChildTo(a, b, c, d, e, f, g, h, i)
        }
    }
    
    override fun pull(): R = mapValue(a.get(), b.get(), c.get(), d.get(), e.get(), f.get(), g.get(), h.get(), i.get())
    
}

private class CombinedMappingProvider10<A, B, C, D, E, F, G, H, I, J, R>(
    private val a: AbstractProvider<A>,
    private val b: AbstractProvider<B>,
    private val c: AbstractProvider<C>,
    private val d: AbstractProvider<D>,
    private val e: AbstractProvider<E>,
    private val f: AbstractProvider<F>,
    private val g: AbstractProvider<G>,
    private val h: AbstractProvider<H>,
    private val i: AbstractProvider<I>,
    private val j: AbstractProvider<J>,
    private val mapValue: (A, B, C, D, E, F, G, H, I, J) -> R
) : AbstractProvider<R>(ReentrantLock()) {
    
    init {
        changeLocks(lock, a, b, c, d, e, f, g, h, i, j)
        lock.withLock {
            addInactiveParents(a, b, c, d, e, f, g, h, i, j)
            addAsChildTo(a, b, c, d, e, f, g, h, i, j)
        }
    }
    
    override fun pull(): R = mapValue(a.get(), b.get(), c.get(), d.get(), e.get(), f.get(), g.get(), h.get(), i.get(), j.get())
    
}