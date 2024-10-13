@file:OptIn(UnstableProviderApi::class)

package xyz.xenondevs.commons.provider.immutable

import xyz.xenondevs.commons.provider.AbstractProvider
import xyz.xenondevs.commons.provider.Provider
import xyz.xenondevs.commons.provider.UnstableProviderApi
import xyz.xenondevs.commons.provider.addAsChildTo
import xyz.xenondevs.commons.provider.changeLocks
import xyz.xenondevs.commons.tuple.Tuple10
import xyz.xenondevs.commons.tuple.Tuple2
import xyz.xenondevs.commons.tuple.Tuple3
import xyz.xenondevs.commons.tuple.Tuple4
import xyz.xenondevs.commons.tuple.Tuple5
import xyz.xenondevs.commons.tuple.Tuple6
import xyz.xenondevs.commons.tuple.Tuple7
import xyz.xenondevs.commons.tuple.Tuple8
import xyz.xenondevs.commons.tuple.Tuple9
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * Creates and returns a new [Provider] that combines all values of [providers].
 */
@Suppress("UNCHECKED_CAST")
fun <T> combinedProvider(providers: List<Provider<T>>): Provider<List<T>> =
    CombinedProvider(providers as List<AbstractProvider<T>>)

/**
 * Creates and returns a new [Provider] that combines the values of [a] and [b].
 */
fun <A, B> combinedProvider(
    a: Provider<A>,
    b: Provider<B>
): Provider<Tuple2<A, B>> = CombinedProvider2(
    a as AbstractProvider<A>,
    b as AbstractProvider<B>
)

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b] and [c].
 */
fun <A, B, C> combinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>
): Provider<Tuple3<A, B, C>> = CombinedProvider3(
    a as AbstractProvider<A>,
    b as AbstractProvider<B>,
    c as AbstractProvider<C>
)

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c] and [d].
 */
fun <A, B, C, D> combinedProvider(
    a: Provider<A>, 
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>
): Provider<Tuple4<A, B, C, D>> = CombinedProvider4(
    a as AbstractProvider<A>,
    b as AbstractProvider<B>,
    c as AbstractProvider<C>,
    d as AbstractProvider<D>
)

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d] and [e].
 */
fun <A, B, C, D, E> combinedProvider(
    a: Provider<A>,
    b: Provider<B>, 
    c: Provider<C>, 
    d: Provider<D>,
    e: Provider<E>
): Provider<Tuple5<A, B, C, D, E>> = CombinedProvider5(
    a as AbstractProvider<A>,
    b as AbstractProvider<B>,
    c as AbstractProvider<C>,
    d as AbstractProvider<D>,
    e as AbstractProvider<E>
)

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d], [e] and [f].
 */
fun <A, B, C, D, E, F> combinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    e: Provider<E>, 
    f: Provider<F>
): Provider<Tuple6<A, B, C, D, E, F>> = CombinedProvider6(
    a as AbstractProvider<A>,
    b as AbstractProvider<B>,
    c as AbstractProvider<C>,
    d as AbstractProvider<D>,
    e as AbstractProvider<E>,
    f as AbstractProvider<F>
)

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d], [e], [f] and [g].
 */
