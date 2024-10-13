@file:OptIn(UnstableProviderApi::class)

package xyz.xenondevs.commons.provider.immutable

import xyz.xenondevs.commons.provider.AbstractProvider
import xyz.xenondevs.commons.provider.Provider
import xyz.xenondevs.commons.provider.UnstableProviderApi
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