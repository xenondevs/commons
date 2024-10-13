@file:OptIn(UnstableProviderApi::class)

package xyz.xenondevs.commons.provider.immutable

import xyz.xenondevs.commons.provider.AbstractProvider
import xyz.xenondevs.commons.provider.Provider
import xyz.xenondevs.commons.provider.UnstableProviderApi
import java.util.concurrent.locks.ReentrantLock

/**
 * A [Provider] that always returns `null`.
 */
val NULL_PROVIDER: Provider<Nothing?> = provider(null)

/**
 * Creates a new [Provider] that loads its value using the given [lazyValue] function.
 */
fun <T> provider(lazyValue: () -> T): Provider<T> =
    object : AbstractProvider<T>(ReentrantLock()) {
        
        override fun pull(): T {
            return lazyValue()
        }
        
    }

/**
 * Creates a new [Provider] with the given [value].
 */
fun <T> provider(value: T): Provider<T> =
    object : AbstractProvider<T>(ReentrantLock()) {
        
        override fun pull(): T {
            return value
        }
        
    }