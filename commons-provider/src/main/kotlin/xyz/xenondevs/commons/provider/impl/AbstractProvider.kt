package xyz.xenondevs.commons.provider.impl

import xyz.xenondevs.commons.provider.MutableProvider
import xyz.xenondevs.commons.provider.Provider
import xyz.xenondevs.commons.provider.util.weakHashSet
import xyz.xenondevs.commons.provider.util.with
import xyz.xenondevs.commons.provider.util.without
import java.util.*

@Suppress("UNCHECKED_CAST")
class UpdateHandlerCollection<T> private constructor(
    val strongSubscribers: Collection<(T) -> Unit>,
    val weakSubscribers: Map<Any, Set<(Any, T) -> Unit>>,
    val strongObservers: Collection<() -> Unit>,
    val weakObservers: Map<Any, Set<(Any) -> Unit>>,
    val strongChildren: Collection<Provider<*>>,
    val weakChildren: Collection<Provider<*>>
) {
    
    val children: Set<Provider<*>>
        get() = buildSet { addAll(strongChildren); addAll(weakChildren) }
    
    //<editor-fold desc="withers">
    fun withStrongSubscriber(subscriber: (T) -> Unit) =
        copy(strongSubscribers = strongSubscribers.with(subscriber, ::ArrayList))
    
    fun withoutStrongSubscriber(subscriber: (T) -> Unit) =
        copy(strongSubscribers = strongSubscribers.without(subscriber, ::ArrayList))
    
    
    fun withStrongObserver(observer: () -> Unit) =
        copy(strongObservers = strongObservers.with(observer, ::ArrayList))
    
    fun withoutStrongObserver(observer: () -> Unit) =
        copy(strongObservers = strongObservers.without(observer, ::ArrayList))
    
    
    fun withStrongChild(child: Provider<*>) =
        copy(strongChildren = strongChildren.with(child, ::ArrayList))
    
    fun withoutStrongChild(child: Provider<*>) =
        copy(strongChildren = strongChildren.without(child, ::ArrayList))
    
    
    fun withWeakChild(child: Provider<*>) =
        copy(weakChildren = weakChildren.with(child, ::weakHashSet))
    
    fun withoutWeakChild(child: Provider<*>) =
        copy(weakChildren = weakChildren.without(child, ::weakHashSet))
    
    
    fun <O : Any> withWeakSubscriber(owner: O, subscriber: (O, T) -> Unit) =
        copy(weakSubscribers = weakSubscribers.with(owner, subscriber as (Any, T) -> Unit, ::WeakHashMap, ::HashSet))
    
    fun <O : Any> withoutWeakSubscriber(owner: O, subscriber: (O, T) -> Unit) =
        copy(weakSubscribers = weakSubscribers.without(owner, subscriber as (Any, T) -> Unit, ::WeakHashMap, ::HashSet))
    
    fun withoutWeakSubscriber(owner: Any) =
        copy(weakSubscribers = WeakHashMap(weakSubscribers).apply { remove(owner) })
    
    
    fun <O : Any> withWeakObserver(owner: O, observer: (O) -> Unit) =
        copy(weakObservers = weakObservers.with(owner, observer as (Any) -> Unit, ::WeakHashMap, ::HashSet))
    
    fun <O : Any> withoutWeakObserver(owner: O, observer: (O) -> Unit) =
        copy(weakObservers = weakObservers.without(owner, observer as (Any) -> Unit, ::WeakHashMap, ::HashSet))
    
    fun withoutWeakObserver(owner: Any) =
        copy(weakObservers = WeakHashMap(weakObservers).apply { remove(owner) })
    //</editor-fold>
    
    private fun copy(
        strongSubscribers: Collection<(T) -> Unit> = this.strongSubscribers,
        weakSubscribers: Map<Any, Set<(Any, T) -> Unit>> = this.weakSubscribers,
        strongObservers: Collection<() -> Unit> = this.strongObservers,
        weakObservers: Map<Any, Set<(Any) -> Unit>> = this.weakObservers,
        strongChildren: Collection<Provider<*>> = this.strongChildren,
        weakChildren: Collection<Provider<*>> = this.weakChildren
    ) = UpdateHandlerCollection(strongSubscribers, weakSubscribers, strongObservers, weakObservers, strongChildren, weakChildren)
    
    companion object {
        
        private val EMPTY = UpdateHandlerCollection<Any>(emptyList(), emptyMap(), emptyList(), emptyMap(), emptySet(), emptySet())
        
        fun <T> empty() = EMPTY as UpdateHandlerCollection<T>
        
    }
    
}

