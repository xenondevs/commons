@file:OptIn(UnstableProviderApi::class)

package xyz.xenondevs.commons.provider

import java.util.concurrent.locks.ReentrantLock

/**
 * A [Provider] that always returns `null`.
 */
val NULL_PROVIDER: Provider<Nothing?> = provider(null)

/**
 * Creates a new [Provider] that loads its value using the given [lazyValue] function.
 * [lazyValue] should be a pure function.
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
 * [lazyValue] should be a pure function.
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
 * [lazyValue] should be a pure function.
 */
fun <T> mutableProvider(lazyValue: () -> T, setValue: (T) -> Unit = {}): MutableProvider<T> =
    mutableProvider(lazyValue).apply { subscribe(setValue) }