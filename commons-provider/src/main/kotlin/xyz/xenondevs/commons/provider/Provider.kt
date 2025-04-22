package xyz.xenondevs.commons.provider

import java.util.function.Supplier
import kotlin.reflect.KProperty

/**
 * A [Provider] is a thread-safe, lazily-evaluated, reactive data source that holds a single value of type [T].
 *
 * There are various extension functions available for modelling atomic data transformations, such as [map] and [flatMap].
 * It is important that all data transformation functions are pure, meaning that they are side effect free and
 * do not access any mutable external state, in order to maintain the integrity of lazy evaluation. It is especially
 * important that they do not resolve any other provider's value, **as doing so may risk deadlocks**.
 * To properly operate on the values of multiple providers, combine them first via [combinedProvider].
 *
 * Additionally, it is also possible to [subscribe] to and [observe] providers.
 * Note that subscribing disables the lazy evaluation of the provider, as the value will need to be calculated
 * immediately when an update happens in order to propagate it to the subscribers.
 */
sealed interface Provider<out T> : Supplier<@UnsafeVariance T> {
    
    /**
     * Registers a function that will be called with the new value whenever the value of this [Provider] changes.
     *
     * Registering a subscriber disables lazy evaluation of the provider.
     *
     * The given value is not guaranteed to still be the current value at the time of invocation.
     */
    fun subscribe(action: (value: T) -> Unit)
    
    /**
     * Registers a function that will be called whenever the value of this [Provider] changes.
     *
     * Contrary to [subscribe], registering an observer does not disable lazy evaluation of the provider.
     */
    fun observe(action: () -> Unit)
    
    /**
     * Registers a weak subscriber that will be called when the value of this [Provider] changes.
     * The subscriber will be automatically removed when the [owner] is garbage collected.
     *
     * Registering a subscriber disables lazy evaluation of the provider.
     *
     * The given value is not guaranteed to still be the current value at the time of invocation.
     */
    fun <R : Any> subscribeWeak(owner: R, action: (owner: R, value: T) -> Unit)
    
    /**
     * Registers a weak observer that will be called when the value of this [Provider] changes.
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
    
    operator fun <X> getValue(thisRef: X?, property: KProperty<*>?): T = get()
    
}