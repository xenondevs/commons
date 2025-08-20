package xyz.xenondevs.commons.provider.impl

import xyz.xenondevs.commons.provider.DeferredValue
import xyz.xenondevs.commons.provider.MutableProvider
import xyz.xenondevs.commons.provider.Provider

/**
 * A stable provider is based on an immutable [value], meaning the provider's value itself
 * is immutable and possibly lazily initialized.
 */
internal class StableProvider<T>(override val value: DeferredValue<T>) : Provider<T> {
    
    override val parents: Set<Provider<*>>
        get() = emptySet()
    override val children: Set<Provider<*>>
        get() = emptySet()
    
    override fun <R> strongMap(transform: (T) -> R): Provider<R> =
        map(transform)
    
    override fun <R> map(transform: (T) -> R): Provider<R> =
        StableProvider(DeferredValue.Mapped(value, transform))
    
    override fun <R> strongImmediateFlatMap(transform: (T) -> Provider<R>): Provider<R> =
        transform(get())
    
    override fun <R> immediateFlatMap(transform: (T) -> Provider<R>): Provider<R> =
        transform(get())
    
    override fun <R> strongImmediateFlatMapMutable(transform: (T) -> MutableProvider<R>): MutableProvider<R> =
        transform(get())
    
    override fun <R> immediateFlatMapMutable(transform: (T) -> MutableProvider<R>): MutableProvider<R> =
        transform(get())
    
    override fun <R> flatMap(transform: (T) -> Provider<R>): Provider<R> =
        UnidirectionalLazyFlatMappedProvider(this, transform, true)
    
    override fun <R> strongFlatMap(transform: (T) -> Provider<R>): Provider<R> =
        UnidirectionalLazyFlatMappedProvider(this, transform, false)
    
    override fun <R> flatMapMutable(transform: (T) -> MutableProvider<R>): MutableProvider<R> =
        BidirectionalLazyFlatMappedProvider(this, transform, true)
    
    override fun <R> strongFlatMapMutable(transform: (T) -> MutableProvider<R>): MutableProvider<R> =
        BidirectionalLazyFlatMappedProvider(this, transform, false)
    
    // the value is immutable, so subscribers / observers would never be called
    override fun subscribe(action: (T) -> Unit) = Unit
    override fun observe(action: () -> Unit) = Unit
    override fun <R : Any> subscribeWeak(owner: R, action: (R, T) -> Unit) = Unit
    override fun <R : Any> observeWeak(owner: R, action: (R) -> Unit) = Unit
    override fun unsubscribe(action: (T) -> Unit) = Unit
    override fun unobserve(action: () -> Unit) = Unit
    override fun <R : Any> unsubscribeWeak(owner: R, action: (R, T) -> Unit) = Unit
    override fun <R : Any> unsubscribeWeak(owner: R) = Unit
    override fun <R : Any> unobserveWeak(owner: R, action: (R) -> Unit) = Unit
    override fun <R : Any> unobserveWeak(owner: R) = Unit
    override fun addStrongChild(child: Provider<*>) = Unit
    override fun removeStrongChild(child: Provider<*>) = Unit
    override fun addWeakChild(child: Provider<*>) = Unit
    override fun removeWeakChild(child: Provider<*>) = Unit
    override fun handleParentUpdated(updatedParent: Provider<*>) = Unit
    
}