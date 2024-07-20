package xyz.xenondevs.commons.provider.immutable

import xyz.xenondevs.commons.provider.Provider

/**
 * Creates and returns a new [Provider] that returns a fallback [value] if the value of [this][Provider] is null.
 */
fun <T> Provider<T?>.orElse(value: T): Provider<T> =
    map { it ?: value }

/**
 * Creates and returns a new [Provider] that returns a fallback value obtained through [provider] if the value of [this][Provider] is null.
 */
fun <T> Provider<T?>.orElse(provider: Provider<T>): Provider<T> {
    val result = map { it ?: provider.get() }
    provider.addChild(result)
    return result
}

/**
 * Creates and returns a new [Provider] that returns a fallback value obtained through [provider] if the value of [this][Provider] is null.
 */
fun <T> Provider<T?>.orElseLazily(lazyValue: () -> T): Provider<T> {
    // naming this function orElse would lead to a resolution ambiguity with orElse(value: T)
    
    return orElse(provider(lazyValue))
}