package xyz.xenondevs.commons.provider.impl

import xyz.xenondevs.commons.provider.DeferredValue
import xyz.xenondevs.commons.provider.MutableProvider
import xyz.xenondevs.commons.provider.Provider

/**
 * A flat-mapping provider provides the value [T] of a provider ([dynamic parent][dynamicParent]) that was created by applying a
 * [transform] operation to the value of the [static parent][staticParent].
 * 
 * In order to propagate all updates from the dynamic parent, the dynamic parent needs to be resolved immediately,
 * disabling lazy evaluation of the static parent.
 */
internal abstract class AbstractImmediateFlatMappedProvider<P, T, DP : Provider<T>>(
    private val staticParent: Provider<P>,
    private val transform: (P) -> DP,
    private val weak: Boolean
) : AbstractProvider<T>() {
    
    @Volatile
    private var staticParentSeqNo: Long = 0
    
    @Volatile
    private var dynamicParentSeqNo: Long = 0
    
    @Volatile
    protected var dynamicParent: DP
        private set
    
    @Volatile
    override lateinit var value: DeferredValue<T>
    
    init {
        val staticParentValue = staticParent.value
        dynamicParent = transform(staticParentValue.value)
        if (weak) dynamicParent.addWeakChild(this) else dynamicParent.addStrongChild(this)
        value = DeferredValue.ReEmitted(dynamicParent.value)
        staticParentSeqNo = staticParentValue.seqNo
        dynamicParentSeqNo = dynamicParent.value.seqNo
    }
    
    override val parents: Set<Provider<*>>
        get() = setOf(staticParent, dynamicParent)
    
    @Suppress("UNCHECKED_CAST")
    override fun handleParentUpdated(updatedParent: Provider<*>) {
        if (updatedParent == staticParent) {
            updatedParent as Provider<P>
            handleStaticParentUpdated(updatedParent.value)
        } else {
            // assume it is the current dynamic parent, but we can't be certain until in synchronized block
            updatedParent as Provider<T>
            handleDynamicParentUpdated(updatedParent)
        }
    }
    
    /**
     * Called when a parent that is not the static parent is updated.
     * (This may be the current dynamic parent or an older one.)
     * 
     * Only if it is the current dynamic parent and no newer update has been received from it, this
     * provider's [value] and the [dynamicParentSeqNo] are updated to match the dynamic parent.
     */
    private fun handleDynamicParentUpdated(updated: Provider<T>) {
        val dynamicParentValue = updated.value
        if (updated != dynamicParent || dynamicParentSeqNo > dynamicParentValue.seqNo)
            return
        
        synchronized(this) {
            if (updated != dynamicParent || dynamicParentSeqNo > dynamicParentValue.seqNo)
                return
            
            value = DeferredValue.ReEmitted(dynamicParentValue)
            dynamicParentSeqNo = dynamicParentValue.seqNo
        }
        
        updateHandlers.notify()
    }
    
    /**
     * Called when the static parent is updated.
     * 
     * Only if no newer update has been received from the static parent, this provider's
     * dynamic parent is recalculated using [transform]. Then, [dynamicParent], [value], [dynamicParentSeqNo]
     * and [staticParentSeqNo] are updated accordingly.
     */
    private fun handleStaticParentUpdated(staticParentValue: DeferredValue<P>) {
        if (staticParentSeqNo > staticParentValue.seqNo)
            return
        
        synchronized(this) {
            if (staticParentSeqNo > staticParentValue.seqNo)
                return
            
            val currentDynamicParent = dynamicParent
            val newDynamicParent = transform(staticParentValue.value)
            if (newDynamicParent != currentDynamicParent) {
                if (weak) currentDynamicParent.removeWeakChild(this) else currentDynamicParent.removeStrongChild(this)
                if (weak) newDynamicParent.addWeakChild(this) else newDynamicParent.addStrongChild(this)
                
                value = DeferredValue.ReEmitted(newDynamicParent.value)
                dynamicParent = newDynamicParent
                staticParentSeqNo = staticParentValue.seqNo
                dynamicParentSeqNo = newDynamicParent.value.seqNo
            }
        }
        
        updateHandlers.notify()
    }
    
}

internal class UnidirectionalImmediateFlatMappedProvider<P, T>(
    staticParent: Provider<P>,
    transform: (P) -> Provider<T>,
    weak: Boolean
) : AbstractImmediateFlatMappedProvider<P, T, Provider<T>>(staticParent, transform, weak)

internal class BidirectionalImmediateFlatMappedProvider<P, T>(
    staticParent: Provider<P>,
    transform: (P) -> MutableProvider<T>,
    weak: Boolean
) : AbstractImmediateFlatMappedProvider<P, T, MutableProvider<T>>(staticParent, transform, weak), MutableProviderDefaults<T> {
    
    override fun update(value: DeferredValue<T>, ignore: Set<Provider<*>>): Boolean {
        return dynamicParent.update(value)
    }
    
}