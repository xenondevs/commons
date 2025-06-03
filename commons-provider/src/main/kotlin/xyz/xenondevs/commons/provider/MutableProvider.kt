package xyz.xenondevs.commons.provider

import java.lang.ref.WeakReference
import kotlin.reflect.KProperty

/**
 * A [MutableProvider] is a [Provider] that allows [setting][set] the value.
 */
sealed interface MutableProvider<T> : Provider<T> {
    
    /**
     * Creates and returns a new [MutableProvider] that maps the value of [this][MutableProvider]
     * bi-directionally using the provided [transform] and [untransform] functions.
     *
     * [transform] and [untransform] should be pure functions.
     */
    fun <R> strongMap(transform: (T) -> R, untransform: (R) -> T): MutableProvider<R>
    
    /**
     * Creates and returns a new [MutableProvider] that maps the value of [this][MutableProvider]
     * bi-directionally using the provided [transform] and [untransform] functions.
     *
     * [transform] and [untransform] should be pure functions.
     *
     * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
     */
    fun <R> map(transform: (T) -> R, untransform: (R) -> T): MutableProvider<R>
    
    /**
     * Sets the value of this [MutableProvider] to [value].
     */
    fun set(value: T)
    
    operator fun <X> setValue(thisRef: X?, property: KProperty<*>, value: T) = set(value)
    
}