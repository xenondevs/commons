@file:Suppress("UNCHECKED_CAST")

package xyz.xenondevs.commons.provider

import java.util.*
import java.util.function.Supplier
import kotlin.collections.ArrayList
import kotlin.reflect.KProperty

abstract class Provider<T> : Supplier<T> {
    
    protected var children: MutableSet<Provider<*>>? = null
    private var updateHandlers: MutableList<(T) -> Unit>? = null
    
    private var isInitialized = false
    protected var value: T? = null
    
    /**
     * Retrieves the value of this [Provider].
     * 
     * If the value has not been initialized yet, it will be loaded using [loadValue].
     */
    override fun get(): T {
        if (!isInitialized)
            set(loadValue(), updateChildren = false, callUpdateHandlers = false)
        
        return value as T
    }
    
    /**
     * Sets the backing value of this [Provider] to the given [value].
     * 
     * @param updateChildren Whether the children of this [Provider] should be updated.
     * @param callUpdateHandlers Whether the update handlers of this [Provider] should be called.
     * @param ignoredChildren A set of children that should not be updated.
     */
    protected open fun set(
        value: T,
        updateChildren: Boolean = true,
        callUpdateHandlers: Boolean = true,
        ignoredChildren: Set<Provider<*>> = emptySet()
    ) {
        isInitialized = true
        if (this.value == value)
            return
        this.value = value
        
        val children = children
        if (updateChildren && children != null) {
            for (child in children) {
                if (child !in ignoredChildren)
                    child.update()
            }
        }
        
        if (callUpdateHandlers)
            updateHandlers?.forEach { it.invoke(value) }
    }
    
    /**
     * Updates the value of this [Provider] by calling [loadValue].
     */
    fun update() {
        set(loadValue(), updateChildren = true, callUpdateHandlers = true)
    }
    
    /**
     * Registers a child [Provider] that will be updated via [Provider.update] when the value of this [Provider] changes.
     */
    fun addChild(provider: Provider<*>) {
        if (children == null)
            children = Collections.newSetFromMap(WeakHashMap(1))
        
        children!!.add(provider)
    }
    
    /**
     * Removes a child [Provider] that was previously registered via [Provider.addChild].
     */
    fun removeChild(provider: Provider<*>) {
        children?.remove(provider)
    }
    
    /**
     * Registers an update handler that will be called when the value of this [Provider] changes.
     */
    fun addUpdateHandler(action: (T) -> Unit): Provider<T> {
        if (updateHandlers == null)
            updateHandlers = ArrayList(1)
        
        updateHandlers!!.add(action)
        
        return this
    }
    
    /**
     * Unregisters an update handler that was previously registered via [Provider.addUpdateHandler].
     */
    fun removeUpdateHandler(action: (T) -> Unit): Provider<T>{
        updateHandlers?.remove(action)
        return this
    }
    
    /**
     * Loads / computes the value of this [Provider].
     */
    protected abstract fun loadValue(): T
    
    operator fun getValue(thisRef: Any?, property: KProperty<*>?): T = get()
    
}