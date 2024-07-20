package xyz.xenondevs.commons.provider.mutable

import xyz.xenondevs.commons.provider.Provider
import kotlin.reflect.KProperty

/**
 * A [MutableProvider] is a variant of [Provider] that allows setting the value.
 * Due to this, [MutableProviders][MutableProvider] need to propagate changes bi-directionally, both upwards and downwards.
 */
interface MutableProvider<T> : Provider<T> {
    
    /**
     * Sets the value of this [MutableProvider] to [value].
     */
    fun set(value: T, ignoredChildren: Set<Provider<*>> = emptySet())
    
    operator fun <X> setValue(thisRef: X?, property: KProperty<*>, value: T) = set(value)
    
}