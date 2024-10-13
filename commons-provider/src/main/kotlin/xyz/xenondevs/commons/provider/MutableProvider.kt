package xyz.xenondevs.commons.provider

import kotlin.reflect.KProperty

sealed interface MutableProvider<T> : Provider<T> {
    
    /**
     * Sets the value of this [MutableProvider] to [value].
     */
    fun set(value: T)
    
    operator fun <X> setValue(thisRef: X?, property: KProperty<*>, value: T) = set(value)
    
}