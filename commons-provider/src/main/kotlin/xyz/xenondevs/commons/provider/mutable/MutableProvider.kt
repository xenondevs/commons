package xyz.xenondevs.commons.provider.mutable

import xyz.xenondevs.commons.provider.Provider
import kotlin.reflect.KProperty

abstract class MutableProvider<T> : Provider<T>() {
    
    abstract fun setValue(value: T)
    
    operator fun setValue(thisRef: Any, property: KProperty<*>, value: T) = setValue(value)
    
}