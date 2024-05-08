@file:Suppress("UNCHECKED_CAST")

package xyz.xenondevs.commons.provider.mutable

import xyz.xenondevs.commons.provider.Provider
import xyz.xenondevs.commons.provider.immutable.provider

fun <T : Any, R> MutableProvider<T>.map(transform: (T) -> R, untransform: (R) -> T): MutableProvider<R> {
    val provider = MutableMapEverythingProvider(this, transform, untransform)
    addChild(provider)
    return provider
}

fun <T, R> MutableProvider<T?>.mapNonNull(transform: (T & Any) -> R, untransform: (R & Any) -> T & Any): MutableProvider<R?> {
    val provider = MutableMapNonNullProvider(this, transform, untransform)
    addChild(provider)
    return provider
}

fun <T> MutableProvider<T?>.orElse(value: T): MutableProvider<T> {
    val provider = MutableFallbackValueProvider(this, value)
    addChild(provider)
    return provider
}

// naming this function orElse would lead to a resolution ambiguity with orElse(value: T)
fun <T : Any> MutableProvider<T?>.orElseLazily(lazyValue: () -> T): MutableProvider<T> {
    return orElse(provider(lazyValue))
}

fun <T : Any> MutableProvider<T?>.orElse(provider: Provider<T>): MutableProvider<T> {
    val result = MutableFallbackProviderProvider(this, provider)
    provider.addChild(result)
    addChild(result)
    return result
}

fun <T> MutableProvider<T?>.orElse(provider: MutableProvider<T?>): MutableProvider<T?> {
    val result = MutableNullableFallbackProviderProvider(this, provider)
    provider.addChild(result)
    addChild(result)
    return result
}

private class MutableMapEverythingProvider<T, R>(
    private val parent: MutableProvider<T>,
    private val transform: (T) -> R,
    private val untransform: (R) -> T
) : MutableProvider<R>() {
    
    override fun loadValue(): R {
        return transform(parent.get())
    }
    
    override fun set(value: R, updateChildren: Boolean, callUpdateHandlers: Boolean, ignoredChildren: Set<Provider<*>>) {
        super.set(value, updateChildren, callUpdateHandlers, ignoredChildren)
        parent.set(untransform(value), setOf(this))
    }
    
}

private class MutableMapNonNullProvider<T, R>(
    private val provider: MutableProvider<T>,
    private val transform: (T & Any) -> R,
    private val untransform: (R & Any) -> T & Any
) : MutableProvider<R?>() {
    
    override fun loadValue(): R? {
        return provider.get()?.let(transform)
    }
    
    override fun set(value: R?, updateChildren: Boolean, callUpdateHandlers: Boolean, ignoredChildren: Set<Provider<*>>) {
        super.set(value, updateChildren, callUpdateHandlers, ignoredChildren)
        provider.set(value?.let(untransform) as T, setOf(this))
    }
    
}

private class MutableFallbackValueProvider<T>(
    private val provider: MutableProvider<T?>,
    private val fallback: T
) : MutableProvider<T>() {
    
    override fun loadValue(): T {
        return provider.get() ?: fallback
    }
    
    override fun set(value: T, updateChildren: Boolean, callUpdateHandlers: Boolean, ignoredChildren: Set<Provider<*>>) {
        super.set(value, updateChildren, callUpdateHandlers, ignoredChildren)
        provider.set((if (value == fallback) null else value) as T, setOf(this))
    }
    
}

private class MutableFallbackProviderProvider<T>(
    private val provider: MutableProvider<T?>,
    private val fallback: Provider<T & Any>
) : MutableProvider<T & Any>() {
    
    override fun loadValue(): T & Any {
        return provider.get() ?: fallback.get()
    }
    
    override fun set(value: T & Any, updateChildren: Boolean, callUpdateHandlers: Boolean, ignoredChildren: Set<Provider<*>>) {
        super.set(value, updateChildren, callUpdateHandlers, ignoredChildren)
        provider.set((if (value == fallback.get()) null else value) as T, setOf(this))
    }
    
}

private class MutableNullableFallbackProviderProvider<T>(
    private val provider: MutableProvider<T?>,
    private val fallback: MutableProvider<T?>
) : MutableProvider<T?>() {
    
    override fun loadValue(): T? {
        return provider.get() ?: fallback.get()
    }
    
    override fun set(value: T?, updateChildren: Boolean, callUpdateHandlers: Boolean, ignoredChildren: Set<Provider<*>>) {
        super.set(value, updateChildren, callUpdateHandlers, ignoredChildren)
        
        val fallbackValue = fallback.get()
        when (value) {
            fallbackValue -> provider.set(null, setOf(this))
            null -> {
                provider.set(null, setOf(this))
                fallback.set(null, setOf(this))
            }
            else -> provider.set(value, setOf(this))
        }
    }
    
}
