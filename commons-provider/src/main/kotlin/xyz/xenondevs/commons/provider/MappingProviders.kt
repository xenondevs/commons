@file:JvmName("Providers")
@file:JvmMultifileClass
@file:OptIn(UnstableProviderApi::class)

package xyz.xenondevs.commons.provider

import java.lang.ref.WeakReference
import kotlin.concurrent.withLock

/**
 * Creates and returns a new [Provider] that maps the value of [this][Provider]
 * using the [transform] function.
 * 
 * [transform] should be a pure function.
 */
fun <T, R> Provider<T>.strongMap(transform: (T) -> R): Provider<R> =
    MappingProvider(this as AbstractProvider<T>, transform, false)

/**
 * Creates and returns a new [Provider] that maps the value of [this][Provider]
 * using the [transform] function.
 *
 * [transform] should be a pure function.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
fun <T, R> Provider<T>.map(transform: (T) -> R): Provider<R> =
    MappingProvider(this as AbstractProvider<T>, transform, true)

/**
 * Creates and returns a new [Provider] that maps to the value of the [Provider]
 * returned by [transform].
 *
 * [transform] should be a pure function.
 * 
 * Note that this function registers a subscriber on [this][Provider] and as such disables lazy evaluation of [this][Provider].
 */
@Suppress("UNCHECKED_CAST")
fun <T, R> Provider<T>.strongFlatMap(transform: (T) -> Provider<R>): Provider<R> =
    FlatMappingProvider(this as AbstractProvider<T>, transform as (T) -> AbstractProvider<R>, false)

/**
 * Creates and returns a new [Provider] that maps to the value of the [Provider]
 * returned by [transform].
 *
 * [transform] should be a pure function.
 * 
 * The returned provider will only be stored in a [WeakReference] in the parent providers
 * ([this][MutableProvider] and the result of [transform]).
 */
@Suppress("UNCHECKED_CAST")
fun <T, R> Provider<T>.flatMap(transform: (T) -> Provider<R>): Provider<R> =
    FlatMappingProvider(this as AbstractProvider<T>, transform as (T) -> AbstractProvider<R>, true)

/**
 * Creates and returns a new [Provider] that maps to the value of the [MutableProvider]
 * returned by [transform].
 *
 * [transform] should be a pure function.
 * 
 * Note that this function registers a subscriber on [this][Provider] and as such disables lazy evaluation of [this][Provider].
 */
@Suppress("UNCHECKED_CAST")
fun <T, R> Provider<T>.strongFlatMapMutable(transform: (T) -> MutableProvider<R>): MutableProvider<R> =
    FlatMappingProvider(this as AbstractProvider<T>, transform as (T) -> AbstractProvider<R>, false)

/**
 * Creates and returns a new [Provider] that maps to the value of the [MutableProvider]
 * returned by [transform].
 *
 * [transform] should be a pure function.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent providers
 * ([this][MutableProvider] and the result of [transform]).
 */
@Suppress("UNCHECKED_CAST")
fun <T, R> Provider<T>.flatMapMutable(transform: (T) -> MutableProvider<R>): MutableProvider<R> =
    FlatMappingProvider(this as AbstractProvider<T>, transform as (T) -> AbstractProvider<R>, true)

/**
 * Creates and returns a new [Provider] that maps non-null values of [this][Provider]
 * using the [transform] function.
 * Null values will be passed through without transformation.
 *
 * [transform] should be a pure function.
 */
inline fun <T : Any, R> Provider<T?>.strongMapNonNull(crossinline transform: (T) -> R): Provider<R?> =
    strongMap { it?.let(transform) }

