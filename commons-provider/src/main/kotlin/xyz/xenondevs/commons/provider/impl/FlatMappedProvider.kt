package xyz.xenondevs.commons.provider.impl

import xyz.xenondevs.commons.provider.DeferredValue
import xyz.xenondevs.commons.provider.MutableProvider
import xyz.xenondevs.commons.provider.Provider
import kotlin.math.max

/**
 * A [DeferredValue] that delegates its value and sequence number to another [DeferredValue].
 * Inherits the sequence number from [delegate], but ensures it is at least [minState].
 */
private class DelegatedDeferredValue<T>(val delegate: DeferredValue<T>, val minState: Long) : DeferredValue<T> {
    
    override val seqNo: Long get() = max(minState, delegate.seqNo)
    override val value: T get() = delegate.value
    
}

internal data class DynamicParent<P : Provider<*>>(
    val state: Long, // the state of staticParent.value from which the dynamic parent was retrieved
    val provider: P
)

internal abstract class AbstractFlatMappedProvider<P, T, DP : Provider<T>>(
    private val staticParent: Provider<P>,
    private val transform: (P) -> DP
) : AbstractProvider<T>() {
    
    @Volatile
    protected var dynamicParent: DynamicParent<DP> = run {
        val staticParentValue = staticParent.value
        val dynamicParent = transform(staticParentValue.value)
        dynamicParent.addWeakChild(this)
        DynamicParent(staticParentValue.seqNo, dynamicParent)
    }
    
    override val parents: Set<Provider<*>>
        get() = setOf(staticParent, dynamicParent.provider)
    
    override val value: DeferredValue<T>
        get() {
            val (minState, dynamicParent) = dynamicParent
            return DelegatedDeferredValue(dynamicParent.value, minState)
        }
    
    override fun handleParentUpdated(updatedParent: Provider<*>) {
        if (updatedParent == dynamicParent.provider) {
            updateHandlers.notify()
        } else if (updatedParent == staticParent) {
            synchronized(this) {
                val staticParentValue = staticParent.value
                if (dynamicParent.state > staticParentValue.seqNo)
                    return
                
                val curDynamicParent = dynamicParent.provider
                val newDynamicParent = transform(staticParentValue.value)
                if (newDynamicParent != curDynamicParent) {
                    curDynamicParent.removeWeakChild(this)
                    newDynamicParent.addWeakChild(this)
                    this.dynamicParent = DynamicParent(staticParentValue.seqNo, newDynamicParent)
                }
            }
            
            updateHandlers.notify()
        }
    }
    
}

internal class UnidirectionalFlatMappedProvider<P, T>(
    staticParent: Provider<P>,
    transform: (P) -> Provider<T>
) : AbstractFlatMappedProvider<P, T, Provider<T>>(staticParent, transform)

internal class BidirectionalFlatMappedProvider<P, T>(
    staticParent: Provider<P>,
    transform: (P) -> MutableProvider<T>
) : AbstractFlatMappedProvider<P, T, MutableProvider<T>>(staticParent, transform), MutableProviderDefaults<T> {
    
    override fun update(value: DeferredValue<T>, ignore: Set<Provider<*>>): Boolean {
        if (dynamicParent.provider.update(value, setOf(this))) {
            updateHandlers.notify(ignore)
            return true
        }
        
        return false
    }
    
}