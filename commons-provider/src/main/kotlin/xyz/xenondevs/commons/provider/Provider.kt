package xyz.xenondevs.commons.provider

import java.util.function.Supplier
import kotlin.reflect.KProperty

/**
 * A [Provider] is a container for a value [T].
 * It is a reactive data structure that allows for the registration of children and subscribers,
 * which will be notified when the value of the [Provider] changes.
 */
interface Provider<out T> : Supplier<@UnsafeVariance T> {
    
    /**
     * Updates the value of this [Provider].
     */
    fun update()
    
    /**
     * Registers a child [Provider] that will be updated via [Provider.update] when the value of this [Provider] changes.
     */
    fun addChild(provider: Provider<*>)
    
    /**
     * Registers a subscriber that will be called when the value of this [Provider] changes.
     */
    fun subscribe(action: (T) -> Unit)
    
    operator fun <X> getValue(thisRef: X?, property: KProperty<*>?): T = get()
    
}