/**
 * Creates and returns a new [Provider] that maps non-null values of [this][Provider]
 * using the [transform] function.
 * Null values will be passed through without transformation.
 *
 * [transform] should be a pure function.
 * 
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
inline fun <T : Any, R> Provider<T?>.mapNonNull(crossinline transform: (T) -> R): Provider<R?> =
    map { it?.let(transform) }

private class MappingProvider<P, T>(
    private val parent: AbstractProvider<P>,
    private val transform: (P) -> T,
    weak: Boolean
) : AbstractProvider<T>(parent.lock) {
    
    init {
        lock.withLock {
            addInactiveParent(parent)
            parent.addChild(active = true, weak = weak, child = this)
        }
    }
    
    override fun pull(): T {
        return transform(parent.get())
    }
    
}

private class FlatMappingProvider<P, T>(
    private val parent: AbstractProvider<P>,
    private val transform: (P) -> AbstractProvider<T>,
    private val weak: Boolean
) : AbstractProvider<T>(parent.lock) {
    
    private var provider: AbstractProvider<T>
    
    init {
        lock.withLock {
            addInactiveParent(parent)
            parent.addChild(active = true, weak = weak, child = this)
            
            provider = transform(parent.get())
            provider.changeLock(lock)
            provider.addChild(active = true, weak = weak, child = this)
            addParent(provider, ignored = setOf(parent)) { it }
            
            parent.subscribeWeakImmediate(this) { thisRef, parentValue ->
                thisRef.handleProviderUpdate(transform(parentValue))
            }
        }
    }
    
    private fun handleProviderUpdate(new: AbstractProvider<T>) {
        if (new == provider)
            return
        
        provider.removeChild(active = true, weak = weak, child = this)
        removeParent(provider)
        
        provider = new
        provider.changeLock(lock)
        provider.addChild(active = true, weak = weak, child = this)
        addParent(provider, ignored = setOf(parent)) { it }
    }
    
    override fun pull(): T {
        return provider.get()
    }
    
}

/**
 * Creates and returns a new [MutableProvider] that maps the value of [this][MutableProvider]
 * bi-directionally using the provided [transform] and [untransform] functions.
 * 
 * [transform] and [untransform] should be pure functions.
 */
fun <T, R> MutableProvider<T>.strongMap(transform: (T) -> R, untransform: (R) -> T): MutableProvider<R> =
    MutableMappingProvider(this as AbstractProvider<T>, transform, untransform, false)

/**
 * Creates and returns a new [MutableProvider] that maps the value of [this][MutableProvider]
 * bi-directionally using the provided [transform] and [untransform] functions.
 *
 * [transform] and [untransform] should be pure functions.
 * 
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
fun <T, R> MutableProvider<T>.map(transform: (T) -> R, untransform: (R) -> T): MutableProvider<R> =
    MutableMappingProvider(this as AbstractProvider<T>, transform, untransform, true)

/**
 * Creates and returns a new [MutableProvider] that maps non-null values of [this][MutableProvider]
 * bi-directionally using the provided [transform] and [untransform] functions.
 * Null values will be passed through without transformation.
 *
 * [transform] and [untransform] should be pure functions.
 */
inline fun <T : Any, R : Any> MutableProvider<T?>.strongMapNonNull(
    crossinline transform: (T) -> R?,
    crossinline untransform: (R) -> T?
): MutableProvider<R?> = strongMap({ it?.let(transform) }, { it?.let(untransform) })

/**
 * Creates and returns a new [MutableProvider] that maps non-null values of [this][MutableProvider]
 * bi-directionally using the provided [transform] and [untransform] functions.
 * Null values will be passed through without transformation.
 *
 * [transform] and [untransform] should be pure functions.
 * 
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
inline fun <T : Any, R : Any> MutableProvider<T?>.mapNonNull(
    crossinline transform: (T) -> R?,
    crossinline untransform: (R) -> T?
): MutableProvider<R?> = map({ it?.let(transform) }, { it?.let(untransform) })

private class MutableMappingProvider<P, T>(
    private val parent: AbstractProvider<P>,
    private val transform: (P) -> T,
    private val untransform: (T) -> P,
    weak: Boolean
) : AbstractProvider<T>(parent.lock) {
    
    init {
        lock.withLock {
            addParent(parent) { untransform(it) }
            parent.addChild(active = true, weak = weak, child = this)
        }
    }
    
    override fun pull(): T {
        return transform(parent.get())
    }
    
}