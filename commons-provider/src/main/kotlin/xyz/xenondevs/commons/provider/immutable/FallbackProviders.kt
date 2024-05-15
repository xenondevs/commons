package xyz.xenondevs.commons.provider.immutable

import xyz.xenondevs.commons.provider.Provider

/**
 * Creates and returns a new [Provider] that returns a fallback [value] if the value of [this][Provider] is null.
 */
fun <T> Provider<T?>.orElse(value: T): Provider<T> {
    val provider = FallbackValueProvider(this, value)
    addChild(provider)
    return provider
}

/**
 * Creates and returns a new [Provider] that returns a fallback value obtained through [provider] if the value of [this][Provider] is null.
 */
fun <T> Provider<T?>.orElse(provider: Provider<T>): Provider<T> {
    val result = FallbackProviderProvider(this, provider)
    provider.addChild(result)
    addChild(result)
    return result
}

/**
 * Creates and returns a new [Provider] that returns a fallback value obtained through [provider] if the value of [this][Provider] is null.
 */
fun <T> Provider<T?>.orElseLazily(lazyValue: () -> T): Provider<T> {
    // naming this function orElse would lead to a resolution ambiguity with orElse(value: T)
    
    return orElse(provider(lazyValue))
}

private class FallbackValueProvider<T>(
    private val provider: Provider<T?>,
    private val fallback: T
) : Provider<T>() {
    override fun loadValue(): T {
        return provider.get() ?: fallback
    }
}

private class FallbackProviderProvider<T>(
    private val provider: Provider<T?>,
    private val fallback: Provider<T>
) : Provider<T>() {
    override fun loadValue(): T {
        return (provider.get() ?: fallback.get())
    }
}