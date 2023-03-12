package xyz.xenondevs.commons.provider.mutable

import xyz.xenondevs.commons.provider.Provider

fun <T> mutableProvider(initialValue: T): MutableProvider<T> =
    MutableProviderWrapper(initialValue)

private class MutableProviderWrapper<T>(private val initialValue: T) : MutableProvider<T>() {
    
    override fun setValue(value: T) {
        this._value = value
        children?.forEach(Provider<*>::update)
    }
    
    override fun loadValue(): T {
        return initialValue
    }
    
}