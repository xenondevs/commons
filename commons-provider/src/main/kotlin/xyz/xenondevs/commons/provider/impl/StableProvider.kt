@file:Suppress("PackageDirectoryMismatch") // needs to be in root package for sealed hierarchy, impl directory is used for source file organization
package xyz.xenondevs.commons.provider

// may be able to take advantage of Stable Values (https://openjdk.org/jeps/502) in the future

/**
 * A stable provider is an immutable provider whose value is loaded lazily.
 */
internal class StableProvider<T>(private val lazyValue: Lazy<T>) : ProviderImpl<T> {
    
    override val parents: Set<Provider<*>>
        get() = emptySet()
    override val children: Set<Provider<*>>
        get() = emptySet()
    
    override val value: DeferredValue<T> = DeferredValue.Lazy(lazyValue)
    
    override fun <R> strongMap(transform: (T) -> R): Provider<R> =
        map(transform)
    
    override fun <R> map(transform: (T) -> R): Provider<R> =
        StableProvider(lazy { transform(lazyValue.value) })
    
    override fun <R> strongFlatMap(transform: (T) -> Provider<R>): Provider<R> =
        transform(get())
    
    override fun <R> flatMap(transform: (T) -> Provider<R>): Provider<R> =
        transform(get())
    
    override fun <R> strongFlatMapMutable(transform: (T) -> MutableProvider<R>): MutableProvider<R> =
        transform(get())
    
    override fun <R> flatMapMutable(transform: (T) -> MutableProvider<R>): MutableProvider<R> =
        transform(get())
    
    @Suppress("UNCHECKED_CAST")
    override fun <R> lazyFlatMap(transform: (T) -> Provider<R>): Provider<R> =
        UnidirectionalLazyFlatMappedProvider(this, transform as (T) -> ProviderImpl<R>)
    
    @Suppress("UNCHECKED_CAST")
    override fun <R> strongLazyFlatMap(transform: (T) -> Provider<R>): Provider<R> =
        UnidirectionalLazyFlatMappedProvider(this, transform as (T) -> ProviderImpl<R>)
    
    @Suppress("UNCHECKED_CAST")
    override fun <R> lazyFlatMapMutable(transform: (T) -> MutableProvider<R>): MutableProvider<R> =
        BidirectionalLazyFlatMappedProvider(this, transform as (T) -> MutableProviderImpl<R>)
    
    @Suppress("UNCHECKED_CAST")
    override fun <R> strongLazyFlatMapMutable(transform: (T) -> MutableProvider<R>): MutableProvider<R> =
        BidirectionalLazyFlatMappedProvider(this, transform as (T) -> MutableProviderImpl<R>)
    
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
    
}