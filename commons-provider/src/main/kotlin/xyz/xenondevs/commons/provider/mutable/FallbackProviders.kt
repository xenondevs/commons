@file:Suppress("UNCHECKED_CAST")

package xyz.xenondevs.commons.provider.mutable

import xyz.xenondevs.commons.provider.AbstractProvider
import xyz.xenondevs.commons.provider.Provider
import xyz.xenondevs.commons.provider.immutable.provider

/**
 * Creates a new [MutableProvider] that returns a fallback [value] if the value of [this][MutableProvider] is null.
 * Conversely, if the returned provider's value is set to [value], the value of [this][MutableProvider] will be set to null.
 *
 * This function is fundamentally different from [MutableProvider.defaultsTo] as it does not pass the default value upwards.
 */
fun <T> MutableProvider<T?>.orElse(value: T): MutableProvider<T> {
    val provider = MutableFallbackValueProvider(this, value)
    addChild(provider)
    return provider
}

/**
 * Creates a new [MutableProvider] that returns a fallback value obtained through [provider] if the value of [this][MutableProvider] is null.
 * Conversely, if the returned provider's value is set to the value obtained through [provider], the value of [this][MutableProvider] will be set to null.
 *
 * This function is fundamentally different from [MutableProvider.defaultsTo] as it does not pass the default value upwards.
 */
fun <T : Any> MutableProvider<T?>.orElse(provider: Provider<T>): MutableProvider<T> {
    val result = MutableFallbackProviderProvider(this, provider)
    provider.addChild(result)
    addChild(result)
    return result
}

/**
 * Creates a new [MutableProvider] that returns a fallback value obtained through the [lazyValue] lambda if the value of [this][MutableProvider] is null.
 * Conversely, if the returned provider's value is set to the value obtained through [lazyValue], the value of [this][MutableProvider] will be set to null.
 *
 * This function is fundamentally different from [MutableProvider.defaultsToLazily] as it does not pass the default value upwards.
 */
fun <T : Any> MutableProvider<T?>.orElseLazily(lazyValue: () -> T): MutableProvider<T> {
    // naming this function orElse would lead to a resolution ambiguity with orElse(value: T)
    
    return orElse(provider(lazyValue))
}

/**
 * Creates a new [MutableProvider] that returns a fallback value obtained through [provider] if the value of [this][MutableProvider] is null.
 * Conversely, if the returned provider's value is set to the value obtained through [provider], the value of [this][MutableProvider] will be set to null.
 *
 * If the value of the returned provider is set to null, both [this][MutableProvider] and [provider] will be set to null.
 */
fun <T> MutableProvider<T?>.orElse(provider: MutableProvider<T?>): MutableProvider<T?> {
    val result = MutableNullableFallbackProviderProvider(this, provider)
    provider.addChild(result)
    addChild(result)
    return result
}


private class MutableFallbackValueProvider<T>(
    private val provider: MutableProvider<T?>,
    private val fallback: T
) : AbstractProvider<T>() {
    
    override fun loadValue(): T {
        return provider.get() ?: fallback
    }
    
    override fun set(value: T, updateChildren: Boolean, callSubscribers: Boolean, ignoredChildren: Set<Provider<*>>) {
        super.set(value, updateChildren, callSubscribers, ignoredChildren)
        provider.set((if (value == fallback) null else value) as T, setOf(this))
    }
    
}

private class MutableFallbackProviderProvider<T : Any>(
    private val provider: MutableProvider<T?>,
    private val fallback: Provider<T>
) : AbstractProvider<T>() {
    
    override fun loadValue(): T {
        return provider.get() ?: fallback.get()
    }
    
    override fun set(value: T, updateChildren: Boolean, callSubscribers: Boolean, ignoredChildren: Set<Provider<*>>) {
        super.set(value, updateChildren, callSubscribers, ignoredChildren)
        provider.set(if (value == fallback.get()) null else value, setOf(this))
    }
    
}

private class MutableNullableFallbackProviderProvider<T>(
    private val provider: MutableProvider<T?>,
    private val fallback: MutableProvider<T?>
) : AbstractProvider<T?>() {
    
    override fun loadValue(): T? {
        return provider.get() ?: fallback.get()
    }
    
    override fun set(value: T?, updateChildren: Boolean, callSubscribers: Boolean, ignoredChildren: Set<Provider<*>>) {
        super.set(value, updateChildren, callSubscribers, ignoredChildren)
        
        val fallbackValue = fallback.get()
        when (value) {
            fallbackValue -> provider.set(null, ignoredChildren = setOf(this))
            null -> {
                provider.set(null, setOf(this))
                fallback.set(null, setOf(this))
            }
            else -> provider.set(value, setOf(this))
        }
    }
    
}