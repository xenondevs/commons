package xyz.xenondevs.commons.provider.mutable

import xyz.xenondevs.commons.provider.AbstractProvider
import xyz.xenondevs.commons.provider.Provider
import xyz.xenondevs.commons.provider.immutable.provider

/**
 * Creates a new [MutableProvider] that defaults to [value] if the value of [this][MutableProvider] is null.
 * The default value is propagated upwards when the value of the returned provider is loaded.
 */
fun <T : Any> MutableProvider<T?>.defaultsTo(value: T): MutableProvider<T> =
    MutableDefaultValueProvider(this, value)

/**
 * Creates a new [MutableProvider] that defaults to the value obtained through [provider] if the value of [this][MutableProvider] is null.
 * The default value is propagated upwards when the value of the returned provider is loaded.
 * Once the value has been propagated upwards, changes to the value of [provider] will be ignored.
 */
fun <T : Any> MutableProvider<T?>.defaultsTo(provider: Provider<T>): MutableProvider<T> =
    MutableDefaultProviderProvider(this, provider)

/**
 * Creates a new [MutableProvider] that defaults to the value obtained through the [lazyValue] lambda if the value of [this][MutableProvider] is null.
 * The default value is propagated upwards when the value of the returned provider is loaded.
 */
fun <T : Any> MutableProvider<T?>.defaultsToLazily(lazyValue: () -> T): MutableProvider<T> {
    // naming this function orElse would lead to a resolution ambiguity with defaultsTo(value: T)
    
    return defaultsTo(provider(lazyValue))
}

private abstract class MutableDefaultingProvider<T : Any>(
    protected val provider: MutableProvider<T?>,
) : AbstractProvider<T>() {
    
    override fun loadValue(): T {
        var value = provider.get()
        if (value == null)
            value = applyDefaultValue()
        
        return value
    }
    
    override fun set(value: T, ignoredChildren: Set<Provider<*>>) {
        super.set(value, ignoredChildren)
        provider.set(value, setOf(this))
    }
    
    protected abstract fun applyDefaultValue(): T
    
}

private class MutableDefaultValueProvider<T : Any>(
    provider: MutableProvider<T?>,
    private val defaultValue: T
) : MutableDefaultingProvider<T>(provider) {
    
    init {
        provider.addChild(this)
    }
    
    override fun applyDefaultValue(): T {
        provider.set(defaultValue, setOf(this))
        return defaultValue
    }
    
}

private class MutableDefaultProviderProvider<T : Any>(
    provider: MutableProvider<T?>,
    private val defaultProvider: Provider<T>
) : MutableDefaultingProvider<T>(provider) {
    
    init {
        provider.addChild(this)
    }
    
    override fun applyDefaultValue(): T {
        val defaultValue = defaultProvider.get()
        provider.set(defaultValue, setOf(this))
        return defaultValue
    }
    
}