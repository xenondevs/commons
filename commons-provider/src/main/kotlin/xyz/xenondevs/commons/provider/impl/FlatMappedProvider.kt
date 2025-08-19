package xyz.xenondevs.commons.provider.impl

import xyz.xenondevs.commons.provider.DeferredValue
import xyz.xenondevs.commons.provider.MutableProvider
import xyz.xenondevs.commons.provider.Provider
import kotlin.math.max

/**
 * A [DeferredValue] that delegates its value and sequence number to another [DeferredValue].
 * Inherits the sequence number from [delegate], but ensures it is at least [minSeqNo].
 */
private class DelegatedDeferredValue<T>(val delegate: DeferredValue<T>, val minSeqNo: Long) : DeferredValue<T> {
    
    override val seqNo: Long get() = max(minSeqNo, delegate.seqNo)
    override val value: T get() = delegate.value
    
}

/**
 * Contains the [result][flatMapResult] of a flatMap transform operation and the [sequence number][staticParentSeqNo]
 * of the value that was used to create it.
 */
internal data class DynamicParent<T, DP : Provider<T>>(
    val staticParentSeqNo: Long,
    val flatMapResult: DP
) {
    
    /**
     * Creates a new [DeferredValue] using the current value of [flatMapResult] and the max of its sequence number
     * and the sequence number that resulted in the creation of [flatMapResult] ([staticParentSeqNo]).
     */
    val deferredValue: DeferredValue<T>
        get() = DelegatedDeferredValue(flatMapResult.value, staticParentSeqNo)
    
}

/**
 * A flat-mapping provider provides the value [T] of a provider ([dynamic parent][dynamicParent]) that was created by applying a
 * [transform] operation to the value of the [static parent][staticParent] of type [P].
 * 
 * In order to propagate all updates from the dynamic parent, the dynamic parent needs to be resolved immediately,
 * disabling lazy evaluation of the static parent.
 */
internal abstract class AbstractFlatMappedProvider<P, T, DP : Provider<T>>(
    private val staticParent: Provider<P>,
    private val transform: (P) -> DP
) : AbstractProvider<T>() {
    
    @Volatile
    protected var dynamicParent: DynamicParent<T, DP> = run {
        val staticParentValue = staticParent.value
        val dynamicParent = transform(staticParentValue.value)
        dynamicParent.addWeakChild(this)
        DynamicParent(staticParentValue.seqNo, dynamicParent)
    }
    
    override val parents: Set<Provider<*>>
        get() = setOf(staticParent, dynamicParent.flatMapResult)
    
    override val value: DeferredValue<T>
        get() = dynamicParent.deferredValue
    
    override fun handleParentUpdated(updatedParent: Provider<*>) {
        if (updatedParent == dynamicParent.flatMapResult) {
            // The dynamic parent was updated: this can just be propagated to the update handlers
            // because getting DynamicParent#deferredValue will resolve the latest value from the dynamic parent.
            updateHandlers.notify()
        } else if (updatedParent == staticParent) {
            // The static parent was updated: this requires synchronization and a seqNo check to ensure
            // that it's not overriding a newer dynamic parent.
            // Additionally, this provider needs to be removed from the old dynamic parent's children
            // and added to the new dynamic parent's children to receive future updates.
            synchronized(this) {
                val staticParentValue = staticParent.value
                if (dynamicParent.staticParentSeqNo > staticParentValue.seqNo)
                    return
                
                val curDynamicParent = dynamicParent.flatMapResult
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
        if (dynamicParent.flatMapResult.update(value, setOf(this))) {
            updateHandlers.notify(ignore)
            return true
        }
        
        return false
    }
    
}