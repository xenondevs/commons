package xyz.xenondevs.commons.provider

import java.util.function.Supplier
import kotlin.reflect.KProperty

sealed interface Provider<out T> : Supplier<@UnsafeVariance T> {
    
    /**
     * Registers a subscriber that will be called when the value of this [Provider] changes.
     */
    fun subscribe(action: (value: T) -> Unit)
    
    /**
     * Registers a weak subscriber that will be called when the value of this [Provider] changes.
     * The subscriber will be automatically removed when the [owner] is garbage collected.
     */
    fun <R : Any> subscribeWeak(owner: R, action: (owner: R, value: T) -> Unit)
    
    /**
     * Removes a previously registered subscriber.
     */
    fun unsubscribe(action: Function1<T, Unit>)
    
    /**
     * Removes a previously registered weak subscriber.
     */
    fun <R : Any> unsubscribeWeak(owner: R, action: Function2<R, T, Unit>)
    
    /**
     * Removes all weak subscribers under the given [owner].
     */
    fun <R : Any> unsubscribeWeak(owner: R)
    
    operator fun <X> getValue(thisRef: X?, property: KProperty<*>?): T = get()
    
}