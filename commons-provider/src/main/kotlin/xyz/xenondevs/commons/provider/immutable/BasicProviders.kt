package xyz.xenondevs.commons.provider.immutable

import xyz.xenondevs.commons.provider.AbstractProvider
import xyz.xenondevs.commons.provider.Provider

/**
 * Creates a new [Provider] that loads its value using the given [loadValue] function.
 */
fun <T> provider(loadValue: () -> T): Provider<T> =
    LambdaProvider(loadValue)

/**
 * Creates a new [Provider] with the given [value].
 */
@Suppress("UNCHECKED_CAST")
fun <T> provider(value: T): Provider<T> {
    if (value == null)
        return NullProvider as Provider<T>
    
    return ProviderWrapper(value)
}

private class LambdaProvider<T>(private val loader: () -> T) : AbstractProvider<T>() {
    
    override fun loadValue(): T {
        return loader()
    }
    
}

private class ProviderWrapper<T>(private val staticValue: T) : AbstractProvider<T>() {
    
    override fun loadValue(): T {
        return staticValue
    }
    
}

/**
 * A [Provider] that always returns `null`.
 */
object NullProvider : AbstractProvider<Any?>() {
    
    override fun loadValue(): Any? {
        return null
    }
    
}