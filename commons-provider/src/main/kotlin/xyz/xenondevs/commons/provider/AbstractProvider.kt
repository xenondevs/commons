package xyz.xenondevs.commons.provider

import xyz.xenondevs.commons.provider.immutable.NullProvider.loadValue
import xyz.xenondevs.commons.provider.mutable.MutableProvider
import java.util.*

/**
 * The base [MutableProvider] implementation.
 */
abstract class AbstractProvider<T> : MutableProvider<T> {
    
    protected open var children: MutableSet<Provider<*>>? = null
    protected open var subscribers: MutableList<(T) -> Unit>? = null
    protected open var weakSubscribers: MutableMap<Any, MutableList<(Any, T) -> Unit>>? = null
    
    protected open var isInitialized = false
    protected open var value: T? = null
    
    /**
     * Retrieves the value of this [Provider].
     *
     * If the value has not been initialized yet, it will be loaded using [loadValue].
     */
    @Suppress("UNCHECKED_CAST")
    override fun get(): T {
        if (!isInitialized)
            set(loadValue(), updateChildren = false, callSubscribers = false)
        
        return value as T
    }
    
    override fun update() {
        // lazy approach: only load value if it is actually needed (i.e. subscribers are present),
        // otherwise just unset the value and load on next get() call
        if (subscribers.isNullOrEmpty() && weakSubscribers.isNullOrEmpty()) {
            isInitialized = false
            value = null
            children?.forEach { it.update() }
        } else {
            set(loadValue(), updateChildren = true, callSubscribers = true)
        }
    }
    
    override fun set(value: T, ignoredChildren: Set<Provider<*>>) {
        set(value, updateChildren = true, callSubscribers = true, ignoredChildren = ignoredChildren)
    }
    
    /**
     * Sets the backing value of this [Provider] to the given [value].
     *
     * @param updateChildren Whether the children of this [Provider] should be updated.
     * @param callSubscribers Whether the update handlers of this [Provider] should be called.
     * @param ignoredChildren A set of children that should not be updated.
     */
    private fun set(
        value: T,
        updateChildren: Boolean = true,
        callSubscribers: Boolean = true,
        ignoredChildren: Set<Provider<*>> = emptySet()
    ) {
        isInitialized = true
        if (this.value === value)
            return
        this.value = value
        
        val children = children
        if (updateChildren && children != null) {
            for (child in children) {
                if (child !in ignoredChildren)
                    child.update()
            }
        }
        
        if (callSubscribers) {
            subscribers?.forEach { it.invoke(value) }
            weakSubscribers?.forEach { (owner, subs) -> subs.forEach { it(owner, value) } }
        }
    }
    
    override fun addChild(provider: Provider<*>) {
        if (children == null)
            children = Collections.newSetFromMap(WeakHashMap(1))
        
        children!!.add(provider)
    }
    
    override fun subscribe(action: (T) -> Unit) {
        if (subscribers == null)
            subscribers = ArrayList(1)
        
        subscribers!!.add(action)
    }
    
    @Suppress("UNCHECKED_CAST")
    override fun <R : Any> subscribeWeak(owner: R, action: (R, T) -> Unit) {
        if (weakSubscribers == null)
            weakSubscribers = WeakHashMap(1)
        
        weakSubscribers!!
            .getOrPut(owner) { ArrayList(1) }
            .add(action as (Any, T) -> Unit)
    }
    
    override fun removeChild(provider: Provider<*>) {
        children?.remove(provider)
    }
    
    override fun unsubscribe(action: Function1<T, Unit>) {
        subscribers?.remove(action)
    }
    
    override fun <R : Any> unsubscribeWeak(owner: R, action: Function2<R, T, Unit>) {
        val weakSubscribers = weakSubscribers
            ?: return
        val list = weakSubscribers[owner]
            ?: return
     
        list.remove(action)
     
        if (list.isEmpty())
            weakSubscribers.remove(owner)
    }
    
    override fun <R : Any> unsubscribeWeak(owner: R) {
        weakSubscribers?.remove(owner)
    }
    
    protected abstract fun loadValue(): T
    
}