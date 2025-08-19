package xyz.xenondevs.commons.provider

import java.lang.ref.WeakReference
import kotlin.reflect.KProperty

/**
 * A [MutableProvider] is a [Provider] that allows [setting][set] the value.
 * 
 * Usage examples:
 *
 * 1:
 * ```kotlin
 * val provider0 = mutableProvider("Hello") // MutableProvider with initial value "Hello"
 * val provider1 = provider0.map { it + ", World!" } // Lazily maps the value of provider0 by appending ", World!" to it
 *
 * println(provider1.get()) // "Hello, World!" (runs provider1 transform lambda)
 * provider0.set("Hi") // Sets the value of provider0 to "Hi", invalidating the cached value of provider1 without running the mapping lambda
 * println(provider1.get()) // "Hi, World!" (runs provider1 transform lambda)
 * ```
 * 
 * 2:
 * ```kotlin
 * val provider0 = mutableProvider(1) // MutableProvider with initial value 1
 * val provider1 = provider0.map({ it * 2 }, { it / 2 }) // Lazily maps the value of provider0 by multiplying it with 2 and untransforming it by dividing by 2
 *
 * println(provider1.get()) // "2" (runs provider1 transform lambda)
 * provider1.set(4) // Sets the value of provider1 to 4, invalidating the cached value of provider0 without running the mapping lambda
 * println(provider0.get()) // "2" (runs provider1 untransform lambda)
 * ```
 * 
 * @see mutableProvider
 */
interface MutableProvider<T> : Provider<T> {
    
    /**
     * Creates and returns a new [MutableProvider] that maps the value of [this][MutableProvider]
     * bi-directionally using the provided [transform] and [untransform] functions.
     *
     * [transform] and [untransform] should be pure functions.
     */
    fun <R> strongMap(transform: (T) -> R, untransform: (R) -> T): MutableProvider<R>
    
    /**
     * Creates and returns a new [MutableProvider] that maps the value of [this][MutableProvider]
     * bi-directionally using the provided [transform] and [untransform] functions.
     *
     * [transform] and [untransform] should be pure functions.
     *
     * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
     */
    fun <R> map(transform: (T) -> R, untransform: (R) -> T): MutableProvider<R>
    
    /**
     * Creates and returns a new [Provider] that maps the value of [this][MutableProvider]
     * unidirectionally using the provided [createObservable] function.
     * The value returned by [createObservable] should be an observable type that propagates updates
     * to the updateHandler specified in the function, which will in turn propagate changes from the provider.
     * 
     * [createObservable] should be a pure function.
     * 
     * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
     * 
     * @see [observed]
     */
    fun <R> mapObserved(createObservable: (value: T, updateHandler: () -> Unit) -> R): Provider<R>
    
    /**
     * Creates and returns a new [Provider] that maps the value of [this][MutableProvider]
     * unidirectionally using the provided [createObservable] function.
     * The value returned by [createObservable] should be an observable type that propagates updates
     * to the updateHandler specified in the function, which will in turn propagate changes from the provider.
     *
     * [createObservable] should be a pure function.
     * 
     * @see [strongObserved]
     */
    fun <R> strongMapObserved(createObservable: (value: T, updateHandler: () -> Unit) -> R): Provider<R>
    
    /**
     * Sets the value of [this][MutableProvider] to [value].
     */
    fun set(value: T) {
        update(DeferredValue.Direct(value))
    }
    
    /**
     * Sets the value of [this][MutableProvider] to [value].
     */
    operator fun <X> setValue(thisRef: X?, property: KProperty<*>, value: T) = set(value)
    
    /**
     * Attempts to update the value of this [MutableProvider] to [value].
     * Fails if the [DeferredValue.seqNo] of [value] is less than the current state of this [MutableProvider]. Equal state succeeds.
     *
     * On success, returns `true` and updates the value while not notifying [ignore].
     */
    fun update(value: DeferredValue<T>, ignore: Set<Provider<*>> = emptySet()): Boolean
    
}