/**
 * Base class for all default implementations of [xyz.xenondevs.commons.provider.Provider] that can have children.
 */
internal abstract class AbstractProvider<T> : Provider<T> {
    
    @Volatile
    protected var updateHandlers = UpdateHandlerCollection.empty<T>()
        private set
    
    override val children: Set<Provider<*>>
        get() = updateHandlers.children
    
    //<editor-fold desc="update handler modifications">
    override fun subscribe(action: (T) -> Unit) =
        synchronized(this) { updateHandlers = updateHandlers.withStrongSubscriber(action) }
    
    override fun observe(action: () -> Unit) =
        synchronized(this) { updateHandlers = updateHandlers.withStrongObserver(action) }
    
    override fun <R : Any> subscribeWeak(owner: R, action: (R, T) -> Unit) =
        synchronized(this) { updateHandlers = updateHandlers.withWeakSubscriber(owner, action) }
    
    override fun <R : Any> observeWeak(owner: R, action: (R) -> Unit) =
        synchronized(this) { updateHandlers = updateHandlers.withWeakObserver(owner, action) }
    
    override fun unsubscribe(action: (T) -> Unit) =
        synchronized(this) { updateHandlers = updateHandlers.withoutStrongSubscriber(action) }
    
    override fun unobserve(action: () -> Unit) =
        synchronized(this) { updateHandlers = updateHandlers.withoutStrongObserver(action) }
    
    override fun <R : Any> unsubscribeWeak(owner: R, action: (R, T) -> Unit) =
        synchronized(this) { updateHandlers = updateHandlers.withoutWeakSubscriber(owner, action) }
    
    override fun <R : Any> unobserveWeak(owner: R, action: (R) -> Unit) =
        synchronized(this) { updateHandlers = updateHandlers.withoutWeakObserver(owner, action) }
    
    override fun <R : Any> unsubscribeWeak(owner: R) =
        synchronized(this) { updateHandlers = updateHandlers.withoutWeakSubscriber(owner) }
    
    override fun <R : Any> unobserveWeak(owner: R) =
        synchronized(this) { updateHandlers = updateHandlers.withoutWeakObserver(owner) }
    
    override fun addStrongChild(child: Provider<*>) =
        synchronized(this) { updateHandlers = updateHandlers.withStrongChild(child) }
    
    override fun removeStrongChild(child: Provider<*>) =
        synchronized(this) { updateHandlers = updateHandlers.withoutStrongChild(child) }
    
    override fun addWeakChild(child: Provider<*>) =
        synchronized(this) { updateHandlers = updateHandlers.withWeakChild(child) }
    
    override fun removeWeakChild(child: Provider<*>) =
        synchronized(this) { updateHandlers = updateHandlers.withoutWeakChild(child) }
    //</editor-fold>
    
    /**
     * Notifies all observers, subscribers and children except [ignore] that their parent, which
     * is this provider, has been updated.
     */
    fun UpdateHandlerCollection<T>.notify(ignore: Set<Provider<*>> = emptySet()) {
        // children
        strongChildren.forEach { if (it !in ignore) it.handleParentUpdated(this@AbstractProvider) }
        weakChildren.forEach { if (it !in ignore) it.handleParentUpdated(this@AbstractProvider) }
        
        // observers
        strongObservers.forEach { it() }
        weakObservers.forEach { (owner, actions) -> actions.forEach { it(owner) } }
        
        // subscribers
        if (strongSubscribers.isNotEmpty() || weakSubscribers.isNotEmpty()) {
            runCatching { value.value }.onSuccess { value ->
                strongSubscribers.forEach { it(value) }
                weakSubscribers.forEach { (owner, actions) -> actions.forEach { it(owner, value) } }
            }
        }
    }
    
    //<editor-fold desc="map / flatMap">
    override fun <R> map(transform: (T) -> R): Provider<R> {
        val provider = UnidirectionalTransformingProvider(this, transform)
        addWeakChild(provider)
        provider.handleParentUpdated(this) // propagate potentially lost update during provider creation and child assignment 
        return provider
    }
    
