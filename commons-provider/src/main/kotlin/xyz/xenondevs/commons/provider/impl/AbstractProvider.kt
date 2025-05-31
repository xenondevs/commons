@file:Suppress("PackageDirectoryMismatch") // needs to be in root package for sealed hierarchy, impl directory is used for source file organization
package xyz.xenondevs.commons.provider

import xyz.xenondevs.commons.collections.weakHashSet
import java.util.*

/**
 * Base interface for all implementations of [Provider].
 */
internal interface ProviderImpl<out T> : Provider<T> {
    
    val value: DeferredValue<T>
    
    override fun get(): T = value.value
    
}

/**
 * Base class for all implementations of [Provider] that can have children.
 */
internal abstract class AbstractChildContainingProvider<T> : ProviderWithChildren<T> {
    
    private val subscribers: MutableList<(T) -> Unit> = Collections.synchronizedList(ArrayList(0))
    private val weakSubscribers: MutableMap<Any, MutableList<(Any, T) -> Unit>> = Collections.synchronizedMap(WeakHashMap(0))
    private val observers: MutableList<() -> Unit> = Collections.synchronizedList(ArrayList(0))
    private val weakObservers: MutableMap<Any, MutableList<(Any) -> Unit>> = Collections.synchronizedMap(WeakHashMap(0))
    
    private val children: MutableSet<HasParents> = Collections.synchronizedSet(HashSet())
    private val weakChildren: MutableSet<HasParents> = Collections.synchronizedSet(weakHashSet())
    
    /**
     * Creates a list of runnables that notify all subscribers, observers, and children (except [ignore]) of the value of this provider.
     */
    protected fun prepareNotifiers(ignore: Provider<*>? = null): List<() -> Unit> {
        val preparedSubscribers = ArrayList<() -> Unit>()
        
        synchronized(children) {
            for (child in children) {
                if (child === ignore)
                    continue
                preparedSubscribers += { child.handleParentUpdated(this) }
            }
        }
        
        synchronized(weakChildren) {
            for (weakChild in weakChildren) {
                if (weakChild === ignore)
                    continue
                preparedSubscribers += { weakChild.handleParentUpdated(this) }
            }
        }
        
        synchronized(observers) {
            preparedSubscribers += observers
        }
        
        synchronized(weakObservers) {
            for ((owner, actions) in weakObservers) {
                for (action in actions) {
                    preparedSubscribers += { action(owner) }
                }
            }
        }
        
        synchronized(subscribers) {
            if (subscribers.isNotEmpty()) {
                val value = get()
                for (subscriber in subscribers) {
                    preparedSubscribers += { subscriber(value) }
                }
            }
        }
        
        synchronized(weakSubscribers) {
            if (weakSubscribers.isNotEmpty()) {
                val value = get()
                for ((owner, actions) in weakSubscribers) {
                    for (action in actions) {
                        preparedSubscribers += { action(owner, value) }
                    }
                }
            }
        }
        
        return preparedSubscribers
    }
    
    //<editor-fold desc="(un)registering subscribers / observers">
    override fun subscribe(action: (T) -> Unit) {
        subscribers += action
    }
    
    override fun observe(action: () -> Unit) {
        observers += action
    }
    
    @Suppress("UNCHECKED_CAST")
    override fun <R : Any> subscribeWeak(owner: R, action: (R, T) -> Unit) {
        action as (Any, T) -> Unit
        weakSubscribers.compute(owner) { _, list -> (list ?: ArrayList()).also { it.add(action) } }
    }
    
    @Suppress("UNCHECKED_CAST")
    override fun <R : Any> observeWeak(owner: R, action: (R) -> Unit) {
        action as (Any) -> Unit
        weakObservers.compute(owner) { _, list -> (list ?: ArrayList()).also { it.add(action) } }
    }
    
    override fun unsubscribe(action: (T) -> Unit) { 
        subscribers -= action
    }
    
    override fun unobserve(action: () -> Unit) {
        observers -= action
    }
    
    @Suppress("UNCHECKED_CAST")
    override fun <R : Any> unsubscribeWeak(owner: R, action: (R, T) -> Unit) {
        action as (Any, T) -> Unit
        weakSubscribers.compute(owner) {_, value -> value?.also { it.remove(action) } }
    }
    
    @Suppress("UNCHECKED_CAST")
    override fun <R : Any> unobserveWeak(owner: R, action: (R) -> Unit) {
        action as (Any) -> Unit
        weakObservers.compute(owner) { _, value -> value?.also { it.remove(action) } }
    }
    