fun <A, B, C, D, E, F, G> combinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    e: Provider<E>,
    f: Provider<F>,
    g: Provider<G>
): Provider<Tuple7<A, B, C, D, E, F, G>> = CombinedProvider7(
    a as AbstractProvider<A>,
    b as AbstractProvider<B>,
    c as AbstractProvider<C>,
    d as AbstractProvider<D>,
    e as AbstractProvider<E>,
    f as AbstractProvider<F>,
    g as AbstractProvider<G>
)

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d], [e], [f], [g] and [h].
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
): Provider<Tuple8<A, B, C, D, E, F, G, H>> = CombinedProvider8(
    a as AbstractProvider<A>,
    b as AbstractProvider<B>,
    c as AbstractProvider<C>,
    d as AbstractProvider<D>,
    e as AbstractProvider<E>,
    f as AbstractProvider<F>,
    g as AbstractProvider<G>,
    h as AbstractProvider<H>
)

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d], [e], [f], [g], [h] and [i].
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
): Provider<Tuple9<A, B, C, D, E, F, G, H, I>> = CombinedProvider9(
    a as AbstractProvider<A>,
    b as AbstractProvider<B>,
    c as AbstractProvider<C>,
    d as AbstractProvider<D>,
    e as AbstractProvider<E>,
    f as AbstractProvider<F>,
    g as AbstractProvider<G>,
    h as AbstractProvider<H>,
    i as AbstractProvider<I>
)

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d], [e], [f], [g], [h], [i] and [j].
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
): Provider<Tuple10<A, B, C, D, E, F, G, H, I, J>> = CombinedProvider10(
    a as AbstractProvider<A>,
    b as AbstractProvider<B>,
    c as AbstractProvider<C>,
    d as AbstractProvider<D>,
    e as AbstractProvider<E>,
    f as AbstractProvider<F>,
    g as AbstractProvider<G>,
    h as AbstractProvider<H>,
    i as AbstractProvider<I>,
    j as AbstractProvider<J>
)

private class CombinedProvider<T>(
    private val providers: List<AbstractProvider<T>>
) : AbstractProvider<List<T>>(ReentrantLock()) {
    
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
    
    override fun pull(): List<T> {
        return providers.map { it.get() }
    }
    
}

private class CombinedProvider2<A, B>(
    private val a: AbstractProvider<A>,
    private val b: AbstractProvider<B>
) : AbstractProvider<Tuple2<A, B>>(ReentrantLock()) {
    
    init {
        changeLocks(lock, a, b)
        lock.withLock {
            addInactiveParents(a, b)
            addAsChildTo(a, b)
        }
    }
    
    override fun pull() = Tuple2(a.get(), b.get())
    
}

private class CombinedProvider3<A, B, C>(
    private val a: AbstractProvider<A>,
    private val b: AbstractProvider<B>,
    private val c: AbstractProvider<C>
) : AbstractProvider<Tuple3<A, B, C>>(ReentrantLock()) {
    
    init {
        changeLocks(lock, a, b, c)
        lock.withLock {
            addInactiveParents(a, b, c)
            addAsChildTo(a, b, c)
        }
    }
    
    override fun pull() = Tuple3(a.get(), b.get(), c.get())
    
}

private class CombinedProvider4<A, B, C, D>(
    val a: AbstractProvider<A>,
    val b: AbstractProvider<B>,
    val c: AbstractProvider<C>,
    val d: AbstractProvider<D>
) : AbstractProvider<Tuple4<A, B, C, D>>(ReentrantLock()) {
    
    init {
        changeLocks(lock, a, b, c, d)
        lock.withLock {
            addInactiveParents(a, b, c, d)
            addAsChildTo(a, b, c, d)
        }
    }
    
    override fun pull() = Tuple4(a.get(), b.get(), c.get(), d.get())
    
}

private class CombinedProvider5<A, B, C, D, E>(
    val a: AbstractProvider<A>,
    val b: AbstractProvider<B>,
    val c: AbstractProvider<C>,
    val d: AbstractProvider<D>,
    val e: AbstractProvider<E>
) : AbstractProvider<Tuple5<A, B, C, D, E>>(ReentrantLock()) {
    
    init {
        changeLocks(lock, a, b, c, d, e)
        lock.withLock {
            addInactiveParents(a, b, c, d, e)
            addAsChildTo(a, b, c, d, e)
        }
    }
    
    override fun pull() = Tuple5(a.get(), b.get(), c.get(), d.get(), e.get())
    
}

private class CombinedProvider6<A, B, C, D, E, F>(
    val a: AbstractProvider<A>,
    val b: AbstractProvider<B>,
    val c: AbstractProvider<C>,
    val d: AbstractProvider<D>,
    val e: AbstractProvider<E>,
    val f: AbstractProvider<F>
) : AbstractProvider<Tuple6<A, B, C, D, E, F>>(ReentrantLock()) {
    
    init {
        changeLocks(lock, a, b, c, d, e, f)
        lock.withLock {
            addInactiveParents(a, b, c, d, e, f)
            addAsChildTo(a, b, c, d, e, f)
        }
    }
    
    override fun pull() = Tuple6(a.get(), b.get(), c.get(), d.get(), e.get(), f.get())
    
}

