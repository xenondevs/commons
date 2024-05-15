package xyz.xenondevs.commons.provider.immutable

import xyz.xenondevs.commons.provider.Provider

/**
 * Creates a new [Provider] that loads its value using the given [loadValue] function.
 */
@JvmName("providerLambda")
fun <T> provider(loadValue: () -> T): Provider<T> =
    LambdaProvider(loadValue)

/**
 * Creates a new [Provider] with the given [value].
 */
@Suppress("UNCHECKED_CAST")
@JvmName("providerStatic")
fun <T> provider(value: T): Provider<T> {
    if (value == null)
        return NullProvider as Provider<T>
    
    return ProviderWrapper(value)
}

private class LambdaProvider<T>(private val loader: () -> T) : Provider<T>() {
    
    override fun loadValue(): T {
        return loader()
    }
    
}

private class ProviderWrapper<T>(private val staticValue: T) : Provider<T>() {
    
    override fun loadValue(): T {
        return staticValue
    }
    
}

/**
 * A [Provider] that always returns `null`.
 */
object NullProvider : Provider<Any?>() {
    
    override fun loadValue(): Any? {
        return null
    }
    
}