    override fun <R : Any> unsubscribeWeak(owner: R) {
        weakSubscribers.remove(owner)
    }
    
    override fun <R : Any> unobserveWeak(owner: R) {
        weakObservers.remove(owner)
    }
    //</editor-fold>
    
    //<editor-fold desc="(un)registering (weak) children">
    override fun addStrongChild(child: HasParents) {
        children += child
    }
    
    override fun removeStrongChild(child: HasParents) {
        children -= child
    }
    
    override fun addWeakChild(child: HasParents) {
        weakChildren += child
    }
    
    override fun removeWeakChild(child: HasParents) {
        weakChildren -= child
    }
    //</editor-fold>
    
}

/**
 * A provider that can have parent(s), which is every non-root provider.
 */
internal interface HasParents {
    
    /**
     * Handles the update of [updatedParent].
     */
    fun handleParentUpdated(updatedParent: ProviderImpl<*>)
    
}

/**
 * A provider that can have children, which is every non-immutable provider.
 */
internal interface ProviderWithChildren<T> : ProviderImpl<T> {
    
    /**
     * Adds a child provider using a strong reference.
     */
    fun addStrongChild(child: HasParents)
    
    /**
     * Removes a strongly referenced child provider.
     */
    fun removeStrongChild(child: HasParents)
    
    /**
     * Adds a child provider using a weak reference.
     */
    fun addWeakChild(child: HasParents)
    
    /**
     * Removes a weakly referenced child provider.
     */
    fun removeWeakChild(child: HasParents)
    
    override fun <R> map(transform: (T) -> R): Provider<R> {
        val provider = UnidirectionalTransformingProvider(this, transform)
        addWeakChild(provider)
        return provider
    }
    
    override fun <R> strongMap(transform: (T) -> R): Provider<R> {
        val provider = UnidirectionalTransformingProvider(this, transform)
        addStrongChild(provider)
        return provider
    }
    
    @Suppress("UNCHECKED_CAST")
    override fun <R> flatMapMutable(transform: (T) -> MutableProvider<R>): MutableProvider<R> {
        val provider = BidirectionalFlatMappedProvider(this, transform as (T) -> MutableProviderImpl<R>)
        addWeakChild(provider)
        return provider
    }
    
    @Suppress("UNCHECKED_CAST")
    override fun <R> strongFlatMapMutable(transform: (T) -> MutableProvider<R>): MutableProvider<R> {
        val provider = BidirectionalFlatMappedProvider(this, transform as (T) -> MutableProviderImpl<R>)
        addStrongChild(provider)
        return provider
    }
    
    @Suppress("UNCHECKED_CAST")
    override fun <R> flatMap(transform: (T) -> Provider<R>): Provider<R> {
        val provider = UnidirectionalFlatMappedProvider(this, transform as (T) -> ProviderImpl<R>)
        addWeakChild(provider)
        return provider
    }
    
    @Suppress("UNCHECKED_CAST")
    override fun <R> strongFlatMap(transform: (T) -> Provider<R>): Provider<R> {
        val provider = UnidirectionalFlatMappedProvider(this, transform as (T) -> ProviderImpl<R>)
        addStrongChild(provider)
        return provider
    }
    
    @Suppress("UNCHECKED_CAST")
    override fun <R> lazyFlatMap(transform: (T) -> Provider<R>): Provider<R> {
        val provider = UnidirectionalLazyFlatMappedProvider(this, transform as (T) -> ProviderImpl<R>)
        addWeakChild(provider)
        return provider
    }
    
}

internal interface MutableProviderImpl<T> : ProviderWithChildren<T>, MutableProvider<T> {
    
    override fun <R> strongMap(transform: (T) -> R, untransform: (R) -> T): MutableProvider<R> {
        val provider = BidirectionalTransformingProvider(this, transform, untransform)
        addStrongChild(provider)
        return provider
    }
    
    override fun <R> map(transform: (T) -> R, untransform: (R) -> T): MutableProvider<R> {
        val provider = BidirectionalTransformingProvider(this, transform, untransform)
        addWeakChild(provider)
        return provider
    }
    
    override fun set(value: T) {
        update(DeferredValue.Direct(value))
    }
    
    /**
     * Attempts to update the value of this [MutableProvider] to [value].
     * Fails if the [DeferredValue.state] of [value] is less than the current state of this [MutableProvider]. Equal state succeeds.
     *
     * On success, returns `true` and updates the value while not notifying [ignore].
     */
    fun update(value: DeferredValue<T>, ignore: Provider<*>? = null): Boolean
    
}