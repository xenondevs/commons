package xyz.xenondevs.commons.provider

import java.lang.ref.WeakReference
import java.util.function.Supplier
import kotlin.reflect.KProperty

/**
 * A [Provider] is a thread-safe, lazily-evaluated, reactive data source that holds a single value of type [T].
 *
 * Using [map], [immediateFlatMap], and various extension functions, atomic data transformations can be modelled.
 * 
 * Additionally, it is also possible to [subscribe] to and [observe] providers.
 * Note that subscribing disables the lazy evaluation of the provider, as the value will need to be calculated
 * immediately when an update happens in order to propagate it to the subscribers.
 * 
 * It is important that all data transformation functions are pure, meaning that they are side effect free and
 * do not access any mutable external state, in order to maintain the integrity of lazy evaluation. It is especially
 * important that they do not resolve any other provider's value, **as doing so may risk deadlocks**.
 * To properly operate on the values of multiple providers, combine them first via [combinedProvider].
 * 
 * Usage example:
 * ```kotlin
 * val provider0 = provider { "Hello" } // Provider with lazy value of "Hello"
 * val provider1 = provider0.map { it + ", World!" } // Lazily maps the value of provider by appending ", World!" to it
 * 
 * println(provider1.get()) // "Hello, World!" (runs lambdas above)
 * println(provider1.get()) // "Hello, World!" (cached value)
 * ```
 * 
 * @see provider
 */
interface Provider<out T> : Supplier<@UnsafeVariance T> {
    
    /**
     * A snapshot of the direct parents of this [Provider].
     * May not necessarily contain parents if it can be determined that no updates will be received from them.
     */
    val parents: Set<Provider<*>>
    
    /**
     * A snapshot of the direct children of this [Provider].
     * May not necessarily contain children if it can be determined that no updates will be sent to them.
     */
    val children: Set<Provider<*>>
    
    /**
     * The [DeferredValue] that holds the value of this [Provider].
     */
    val value: DeferredValue<T>
    
    /**
     * Creates and returns a new [Provider] that maps the value of [this][Provider]
     * using the [transform] function.
     *
     * [transform] should be a pure function.
     */
    fun <R> strongMap(transform: (T) -> R): Provider<R>
    
    /**
     * Creates and returns a new [Provider] that maps the value of [this][Provider]
     * using the [transform] function.
     *
     * [transform] should be a pure function.
     *
     * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
     */
    fun <R> map(transform: (T) -> R): Provider<R>
    
    /**
     * Creates and returns a new [Provider] that maps to the value of the [Provider]
     * returned by [transform].
     *
     * This disables lazy evaluation of [this][Provider] in order to ensure that all updates of the
     * flat-mapped provider are received.
     * 
     * [transform] should be a pure function.
     */
    fun <R> strongImmediateFlatMap(transform: (T) -> Provider<R>): Provider<R>
    
    /**
     * Creates and returns a new [Provider] that maps to the value of the [Provider]
     * returned by [transform].
     *
     * This disables lazy evaluation of [this][Provider] in order to ensure that all updates of the
     * flat-mapped provider are received.
     * 
     * [transform] should be a pure function.
     *
     * The returned provider will only be stored in a [WeakReference] in the parent providers
     * ([this][MutableProvider] and the result of [transform]).
     */
    fun <R> immediateFlatMap(transform: (T) -> Provider<R>): Provider<R>
    
    /**
     * Creates and returns a new [MutableProvider] that maps to the value of the [MutableProvider]
     * returned by [transform].
     *
     * This disables lazy evaluation of [this][Provider] in order to ensure that all updates of the
     * flat-mapped provider are received.
     * 
     * [transform] should be a pure function.
     */
    fun <R> strongImmediateFlatMapMutable(transform: (T) -> MutableProvider<R>): MutableProvider<R>
    
    /**
     * Creates and returns a new [MutableProvider] that maps to the value of the [MutableProvider]
     * returned by [transform].
     * 
     * This disables lazy evaluation of [this][Provider] in order to ensure that all updates of the
     * flat-mapped provider are received.
     *
     * [transform] should be a pure function.
     *
     * The returned provider will only be stored in a [WeakReference] in the parent providers
     * ([this][MutableProvider] and the result of [transform]).
     */
    fun <R> immediateFlatMapMutable(transform: (T) -> MutableProvider<R>): MutableProvider<R>
    
    /**
     * Creates and returns new [Provider] that lazily maps to the value of the [Provider]
     * returned by [transform].
     *
     * Contrary to [strongFlatMap], this does not disable lazy evaluation of [this][Provider].
     * Consequently, updates (for example in [subscribers][subscribe] or [observers][observe]) are only
     * received after a flat-mapped provider has been resolved by evaluating the returned provider, applying [transform].
     *
     * [transform] should be a pure function.
     */
    fun <R> strongFlatMap(transform: (T) -> Provider<R>): Provider<R>
    
    /**
     * Creates and returns new [Provider] that lazily maps to the value of the [Provider]
     * returned by [transform].
     * 
     * Contrary to [immediateFlatMap], this does not disable lazy evaluation of [this][Provider].
     * Consequently, updates (for example in [subscribers][subscribe] or [observers][observe]) are only
     * received after a flat-mapped provider has been resolved by evaluating the returned provider, applying [transform].
     * 
     * [transform] should be a pure function.
     * 
     * The returned provider will only be stored in a [WeakReference] in the parent providers
     * ([this][MutableProvider] and the result of [transform]).
     */
    fun <R> flatMap(transform: (T) -> Provider<R>): Provider<R>
    
