@file:Suppress("UNCHECKED_CAST")

package xyz.xenondevs.commons.provider.mutable

import xyz.xenondevs.commons.provider.Provider

/**
 * Creates and returns a new [MutableProvider] that maps the value of [this][MutableProvider]
 * bi-directionally using the provided [transform] and [untransform] functions.
 */
fun <T : Any, R> MutableProvider<T>.map(transform: (T) -> R, untransform: (R) -> T): MutableProvider<R> {
    val provider = MutableMapEverythingProvider(this, transform, untransform)
    addChild(provider)
    return provider
}

/**
 * Creates and returns a new [MutableProvider] that maps all non-null values of [this][MutableProvider]
 * bi-directionally using the provided [transform] and [untransform] functions.
 * Null values will be passed through without transformation.
 */
fun <T, R> MutableProvider<T?>.mapNonNull(transform: (T & Any) -> R, untransform: (R & Any) -> T & Any): MutableProvider<R?> {
    val provider = MutableMapNonNullProvider(this, transform, untransform)
    addChild(provider)
    return provider
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