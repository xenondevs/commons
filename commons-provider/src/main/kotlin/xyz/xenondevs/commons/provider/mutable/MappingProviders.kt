package xyz.xenondevs.commons.provider.mutable

import xyz.xenondevs.commons.provider.AbstractProvider
import xyz.xenondevs.commons.provider.Provider

/**
 * Creates and returns a new [MutableProvider] that maps the value of [this][MutableProvider]
 * bi-directionally using the provided [transform] and [untransform] functions.
 */
fun <T, R> MutableProvider<T>.map(transform: (T) -> R, untransform: (R) -> T): MutableProvider<R> {
    val provider = MutableMappingProvider(this, transform, untransform)
    addChild(provider)
    return provider
}

/**
 * Creates and returns a new [MutableProvider] that maps non-null values of [this][MutableProvider]
 * bi-directionally using the provided [transform] and [untransform] functions.
 * Null values will be passed through without transformation.
 */
inline fun <T : Any, R : Any> MutableProvider<T?>.mapNonNull(
    crossinline transform: (T) -> R?,
    crossinline untransform: (R) -> T?
): MutableProvider<R?> = map({ it?.let(transform) }, { it?.let(untransform) })

private class MutableMappingProvider<T, R>(
    private val parent: MutableProvider<T>,
    private val transform: (T) -> R,
    private val untransform: (R) -> T
) : AbstractProvider<R>() {
    
    override fun loadValue(): R {
        return transform(parent.get())
    }
    
    override fun set(value: R, updateChildren: Boolean, callSubscribers: Boolean, ignoredChildren: Set<Provider<*>>) {
        super.set(value, updateChildren, callSubscribers, ignoredChildren)
        parent.set(untransform(value), setOf(this))
    }
    
}