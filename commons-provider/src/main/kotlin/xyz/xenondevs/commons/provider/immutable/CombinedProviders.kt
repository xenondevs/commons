package xyz.xenondevs.commons.provider.immutable

import xyz.xenondevs.commons.provider.Provider
import xyz.xenondevs.commons.tuple.Tuple10
import xyz.xenondevs.commons.tuple.Tuple2
import xyz.xenondevs.commons.tuple.Tuple3
import xyz.xenondevs.commons.tuple.Tuple4
import xyz.xenondevs.commons.tuple.Tuple5
import xyz.xenondevs.commons.tuple.Tuple6
import xyz.xenondevs.commons.tuple.Tuple7
import xyz.xenondevs.commons.tuple.Tuple8
import xyz.xenondevs.commons.tuple.Tuple9

fun <T> combinedProvider(providers: List<Provider<T>>): Provider<List<T>> =
    CombinedProvider(providers)

operator fun <A, B> Provider<A>.plus(b: Provider<B>): CombinedProvider2<A, B> =
    CombinedProvider2(this, b)

operator fun <A, B, C> CombinedProvider2<A, B>.plus(c: Provider<C>): CombinedProvider3<A, B, C> =
    CombinedProvider3(a, b, c)

operator fun <A, B, C, D> CombinedProvider3<A, B, C>.plus(d: Provider<D>): CombinedProvider4<A, B, C, D> =
    CombinedProvider4(a, b, c, d)

operator fun <A, B, C, D, E> CombinedProvider4<A, B, C, D>.plus(e: Provider<E>): CombinedProvider5<A, B, C, D, E> =
    CombinedProvider5(a, b, c, d, e)

operator fun <A, B, C, D, E, F> CombinedProvider5<A, B, C, D, E>.plus(f: Provider<F>): CombinedProvider6<A, B, C, D, E, F> =
    CombinedProvider6(a, b, c, d, e, f)

operator fun <A, B, C, D, E, F, G> CombinedProvider6<A, B, C, D, E, F>.plus(g: Provider<G>): CombinedProvider7<A, B, C, D, E, F, G> =
    CombinedProvider7(a, b, c, d, e, f, g)

operator fun <A, B, C, D, E, F, G, H> CombinedProvider7<A, B, C, D, E, F, G>.plus(h: Provider<H>): CombinedProvider8<A, B, C, D, E, F, G, H> =
    CombinedProvider8(a, b, c, d, e, f, g, h)

operator fun <A, B, C, D, E, F, G, H, I> CombinedProvider8<A, B, C, D, E, F, G, H>.plus(i: Provider<I>): CombinedProvider9<A, B, C, D, E, F, G, H, I> =
    CombinedProvider9(a, b, c, d, e, f, g, h, i)

operator fun <A, B, C, D, E, F, G, H, I, J> CombinedProvider9<A, B, C, D, E, F, G, H, I>.plus(j: Provider<J>): CombinedProvider10<A, B, C, D, E, F, G, H, I, J> =
    CombinedProvider10(a, b, c, d, e, f, g, h, i, j)

fun <A, B> combinedProvider(a: Provider<A>, b: Provider<B>): CombinedProvider2<A, B> =
    CombinedProvider2(a, b)

fun <A, B, C> combinedProvider(a: Provider<A>, b: Provider<B>, c: Provider<C>): CombinedProvider3<A, B, C> =
    CombinedProvider3(a, b, c)

fun <A, B, C, D> combinedProvider(a: Provider<A>, b: Provider<B>, c: Provider<C>, d: Provider<D>): CombinedProvider4<A, B, C, D> =
    CombinedProvider4(a, b, c, d)

fun <A, B, C, D, E> combinedProvider(a: Provider<A>, b: Provider<B>, c: Provider<C>, d: Provider<D>, e: Provider<E>): CombinedProvider5<A, B, C, D, E> =
    CombinedProvider5(a, b, c, d, e)

fun <A, B, C, D, E, F> combinedProvider(a: Provider<A>, b: Provider<B>, c: Provider<C>, d: Provider<D>, e: Provider<E>, f: Provider<F>): CombinedProvider6<A, B, C, D, E, F> =
    CombinedProvider6(a, b, c, d, e, f)

fun <A, B, C, D, E, F, G> combinedProvider(a: Provider<A>, b: Provider<B>, c: Provider<C>, d: Provider<D>, e: Provider<E>, f: Provider<F>, g: Provider<G>): CombinedProvider7<A, B, C, D, E, F, G> =
    CombinedProvider7(a, b, c, d, e, f, g)

fun <A, B, C, D, E, F, G, H> combinedProvider(a: Provider<A>, b: Provider<B>, c: Provider<C>, d: Provider<D>, e: Provider<E>, f: Provider<F>, g: Provider<G>, h: Provider<H>): CombinedProvider8<A, B, C, D, E, F, G, H> =
    CombinedProvider8(a, b, c, d, e, f, g, h)

fun <A, B, C, D, E, F, G, H, I> combinedProvider(a: Provider<A>, b: Provider<B>, c: Provider<C>, d: Provider<D>, e: Provider<E>, f: Provider<F>, g: Provider<G>, h: Provider<H>, i: Provider<I>): CombinedProvider9<A, B, C, D, E, F, G, H, I> =
    CombinedProvider9(a, b, c, d, e, f, g, h, i)