private class CombinedProvider7<A, B, C, D, E, F, G>(
    val a: AbstractProvider<A>,
    val b: AbstractProvider<B>,
    val c: AbstractProvider<C>,
    val d: AbstractProvider<D>,
    val e: AbstractProvider<E>,
    val f: AbstractProvider<F>,
    val g: AbstractProvider<G>
) : AbstractProvider<Tuple7<A, B, C, D, E, F, G>>(ReentrantLock()) {
    
    init {
        changeLocks(lock, a, b, c, d, e, f, g)
        lock.withLock {
            addInactiveParents(a, b, c, d, e, f, g)
            addAsChildTo(a, b, c, d, e, f, g)
        }
    }
    
    override fun pull() = Tuple7(a.get(), b.get(), c.get(), d.get(), e.get(), f.get(), g.get())
    
}

private class CombinedProvider8<A, B, C, D, E, F, G, H>(
    val a: AbstractProvider<A>,
    val b: AbstractProvider<B>,
    val c: AbstractProvider<C>,
    val d: AbstractProvider<D>,
    val e: AbstractProvider<E>,
    val f: AbstractProvider<F>,
    val g: AbstractProvider<G>,
    val h: AbstractProvider<H>
) : AbstractProvider<Tuple8<A, B, C, D, E, F, G, H>>(ReentrantLock()) {
    
    init {
        changeLocks(lock, a, b, c, d, e, f, g, h)
        lock.withLock {
            addInactiveParents(a, b, c, d, e, f, g, h)
            addAsChildTo(a, b, c, d, e, f, g, h)
        }
    }
    
    override fun pull() = Tuple8(a.get(), b.get(), c.get(), d.get(), e.get(), f.get(), g.get(), h.get())
    
}

private class CombinedProvider9<A, B, C, D, E, F, G, H, I>(
    val a: AbstractProvider<A>,
    val b: AbstractProvider<B>,
    val c: AbstractProvider<C>,
    val d: AbstractProvider<D>,
    val e: AbstractProvider<E>,
    val f: AbstractProvider<F>,
    val g: AbstractProvider<G>,
    val h: AbstractProvider<H>,
    val i: AbstractProvider<I>
) : AbstractProvider<Tuple9<A, B, C, D, E, F, G, H, I>>(ReentrantLock()) {
    
    init {
        changeLocks(lock, a, b, c, d, e, f, g, h, i)
        lock.withLock {
            addInactiveParents(a, b, c, d, e, f, g, h, i)
            addAsChildTo(a, b, c, d, e, f, g, h, i)
        }
    }
    
    override fun pull() = Tuple9(a.get(), b.get(), c.get(), d.get(), e.get(), f.get(), g.get(), h.get(), i.get())
    
}

private class CombinedProvider10<A, B, C, D, E, F, G, H, I, J>(
    val a: AbstractProvider<A>,
    val b: AbstractProvider<B>,
    val c: AbstractProvider<C>,
    val d: AbstractProvider<D>,
    val e: AbstractProvider<E>,
    val f: AbstractProvider<F>,
    val g: AbstractProvider<G>,
    val h: AbstractProvider<H>,
    val i: AbstractProvider<I>,
    val j: AbstractProvider<J>
) : AbstractProvider<Tuple10<A, B, C, D, E, F, G, H, I, J>>(ReentrantLock()) {
    
    init {
        changeLocks(lock, a, b, c, d, e, f, g, h, i, j)
        lock.withLock {
            addInactiveParents(a, b, c, d, e, f, g, h, i, j)
            addAsChildTo(a, b, c, d, e, f, g, h, i, j)
        }
    }
    
    override fun pull() = Tuple10(a.get(), b.get(), c.get(), d.get(), e.get(), f.get(), g.get(), h.get(), i.get(), j.get())
    
}