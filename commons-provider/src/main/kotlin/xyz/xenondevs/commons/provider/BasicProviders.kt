@file:JvmName("Providers")
@file:JvmMultifileClass

package xyz.xenondevs.commons.provider

import xyz.xenondevs.commons.provider.impl.BidirectionalProvider
import xyz.xenondevs.commons.provider.impl.StableProvider

/**
 * A [Provider] that always returns `null`.
 */
val NULL_PROVIDER: Provider<Nothing?> = provider(null)

/**
 * Creates a new [Provider] that loads its value using the given [lazyValue] function.
 * [lazyValue] should be a pure function.
 */
fun <T> provider(lazyValue: () -> T): Provider<T> =
    StableProvider(DeferredValue.Lazy(lazyValue))

/**
 * Creates a new [Provider] with the given [value].
 */
fun <T> provider(value: T): Provider<T> =
    StableProvider(DeferredValue.Direct(value))

/**
 * Creates a new [MutableProvider] with the given [initialValue].
 */
fun <T> mutableProvider(initialValue: T): MutableProvider<T> =
    BidirectionalProvider(DeferredValue.Direct(initialValue))

/**
 * Creates a new [MutableProvider] that loads its value using the given [lazyValue] function.
 * [lazyValue] should be a pure function.
 */
fun <T> mutableProvider(lazyValue: () -> T): MutableProvider<T> =
    BidirectionalProvider(DeferredValue.Lazy(lazyValue))

/**
 * Creates a new [MutableProvider] that loads its value using the given [lazyValue] function
 * and sets it using the given [setValue] function.
 * [lazyValue] should be a pure function.
 */
fun <T> mutableProvider(lazyValue: () -> T, setValue: (T) -> Unit = {}): MutableProvider<T> =
    mutableProvider(lazyValue).apply { subscribe(setValue) }