fun <A, B, C, D, E, F, G, H, I, J> combinedProvider(a: Provider<A>, b: Provider<B>, c: Provider<C>, d: Provider<D>, e: Provider<E>, f: Provider<F>, g: Provider<G>, h: Provider<H>, i: Provider<I>, j: Provider<J>): CombinedProvider10<A, B, C, D, E, F, G, H, I, J> =
    CombinedProvider10(a, b, c, d, e, f, g, h, i, j)

internal class CombinedProvider<T>(
    private val providers: List<Provider<out T>>
) : Provider<List<T>>() {
    
    init {
        providers.forEach { it.addChild(this) }
    }
    
    override fun loadValue(): List<T> {
        return providers.map { it.get() }
    }
    
}

class CombinedProvider2<A, B> internal constructor(
    val a: Provider<A>,
    val b: Provider<B>
) : Provider<Tuple2<A, B>>() {
    
    init {
        a.addChild(this)
        b.addChild(this)
    }
    
    override fun loadValue() = Tuple2(a.get(), b.get())
    
}

class CombinedProvider3<A, B, C> internal constructor(
    val a: Provider<A>,
    val b: Provider<B>,
    val c: Provider<C>
) : Provider<Tuple3<A, B, C>>() {
    
    init {
        a.addChild(this)
        b.addChild(this)
        c.addChild(this)
    }
    
    override fun loadValue() = Tuple3(a.get(), b.get(), c.get())
    
}

class CombinedProvider4<A, B, C, D> internal constructor(
    val a: Provider<A>,
    val b: Provider<B>,
    val c: Provider<C>,
    val d: Provider<D>
) : Provider<Tuple4<A, B, C, D>>() {
    
    init {
        a.addChild(this)
        b.addChild(this)
        c.addChild(this)
        d.addChild(this)
    }
    
    override fun loadValue() = Tuple4(a.get(), b.get(), c.get(), d.get())
    
}

class CombinedProvider5<A, B, C, D, E> internal constructor(
    val a: Provider<A>,
    val b: Provider<B>,
    val c: Provider<C>,
    val d: Provider<D>,
    val e: Provider<E>
) : Provider<Tuple5<A, B, C, D, E>>() {
    
    init {
        a.addChild(this)
        b.addChild(this)
        c.addChild(this)
        d.addChild(this)
        e.addChild(this)
    }
    
    override fun loadValue() = Tuple5(a.get(), b.get(), c.get(), d.get(), e.get())
    
}

class CombinedProvider6<A, B, C, D, E, F> internal constructor(
    val a: Provider<A>,
    val b: Provider<B>,
    val c: Provider<C>,
    val d: Provider<D>,
    val e: Provider<E>,
    val f: Provider<F>
) : Provider<Tuple6<A, B, C, D, E, F>>() {
    
    init {
        a.addChild(this)
        b.addChild(this)
        c.addChild(this)
        d.addChild(this)
        e.addChild(this)
        f.addChild(this)
    }
    
    override fun loadValue() = Tuple6(a.get(), b.get(), c.get(), d.get(), e.get(), f.get())
    
}

class CombinedProvider7<A, B, C, D, E, F, G> internal constructor(
    val a: Provider<A>,
    val b: Provider<B>,
    val c: Provider<C>,
    val d: Provider<D>,
    val e: Provider<E>,
    val f: Provider<F>,
    val g: Provider<G>
) : Provider<Tuple7<A, B, C, D, E, F, G>>() {
    
    init {
        a.addChild(this)
        b.addChild(this)
        c.addChild(this)
        d.addChild(this)
        e.addChild(this)
        f.addChild(this)
        g.addChild(this)
    }
    
    override fun loadValue() = Tuple7(a.get(), b.get(), c.get(), d.get(), e.get(), f.get(), g.get())
    
}

class CombinedProvider8<A, B, C, D, E, F, G, H> internal constructor(
    val a: Provider<A>,
    val b: Provider<B>,
    val c: Provider<C>,
    val d: Provider<D>,
    val e: Provider<E>,
    val f: Provider<F>,
    val g: Provider<G>,
    val h: Provider<H>
) : Provider<Tuple8<A, B, C, D, E, F, G, H>>() {
    
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
    
    override fun loadValue() = Tuple8(a.get(), b.get(), c.get(), d.get(), e.get(), f.get(), g.get(), h.get())
    
}

class CombinedProvider9<A, B, C, D, E, F, G, H, I> internal constructor(
    val a: Provider<A>,
    val b: Provider<B>,
    val c: Provider<C>,
    val d: Provider<D>,
    val e: Provider<E>,
    val f: Provider<F>,
    val g: Provider<G>,
    val h: Provider<H>,
    val i: Provider<I>
) : Provider<Tuple9<A, B, C, D, E, F, G, H, I>>() {
    
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
    
    override fun loadValue() = Tuple9(a.get(), b.get(), c.get(), d.get(), e.get(), f.get(), g.get(), h.get(), i.get())
    
}

class CombinedProvider10<A, B, C, D, E, F, G, H, I, J> internal constructor(
    val a: Provider<A>,
    val b: Provider<B>,
    val c: Provider<C>,
    val d: Provider<D>,
    val e: Provider<E>,
    val f: Provider<F>,
    val g: Provider<G>,
    val h: Provider<H>,
    val i: Provider<I>,
    val j: Provider<J>
) : Provider<Tuple10<A, B, C, D, E, F, G, H, I, J>>() {
    
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
    
    override fun loadValue() = Tuple10(a.get(), b.get(), c.get(), d.get(), e.get(), f.get(), g.get(), h.get(), i.get(), j.get())
    
}