@file:Suppress("UNCHECKED_CAST")

package xyz.xenondevs.commons.provider

import java.util.*
import kotlin.collections.ArrayList
import kotlin.reflect.KProperty

@Suppress("PropertyName")
abstract class Provider<T> {
    
    protected var children: MutableSet<Provider<*>>? = null
    private var updateHandlers: MutableList<(T) -> Unit>? = null
    
    private var initialized = false
    protected var _value: T? = null
        set(value) {
            field = value
            initialized = true
        }
    
    val value: T
        get() {
            if (!initialized) {
                _value = loadValue()
            }
            
            return _value as T
        }
    
    fun update() {
        val value = loadValue()
        _value = value
        updateHandlers?.forEach { it.invoke(value) }
        children?.forEach(Provider<*>::update)
    }
    
    fun addChild(provider: Provider<*>) {
        if (children == null)
            children = Collections.newSetFromMap(WeakHashMap())
        
        children!!.add(provider)
    }
    
    fun removeChild(provider: Provider<*>) {
        children?.remove(provider)
    }
    
    fun handleUpdate(action: (T) -> Unit): Provider<T> {
        if (updateHandlers == null)
            updateHandlers = ArrayList(1)
        
        updateHandlers!!.add(action)
        
        return this
    }
    
    operator fun getValue(thisRef: Any?, property: KProperty<*>?): T = value
    
    protected abstract fun loadValue(): T
    
}