    override fun <R> strongMap(transform: (T) -> R): Provider<R> {
        val provider = UnidirectionalTransformingProvider(this, transform)
        addStrongChild(provider)
        provider.handleParentUpdated(this) // propagate potentially lost update during provider creation and child assignment
        return provider
    }
    
    override fun <R> immediateFlatMapMutable(transform: (T) -> MutableProvider<R>): MutableProvider<R> {
        val provider = BidirectionalImmediateFlatMappedProvider(this, transform, true)
        addWeakChild(provider)
        provider.handleParentUpdated(this) // propagate potentially lost update during provider creation and child assignment
        return provider
    }
    
    override fun <R> strongImmediateFlatMapMutable(transform: (T) -> MutableProvider<R>): MutableProvider<R> {
        val provider = BidirectionalImmediateFlatMappedProvider(this, transform, false)
        addStrongChild(provider)
        provider.handleParentUpdated(this) // propagate potentially lost update during provider creation and child assignment
        return provider
    }
    
    override fun <R> immediateFlatMap(transform: (T) -> Provider<R>): Provider<R> {
        val provider = UnidirectionalImmediateFlatMappedProvider(this, transform, true)
        addWeakChild(provider)
        provider.handleParentUpdated(this) // propagate potentially lost update during provider creation and child assignment
        return provider
    }
    
    override fun <R> strongImmediateFlatMap(transform: (T) -> Provider<R>): Provider<R> {
        val provider = UnidirectionalImmediateFlatMappedProvider(this, transform, false)
        addStrongChild(provider)
        provider.handleParentUpdated(this) // propagate potentially lost update during provider creation and child assignment
        return provider
    }
    
    override fun <R> flatMap(transform: (T) -> Provider<R>): Provider<R> {
        val provider = UnidirectionalLazyFlatMappedProvider(this, transform, true)
        addWeakChild(provider)
        provider.handleParentUpdated(this) // propagate potentially lost update during provider creation and child assignment
        return provider
    }
    
    override fun <R> strongFlatMap(transform: (T) -> Provider<R>): Provider<R> {
        val provider = UnidirectionalLazyFlatMappedProvider(this, transform, false)
        addStrongChild(provider)
        provider.handleParentUpdated(this) // propagate potentially lost update during provider creation and child assignment
        return provider
    }
    
    override fun <R> flatMapMutable(transform: (T) -> MutableProvider<R>): MutableProvider<R> {
        val provider = BidirectionalLazyFlatMappedProvider(this, transform, true)
        addWeakChild(provider)
        provider.handleParentUpdated(this) // propagate potentially lost update during provider creation and child assignment
        return provider
    }
    
    override fun <R> strongFlatMapMutable(transform: (T) -> MutableProvider<R>): MutableProvider<R> {
        val provider = BidirectionalLazyFlatMappedProvider(this, transform, false)
        addStrongChild(provider)
        provider.handleParentUpdated(this) // propagate potentially lost update during provider creation and child assignment
        return provider
    }
    //</editor-fold>
    
}

internal interface MutableProviderDefaults<T> : MutableProvider<T> {
    
    override fun <R> strongMap(transform: (T) -> R, untransform: (R) -> T): MutableProvider<R> {
        val provider = BidirectionalTransformingProvider(this, transform, untransform)
        addStrongChild(provider)
        provider.handleParentUpdated(this) // propagate potentially lost update during provider creation and child assignment
        return provider
    }
    
    override fun <R> map(transform: (T) -> R, untransform: (R) -> T): MutableProvider<R> {
        val provider = BidirectionalTransformingProvider(this, transform, untransform)
        addWeakChild(provider)
        provider.handleParentUpdated(this) // propagate potentially lost update during provider creation and child assignment
        return provider
    }
    
    override fun <R> mapObserved(createObservable: (T, () -> Unit) -> R): Provider<R> {
        val provider = ObservedValueUndirectionalTransformingProvider(this, createObservable)
        addStrongChild(provider)
        provider.handleParentUpdated(this) // propagate potentially lost update during provider creation and child assignment
        return provider
    }
    
    override fun <R> strongMapObserved(createObservable: (T, () -> Unit) -> R): Provider<R> {
        val provider = ObservedValueUndirectionalTransformingProvider(this, createObservable)
        addWeakChild(provider)
        provider.handleParentUpdated(this) // propagate potentially lost update during provider creation and child assignment
        return provider
    }
    
}