@file:Suppress("PackageDirectoryMismatch") // needs to be in root package for sealed hierarchy, impl directory is used for source file organization
package xyz.xenondevs.commons.provider

import java.util.concurrent.atomic.AtomicLong
import kotlin.math.max

internal sealed interface DeferredValue<out T> : Comparable<DeferredValue<*>> {
    
    val state: Long
    val value: T
    
    override fun compareTo(other: DeferredValue<*>): Int = state.compareTo(other.state)
    
    class Mapped<P, T>(val parent: DeferredValue<P>, val transform: (P) -> T) : DeferredValue<T> {
        
        override val state: Long get() = parent.state
        override val value: T by lazy { transform(parent.value) }
        
    }
    
    class MappedWithSelf<P, T>(val parent: DeferredValue<P>, val transform: (P, DeferredValue<T>) -> T) : DeferredValue<T> {
        
        override val state: Long get() = parent.state
        override val value: T by lazy { transform(parent.value, this) }
        
    }
    
    class MappedMulti<P, T>(val parents: List<DeferredValue<P>>, val transform: (List<P>) -> T) : DeferredValue<T> {
        
        override val state: Long get() = parents.maxOf { it.state }
        override val value: T by lazy { transform(parents.map { it.value }) }
        
    }
    
    class Delegate<T>(val delegate: DeferredValue<T>, val minState: Long) : DeferredValue<T> {
        
        override val state: Long get() = max(minState, delegate.state)
        override val value: T get() = delegate.value
        
    }
    
    class DelegateLambda<T>(val getDelegate: () -> DeferredValue<T>, val getMinState: () -> Long) : DeferredValue<T> {
        
        override val state: Long get() = max(getMinState(), getDelegate().state)
        override val value: T get() = getDelegate().value
    }
    
    class Lazy<T>(lazy: kotlin.Lazy<T>) : DeferredValue<T> {
        
        constructor(initializer: () -> T) : this(lazy(initializer))
        
        override val state: Long = globalState.incrementAndGet()
        override val value: T by lazy
        
    }
    
    class Direct<T>(override val value: T) : DeferredValue<T> {
        
        override val state: Long = globalState.incrementAndGet()
        
    }
    
    private companion object {
        
        val globalState = AtomicLong(0L)
        
    }
    
}