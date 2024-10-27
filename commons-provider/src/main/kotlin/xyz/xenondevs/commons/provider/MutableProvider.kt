package xyz.xenondevs.commons.provider

import kotlin.reflect.KProperty

/**
 * A [MutableProvider] is a [Provider] that allows [setting][set] the value.
 * 
 * Like [Provider], there are various extension functions available for modelling
 * data transformations, which are bidirectional in the case of [MutableProvider].
 */
sealed interface MutableProvider<T> : Provider<T> {
    
    /**
     * Sets the value of this [MutableProvider] to [value].
     */
    fun set(value: T)
    
    operator fun <X> setValue(thisRef: X?, property: KProperty<*>, value: T) = set(value)
    
}