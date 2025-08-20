package xyz.xenondevs.commons.provider.impl

import xyz.xenondevs.commons.provider.DeferredValue
import xyz.xenondevs.commons.provider.MutableProvider
import xyz.xenondevs.commons.provider.Provider

/**
 * A lazily flat-mapping provider provides the value [T] of a provider ([dynamic parent][lazyDynamicParentContainer]) that was created by
 * applying a [transform] operation to the value of the [static parent][staticParent].
 *
 * Changes applied to the dynamic parent are only propagated by this provider when the dynamic parent has actually been
 * resolved, which only happens when the value of this provider is resolved. When the static parent changes, the dynamic parent becomes
 * unresolved and future updates to the dynamic parent will not be propagated until the value of this provider is resolved.
 */
internal abstract class AbstractLazyFlatMappedProvider<P, T, DP : Provider<T>>(
    private val staticParent: Provider<P>,
    private val transform: (P) -> DP,
    private val weak: Boolean
) : AbstractProvider<T>() {
    
    @Volatile
    private var staticParentSeqNo: Long = staticParent.value.seqNo
    
    @Volatile
    protected var lazyDynamicParentContainer: Lazy<DynamicParentContainer> = lazy(this, ::DynamicParentContainer)
        private set
    
    @Volatile
    override var value: DeferredValue<T> = lazyDynamicParentContainer.toLazilyReEmittingDeferredValue()
    
    override val parents: Set<Provider<*>>
        get() {
            val ldp = lazyDynamicParentContainer
            if (ldp.isInitialized()) {
                return setOf(staticParent, ldp.value.dynamicParent)
            } else {
                return setOf(staticParent)
            }
        }
    
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
     * Only if it is the current (active) dynamic parent and no newer update has been received
     * from it, this provider's [value] and [DynamicParentContainer.dynamicParentSeqNo] are
     * updated to match the dynamic parent.
     * 
     * Update handlers are only notified if there is a currently resolved dynamic parent.
     */
    private fun handleDynamicParentUpdated(updated: Provider<T>) {
        val dynamicParentValue = updated.value
        val ldpc = lazyDynamicParentContainer
        if (!ldpc.isInitialized()
            || ldpc.value.dynamicParent != updated
            || ldpc.value.dynamicParentSeqNo > dynamicParentValue.seqNo
        ) return
        
        synchronized(this) {
            val ldpc1 = lazyDynamicParentContainer
            if (!ldpc1.isInitialized()
                || ldpc1.value.dynamicParent != updated
                || ldpc.value.dynamicParentSeqNo > dynamicParentValue.seqNo
            ) return
            
            ldpc1.value.dynamicParentSeqNo = dynamicParentValue.seqNo
            value = DeferredValue.ReEmitted(dynamicParentValue)
        }
        
        updateHandlers.notify()
    }
    
    /**
     * Called when the static parent is updated.
     * 
     * Only if no newer update has been received from the static parent, this provider's
     * dynamic parent is reset to an uninitialized lazy state and [staticParentSeqNo] is
     * updated accordingly. The [value] is updated to a special deferred value that will
     * resolve the new dynamic parent resolving its value.
     * 
     * Update handlers are only notified if the dynamic parent was previously resolved.
     */
    private fun handleStaticParentUpdated(staticParentValue: DeferredValue<P>) {
        if (staticParentSeqNo > staticParentValue.seqNo)
            return
        
        var notify = false
        synchronized(this) {
            if (staticParentSeqNo > staticParentValue.seqNo)
                return
            
            // if a dynamic parent is currently resolved, unregister it and reset lazy dynamic parent container
            if (lazyDynamicParentContainer.isInitialized()) {
                val currentDynamicParent = lazyDynamicParentContainer.value.dynamicParent
                if (weak) currentDynamicParent.removeWeakChild(this) else currentDynamicParent.removeStrongChild(this)
                
                // reset to uninitialized
                lazyDynamicParentContainer = lazy(this, ::DynamicParentContainer)
                value = lazyDynamicParentContainer.toLazilyReEmittingDeferredValue()
                
                notify = true
            }
            
            staticParentSeqNo = staticParentValue.seqNo
        }
        
        // I don't think this is strictly necessary, but provides a more consistent behavior
        // (updates from dynamic parents aren't propagated either until the dynamic parent is resolved)
        if (notify)
            updateHandlers.notify()
    }
    
    /**
     * A container for a resolved [dynamicParent], along with the latest [sequence number][dynamicParentSeqNo]
     * of the value that this provider has received from it and re-emitted using [DeferredValue.ReEmitted].
     */
    inner class DynamicParentContainer {
        
        val dynamicParent: DP = transform(this@AbstractLazyFlatMappedProvider.staticParent.value.value)
        
        @Volatile
        var dynamicParentSeqNo: Long = dynamicParent.value.seqNo
        
        init {
            if (weak) {
                dynamicParent.addWeakChild(this@AbstractLazyFlatMappedProvider)
            } else {
                dynamicParent.addStrongChild(this@AbstractLazyFlatMappedProvider)
            }
        }
        
    }
    
    /**
     * Converts a [Lazy] of a [DynamicParentContainer] to a [DeferredValue] with a new sequence number that
     * initializes the dynamic parent container when its value is accessed.
     */
    private fun Lazy<DynamicParentContainer>.toLazilyReEmittingDeferredValue() = object : DeferredValue<T> {
        override val seqNo: Long = DeferredValue.nextSeqNo()
        override val value: T by lazy { this@toLazilyReEmittingDeferredValue.value.dynamicParent.value.value }
    }
    
}

internal class UnidirectionalLazyFlatMappedProvider<P, T>(
    staticParent: Provider<P>,
    transform: (P) -> Provider<T>,
    weak: Boolean
) : AbstractLazyFlatMappedProvider<P, T, Provider<T>>(staticParent, transform, weak)

internal class BidirectionalLazyFlatMappedProvider<P, T>(
    staticParent: Provider<P>,
    transform: (P) -> MutableProvider<T>,
    weak: Boolean
) : AbstractLazyFlatMappedProvider<P, T, MutableProvider<T>>(staticParent, transform, weak), MutableProviderDefaults<T> {
    
    override fun update(value: DeferredValue<T>, ignore: Set<Provider<*>>): Boolean {
        return lazyDynamicParentContainer.value.dynamicParent.update(value)
    }
    
}