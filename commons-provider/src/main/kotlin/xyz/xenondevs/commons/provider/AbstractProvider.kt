package xyz.xenondevs.commons.provider

import xyz.xenondevs.commons.provider.immutable.NullProvider.loadValue
import xyz.xenondevs.commons.provider.mutable.MutableProvider
import java.util.*

/**
 * The base [MutableProvider] implementation.
 */
abstract class AbstractProvider<T> : MutableProvider<T> {
    
    protected var children: MutableSet<Provider<*>>? = null
    private var subscribers: MutableList<(T) -> Unit>? = null
    
    private var isInitialized = false
    protected var value: T? = null
    
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
        if (subscribers.isNullOrEmpty()) {
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
    protected open fun set(
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
        
        if (callSubscribers)
            subscribers?.forEach { it.invoke(value) }
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
    
    protected abstract fun loadValue(): T
    
}