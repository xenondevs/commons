@file:OptIn(UnstableProviderApi::class)

package xyz.xenondevs.commons.provider

import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * Creates a new [MutableProvider] that defaults to [value] if the value of [this][MutableProvider] is null.
 * The default value is propagated upwards when the value of the returned provider is loaded.
 */
fun <T : Any> MutableProvider<T?>.defaultsTo(value: T): MutableProvider<T> =
    MutableDefaultValueProvider(this as AbstractProvider<T?>, value)

/**
 * Creates a new [MutableProvider] that defaults to the value obtained through [provider] if the value of [this][MutableProvider] is null.
 * The default value is propagated upwards when the value of the returned provider is loaded.
 * Once the value has been propagated upwards, changes to the value of [provider] will be ignored.
 */
fun <T : Any> MutableProvider<T?>.defaultsTo(provider: Provider<T>): MutableProvider<T> =
    MutableDefaultProviderProvider(this as AbstractProvider<T?>, provider as AbstractProvider<T>)

/**
 * Creates a new [MutableProvider] that defaults to the value obtained through the [lazyValue] lambda if the value of [this][MutableProvider] is null.
 * The default value is propagated upwards when the value of the returned provider is loaded.
 */
fun <T : Any> MutableProvider<T?>.defaultsToLazily(lazyValue: () -> T): MutableProvider<T> = // naming this function orElse would lead to a resolution ambiguity with defaultsTo(value: T)
    defaultsTo(provider(lazyValue))

private class MutableDefaultValueProvider<T : Any>(
    private val parent: AbstractProvider<T?>,
    private val defaultValue: T
) : AbstractProvider<T>(parent.lock) {
    
    init {
        lock.withLock {
            addParent(parent) { it }
            parent.addChild(this)
        }
    }
    
    override fun pull(): T {
        var value = parent.get()
        if (value == null) {
            value = defaultValue
            parent.onChildChanged(this) { it }
        }
        
        return value
    }
    
}

private class MutableDefaultProviderProvider<T : Any>(
    private val provider: AbstractProvider<T?>,
    private val defaultProvider: AbstractProvider<T>
) : AbstractProvider<T>(ReentrantLock()) {
    
    init {
        provider.changeLock(lock)
        defaultProvider.changeLock(lock)
        lock.withLock {
            addParent(provider) { it }
            addInactiveParent(defaultProvider)
            provider.addChild(this)
            defaultProvider.addInactiveChild(this)
        }
    }
    
    override fun pull(): T {
        var value = provider.get()
        if (value == null) {
            value = defaultProvider.get()
            provider.onChildChanged(this) { it }
        }
        
        return value
    }
    
}