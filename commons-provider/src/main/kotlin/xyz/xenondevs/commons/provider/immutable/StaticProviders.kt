package xyz.xenondevs.commons.provider.immutable

import xyz.xenondevs.commons.provider.Provider

fun <T> provider(value: T): Provider<T> =
    ProviderWrapper(value)

internal class ProviderWrapper<T>(private val staticValue: T) : Provider<T>() {
    
    override fun loadValue(): T {
        return staticValue
    }
    
}

object NullProvider : Provider<Any?>() {
    
    override fun loadValue(): Any? {
        return null
    }
    
}