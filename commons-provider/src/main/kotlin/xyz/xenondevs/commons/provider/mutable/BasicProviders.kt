package xyz.xenondevs.commons.provider.mutable

import xyz.xenondevs.commons.provider.AbstractProvider
import xyz.xenondevs.commons.provider.Provider

/**
 * Creates a new [MutableProvider] with the given [initialValue].
 */
fun <T> mutableProvider(initialValue: T): MutableProvider<T> =
    object : AbstractProvider<T>() {
        override fun loadValue(): T = initialValue
    }

/**
 * Creates a new [MutableProvider] that loads its value using the given [loadValue] function
 * and sets it using the given [setValue] function.
 */
fun <T> mutableProvider(loadValue: () -> T, setValue: (T) -> Unit = {}): MutableProvider<T> =
    object : AbstractProvider<T>() {
        override fun loadValue(): T = loadValue()
        override fun set(value: T, updateChildren: Boolean, callSubscribers: Boolean, ignoredChildren: Set<Provider<*>>) {
            super.set(value, updateChildren, callSubscribers, ignoredChildren)
            setValue(value)
        }
    }