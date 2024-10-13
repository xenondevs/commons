@file:OptIn(UnstableProviderApi::class)

package xyz.xenondevs.commons.provider.mutable

import xyz.xenondevs.commons.provider.AbstractProvider
import xyz.xenondevs.commons.provider.MutableProvider
import xyz.xenondevs.commons.provider.UnstableProviderApi
import java.util.concurrent.locks.ReentrantLock

/**
 * Creates a new [MutableProvider] with the given [initialValue].
 */
fun <T> mutableProvider(initialValue: T): MutableProvider<T> =
    object : AbstractProvider<T>(ReentrantLock()) {
        
        override fun pull(): T {
            return initialValue
        }
        
    }

/**
 * Creates a new [MutableProvider] that loads its value using the given [lazyValue] function.
 */
fun <T> mutableProvider(lazyValue: () -> T): MutableProvider<T> =
    object : AbstractProvider<T>(ReentrantLock()) {
        
        override fun pull(): T {
            return lazyValue()
        }
        
    }

/**
 * Creates a new [MutableProvider] that loads its value using the given [lazyValue] function
 * and sets it using the given [setValue] function.
 */
fun <T> mutableProvider(lazyValue: () -> T, setValue: (T) -> Unit = {}): MutableProvider<T> =
    mutableProvider(lazyValue).apply { subscribe(setValue) }