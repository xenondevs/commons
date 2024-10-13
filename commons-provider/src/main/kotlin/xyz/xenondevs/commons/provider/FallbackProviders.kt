@file:OptIn(UnstableProviderApi::class)

package xyz.xenondevs.commons.provider

import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * Creates and returns a new [Provider] that returns a fallback [value] if the value of [this][Provider] is null.
 */
fun <T> Provider<T?>.orElse(value: T): Provider<T> =
    map { it ?: value }

/**
 * Creates and returns a new [Provider] that returns a fallback value obtained through [provider] if the value of [this][Provider] is null.
 */
fun <T> Provider<T?>.orElse(provider: Provider<T>): Provider<T> {
    this as AbstractProvider<T?>
    provider as AbstractProvider<T>

    provider.changeLock(lock)
    lock.withLock {
        val orElse = map { it ?: provider.get() } as AbstractProvider<T>
        orElse.addInactiveParent(provider)
        provider.addChild(orElse)
        return orElse
    }
}

/**
 * Creates and returns a new [Provider] that returns a fallback value obtained through [provider] if the value of [this][Provider] is null.
 */
fun <T> Provider<T?>.orElseLazily(lazyValue: () -> T): Provider<T> = // naming this function orElse would lead to a resolution ambiguity with orElse(value: T)
    orElse(provider(lazyValue))

/**
 * Creates a new [Provider] that returns a fallback value which is re-created through the [newValue] lambda every time the value of [this][Provider] is set to null.
 */
fun <T> Provider<T?>.orElseNew(newValue: () -> T): Provider<T> =
    map { it ?: newValue() }

/**
 * Creates a new [MutableProvider] that returns a fallback [value] if the value of [this][MutableProvider] is null.
 * Conversely, if the returned provider's value is set a value equal to [value], the value of [this][MutableProvider] will be set to null.
 *
 * This function is fundamentally different from [MutableProvider.defaultsTo] as it does not pass the default value upwards.
 */
fun <T> MutableProvider<T?>.orElse(value: T): MutableProvider<T> =
    MutableFallbackValueProvider(this as AbstractProvider<T?>, value)

/**
 * Creates a new [MutableProvider] that returns a fallback value obtained through [provider] if the value of [this][MutableProvider] is null.
 * Conversely, if the returned provider's value is set to a value equal to the one obtained through [provider], the value of [this][MutableProvider] will be set to null.
 *
 * This function is fundamentally different from [MutableProvider.defaultsTo] as it does not pass the default value upwards.
 */
fun <T : Any> MutableProvider<T?>.orElse(provider: Provider<T>): MutableProvider<T> =
    MutableFallbackProviderProvider(this as AbstractProvider<T?>, provider as AbstractProvider<T>)

/**
 * Creates a new [MutableProvider] that returns a fallback value obtained through the [lazyValue] lambda if the value of [this][MutableProvider] is null.
 * Conversely, if the returned provider's value is set to a value equal to the one obtained through [lazyValue], the value of [this][MutableProvider] will be set to null.
 *
 * This function is fundamentally different from [MutableProvider.defaultsToLazily] as it does not pass the default value upwards.
 */
fun <T : Any> MutableProvider<T?>.orElseLazily(lazyValue: () -> T): MutableProvider<T> = // naming this function orElse would lead to a resolution ambiguity with orElse(value: T)
    orElse(provider(lazyValue))

/**
 * Creates a new [MutableProvider] that returns a fallback value which is re-created through the [newValue] lambda every time the value of [this][MutableProvider] is set to null.
 * Conversely, if the returned provider's value is set to a value equal to one returned by [newValue], the value of [this][MutableProvider] will be set to null.
 *
 * For mutable data types, it is required that the [newValue] lambda returns a new instance every time it is called, and that all of those instances are [equal][Any.equals] to each other.
 */
fun <T : Any> MutableProvider<T?>.orElseNew(newValue: () -> T): MutableProvider<T> =
    MutableFallbackNewProvider(this as AbstractProvider<T?>, newValue)

private class MutableFallbackValueProvider<T>(
    private val provider: AbstractProvider<T?>,
    private val fallback: T
) : AbstractProvider<T>(provider.lock) {
    
    init {
        lock.withLock {
            addParent(provider) { it.takeUnless { it == fallback } }
            provider.addChild(this)
        }
    }
    
    override fun pull(): T {
        return provider.get() ?: fallback
    }
    
}

private class MutableFallbackProviderProvider<T : Any>(
    private val provider: AbstractProvider<T?>,
    private val fallback: AbstractProvider<T>
) : AbstractProvider<T>(ReentrantLock()) {
    
    init {
        provider.changeLock(lock)
        fallback.changeLock(lock)
        lock.withLock {
            addParent(provider, ignored = setOf(fallback)) { it.takeUnless { it == fallback.get() } }
            addInactiveParent(fallback)
            provider.addChild(this)
            fallback.addChild(this)
        }
    }
    
    override fun pull(): T {
        return provider.get() ?: fallback.get()
    }
    
}

private class MutableFallbackNewProvider<T : Any>(
    private val provider: AbstractProvider<T?>,
    private val newValue: () -> T
) : AbstractProvider<T>(provider.lock) {
    
    private val fallbackComparison by lazy(newValue)
    
    init {
        lock.withLock {
            addParent(provider) { it.takeUnless { it == fallbackComparison } }
            provider.addChild(this)
        }
    }
    
    override fun pull(): T {
        return provider.get() ?: newValue()
    }
    
}