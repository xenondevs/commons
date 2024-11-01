@file:JvmName("Providers")
@file:JvmMultifileClass
@file:OptIn(UnstableProviderApi::class)

package xyz.xenondevs.commons.provider

import java.lang.ref.WeakReference
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * Creates a new [MutableProvider] that defaults to [value] if the value of [this][MutableProvider] is null.
 * The default value is propagated upwards when the value of the returned provider is loaded.
 */
fun <T : Any> MutableProvider<T?>.strongDefaultsTo(value: T): MutableProvider<T> =
    MutableDefaultValueProvider(this as AbstractProvider<T?>, value, false)

/**
 * Creates a new [MutableProvider] that defaults to [value] if the value of [this][MutableProvider] is null.
 * The default value is propagated upwards when the value of the returned provider is loaded.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
fun <T : Any> MutableProvider<T?>.defaultsTo(value: T): MutableProvider<T> =
    MutableDefaultValueProvider(this as AbstractProvider<T?>, value, true)

/**
 * Creates a new [MutableProvider] that defaults to the value obtained through [provider] if the value of [this][MutableProvider] is null.
 * The default value is propagated upwards when the value of the returned provider is loaded.
 * Once the value has been propagated upwards, changes to the value of [provider] will be ignored.
 */
fun <T : Any> MutableProvider<T?>.strongDefaultsTo(provider: Provider<T>): MutableProvider<T> =
    MutableDefaultProviderProvider(this as AbstractProvider<T?>, provider as AbstractProvider<T>, false)

/**
 * Creates a new [MutableProvider] that defaults to the value obtained through [provider] if the value of [this][MutableProvider] is null.
 * The default value is propagated upwards when the value of the returned provider is loaded.
 * Once the value has been propagated upwards, changes to the value of [provider] will be ignored.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
fun <T : Any> MutableProvider<T?>.defaultsTo(provider: Provider<T>): MutableProvider<T> =
    MutableDefaultProviderProvider(this as AbstractProvider<T?>, provider as AbstractProvider<T>, true)

/**
 * Creates a new [MutableProvider] that defaults to the value obtained through the [lazyValue] lambda if the value of [this][MutableProvider] is null.
 * The default value is propagated upwards when the value of the returned provider is loaded.
 * 
 * [lazyValue] should be a pure function.
 */
fun <T : Any> MutableProvider<T?>.strongDefaultsToLazily(lazyValue: () -> T): MutableProvider<T> =
    strongDefaultsTo(provider(lazyValue))

/**
 * Creates a new [MutableProvider] that defaults to the value obtained through the [lazyValue] lambda if the value of [this][MutableProvider] is null.
 * The default value is propagated upwards when the value of the returned provider is loaded.
 *
 * [lazyValue] should be a pure function.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
fun <T : Any> MutableProvider<T?>.defaultsToLazily(lazyValue: () -> T): MutableProvider<T> =
    defaultsTo(provider(lazyValue))

private class MutableDefaultValueProvider<T : Any>(
    private val parent: AbstractProvider<T?>,
    private val defaultValue: T,
    weak: Boolean
) : AbstractProvider<T>(parent.lock) {
    
    init {
        lock.withLock {
            addParent(parent) { it }
            parent.addChild(active = true, weak = weak, this)
        }
    }
    
    override fun pull(): T {
        var value = parent.get()
        if (value == null) {
            value = defaultValue
            parent.onChildChanged(this, { it }, ArrayList(0)) // fixme: does not fire subscribers
        }
        
        return value
    }
    
}

private class MutableDefaultProviderProvider<T : Any>(
    private val provider: AbstractProvider<T?>,
    private val defaultProvider: AbstractProvider<T>,
    weak: Boolean
) : AbstractProvider<T>(ReentrantLock()) {
    
    init {
        provider.changeLock(lock)
        defaultProvider.changeLock(lock)
        lock.withLock {
            addParent(provider) { it }
            addInactiveParent(defaultProvider)
            provider.addChild(active = true, weak = weak, child = this)
            defaultProvider.addChild(active = false, weak = weak, child = this)
        }
    }
    
    override fun pull(): T {
        var value = provider.get()
        if (value == null) {
            value = defaultProvider.get()
            provider.onChildChanged(this, { it }, ArrayList(0))
        }
        
        return value
    }
    
}