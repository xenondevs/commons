package xyz.xenondevs.commons.provider.mutable

import xyz.xenondevs.commons.provider.Provider
import kotlin.reflect.KProperty

abstract class MutableProvider<T> : Provider<T>() {
    
    /**
     * Sets the value of this [MutableProvider] to [value].
     * The providers in [ignoredChildren] will not be updated.
     */
    fun set(value: T, ignoredChildren: Set<Provider<*>> = emptySet()) {
        set(value, updateChildren = true, callUpdateHandlers = true, ignoredChildren = ignoredChildren)
    }
    
    operator fun setValue(thisRef: Any, property: KProperty<*>, value: T) = set(value)
    
}