    /**
     * Creates and returns new [MutableProvider] that lazily maps to the value of the [MutableProvider]
     * returned by [transform].
     *
     * Contrary to [strongImmediateFlatMapMutable], this does not disable lazy evaluation of [this][Provider].
     * Consequently, updates (for example in [subscribers][subscribe] or [observers][observe]) are only
     * received after a flat-mapped provider has been resolved by evaluating the returned provider, applying [transform].
     *
     * [transform] should be a pure function.
     */
    fun <R> strongFlatMapMutable(transform: (T) -> MutableProvider<R>): MutableProvider<R>
    
    /**
     * Creates and returns new [MutableProvider] that lazily maps to the value of the [MutableProvider]
     * returned by [transform].
     *
     * Contrary to [immediateFlatMapMutable], this does not disable lazy evaluation of [this][Provider].
     * Consequently, updates (for example in [subscribers][subscribe] or [observers][observe]) are only
     * received after a flat-mapped provider has been resolved by evaluating the returned provider, applying [transform].
     *
     * [transform] should be a pure function.
     * 
     * The returned provider will only be stored in a [WeakReference] in the parent providers
     * ([this][MutableProvider] and the result of [transform]).
     */
    fun <R> flatMapMutable(transform: (T) -> MutableProvider<R>): MutableProvider<R>
    
    /**
     * Registers a function that will be called with the new value whenever the value of this [Provider] changes.
     * If multiple threads update the value concurrently, intermediate subscriber invocations may be skipped and
     * values may be out of order.
     * If resolving the value of this [Provider] throws an exception, subscriber invocations will be skipped silently.
     * 
     * Registering a subscriber disables lazy evaluation of the provider.
     *
     * The given value is not guaranteed to still be the current value at the time of invocation.
     */
    fun subscribe(action: (value: T) -> Unit)
    
    /**
     * Registers a function that will be called whenever the value of this [Provider] changes.
     * If multiple threads update the value concurrently, intermediate observer invocations may be skipped.
     *
     * Contrary to [subscribe], registering an observer does not disable lazy evaluation of the provider.
     */
    fun observe(action: () -> Unit)
    
    /**
     * Registers a weak subscriber that will be called when the value of this [Provider] changes.
     * If multiple threads update the value concurrently, subscriber invocations may be skipped and
     * values may be out of order.
     * If resolving the value of this [Provider] throws an exception, subscriber invocations will be skipped silently.
     * The subscriber will be automatically removed when the [owner] is garbage collected.
     *
     * Registering a subscriber disables lazy evaluation of the provider.
     *
     * The given value is not guaranteed to still be the current value at the time of invocation.
     */
    fun <R : Any> subscribeWeak(owner: R, action: (owner: R, value: T) -> Unit)
    
    /**
     * Registers a weak observer that will be called when the value of this [Provider] changes.
     * If multiple threads update the value concurrently, intermediate observer invocations may be skipped.
     * The observer will be automatically removed when the [owner] is garbage collected.
     *
     * Contrary to [subscribeWeak], registering an observer does not disable lazy evaluation of the provider.
     */
    fun <R : Any> observeWeak(owner: R, action: (owner: R) -> Unit)
    
    /**
     * Removes a previously registered [subscriber][subscribe].
     */
    fun unsubscribe(action: Function1<T, Unit>)
    
    /**
     * Removes a previously registered [observer][observe].
     */
    fun unobserve(action: Function0<Unit>)
    
    /**
     * Removes a previously registered [weak subscriber][subscribeWeak].
     */
    fun <R : Any> unsubscribeWeak(owner: R, action: Function2<R, T, Unit>)
    
    /**
     * Removes a previously registered [weak observer][observeWeak].
     */
    fun <R : Any> unobserveWeak(owner: R, action: Function1<R, Unit>)
    
    /**
     * Removes all [weak subscribers][subscribeWeak] under the given [owner].
     */
    fun <R : Any> unsubscribeWeak(owner: R)
    
    /**
     * Removes all [weak observers][observeWeak] under the given [owner].
     */
    fun <R : Any> unobserveWeak(owner: R)
    
    /**
     * Resolves the [value] of [this][Provider] and returns it.
     */
    override fun get(): T = value.value
    
    /**
     * Resolves the [value] of [this][Provider] and returns it.
     */
    operator fun <X> getValue(thisRef: X?, property: KProperty<*>?): T = get()
    
    // ---- CUSTOM PROVIDER API ----
    
    /**
     * Adds a strongly referenced [child] to [this][Provider].
     * 
     * Child management is performed automatically when creating a new provider,
     * so this should only be used for custom [Provider] implementations.
     */
    fun addStrongChild(child: Provider<*>)
    
    /**
     * Removes a strongly referenced [child] from [this][Provider].
     * 
     * Child management is performed automatically when creating a new provider,
     * so this should only be used for custom [Provider] implementations.
     */
    fun removeStrongChild(child: Provider<*>)
    
    /**
     * Adds a weakly referenced [child] to [this][Provider].
     * 
     * Child management is performed automatically when creating a new provider,
     * so this should only be used for custom [Provider] implementations.
     */
    fun addWeakChild(child: Provider<*>)
    
    /**
     * Removes a weakly referenced [child] from [this][Provider].
     * 
     * Child management is performed automatically when creating a new provider,
     * so this should only be used for custom [Provider] implementations.
     */
    fun removeWeakChild(child: Provider<*>)
    
    /**
     * Handles an update of the [parent provider][updatedParent].
     * 
     * Updating is performed automatically,
     * so this should only be used for custom [Provider] implementations.
     */
    fun handleParentUpdated(updatedParent: Provider<*>)
    
}