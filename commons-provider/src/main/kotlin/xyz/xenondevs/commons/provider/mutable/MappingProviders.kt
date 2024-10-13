@file:OptIn(UnstableProviderApi::class)

package xyz.xenondevs.commons.provider.mutable

import xyz.xenondevs.commons.provider.AbstractProvider
import xyz.xenondevs.commons.provider.MutableProvider
import xyz.xenondevs.commons.provider.UnstableProviderApi
import kotlin.concurrent.withLock

/**
 * Creates and returns a new [MutableProvider] that maps the value of [this][MutableProvider]
 * bi-directionally using the provided [transform] and [untransform] functions.
 */
fun <T, R> MutableProvider<T>.map(transform: (T) -> R, untransform: (R) -> T): MutableProvider<R> =
    MutableMappingProvider(this as AbstractProvider<T>, transform, untransform)

/**
 * Creates and returns a new [MutableProvider] that maps non-null values of [this][MutableProvider]
 * bi-directionally using the provided [transform] and [untransform] functions.
 * Null values will be passed through without transformation.
 */
inline fun <T : Any, R : Any> MutableProvider<T?>.mapNonNull(
    crossinline transform: (T) -> R?,
    crossinline untransform: (R) -> T?
): MutableProvider<R?> = map({ it?.let(transform) }, { it?.let(untransform) })

private class MutableMappingProvider<P, T>(
    private val parent: AbstractProvider<P>,
    private val transform: (P) -> T,
    private val untransform: (T) -> P
) : AbstractProvider<T>(parent.lock) {
    
    init {
        lock.withLock {
            addParent(parent) { untransform(it) }
            parent.addChild(this)
        }
    }
    
    override fun pull(): T {
        return transform(parent.get())
    }
    
}