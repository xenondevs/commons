package xyz.xenondevs.commons.provider.impl

import xyz.xenondevs.commons.provider.DeferredValue
import xyz.xenondevs.commons.provider.MutableProvider
import xyz.xenondevs.commons.provider.Provider

internal abstract class UnidirectionalProvider<P, T> : AbstractProvider<T>() {
    
    abstract override var value: DeferredValue<T>
    
    protected fun update(value: DeferredValue<T>): Boolean {
        if (this.value > value)
            return false
        
        synchronized(this) {
            if (this.value > value)
                return false
            this.value = value
        }
        
        updateHandlers.notify()
        
        return true
    }
    
}

// .map { }: Provider
internal class UnidirectionalTransformingProvider<P, T>(
    private val parent: Provider<P>,
    private val transform: (P) -> T
) : UnidirectionalProvider<P, T>() {
    
    override val parents: Set<Provider<*>>
        get() = setOf(parent)
    
    @Volatile
    override var value: DeferredValue<T> = DeferredValue.Mapped(parent.value, transform)
    
    override fun handleParentUpdated(updatedParent: Provider<*>) {
        update(DeferredValue.Mapped(parent.value, transform))
    }
    
}

/**
 * A [DeferredValue] that is the result of applying [transform] to the value of [parent] and the current [DeferredValue] itself.
 * Inherits the sequence number from [parent].
 */
private class SelfMappedDeferredValue<P, T>(
    val parent: DeferredValue<P>, 
    val transform: (parentValue: P, self: DeferredValue<T>) -> T
) : DeferredValue<T> {
    
    override val seqNo: Long get() = parent.seqNo
    override val value: T by lazy { transform(parent.value, this) }
    
}

// .mapObserved()
internal class ObservedValueUndirectionalTransformingProvider<P, T>(
    private val parent: MutableProvider<P>,
    private val createObservedObj: (parentValue: P, updateHandler: () -> Unit) -> T
) : UnidirectionalProvider<P, T>() {
    
    override val parents: Set<Provider<*>>
        get() = setOf(parent)
    
    @Volatile
    override var value: DeferredValue<T> = createDeferredValue()
    
    /**
     * Creates a [SelfMappedDeferredValue] that [creates an observed object][createObservedObj] that
     * calls [handleObservedUpdated] when it is modified.
     * 
     * The self-mapped approach is required as it is used to bind the [DeferredValue] instance to the
     * specific observed object. This allows the observed object to set the provider's value to the 
     * [DeferredValue] that is associated with the observed object when said observed object is updated.
     * If this provider's value has already changed, the call to update with the old [DeferredValue]
     * will be ignored due to the lower sequence number.
     */
    private fun createDeferredValue(): DeferredValue<T> =
        SelfMappedDeferredValue(parent.value) { original, deferredValue ->
            createObservedObj(original) { handleObservedUpdated(deferredValue) }
        }
    
    private fun handleObservedUpdated(value: DeferredValue<T>) {
        if (update(value)) {
            parent.update(parent.value, setOf(this))
        }
    }
    
    override fun handleParentUpdated(updatedParent: Provider<*>) {
        update(createDeferredValue())
    }
    
}

// combinedProvider
internal class MultiUnidirectionalTransformingProvider<P, T> private constructor(
    private val _parents: List<Provider<P>>,
    private val transform: (List<P>) -> T
) : UnidirectionalProvider<P, T>() {
    
    override val parents: Set<Provider<*>>
        get() = _parents.toSet()
    
    @Volatile
    override var value: DeferredValue<T> = DeferredValue.MappedMulti(_parents.map { it.value }, transform)
    
    override fun handleParentUpdated(updatedParent: Provider<*>) {
        update(DeferredValue.MappedMulti(_parents.map { it.value }, transform))
    }
    
    companion object {
        
        fun <P, T> of(parents: List<Provider<P>>, weak: Boolean, transform: (List<P>) -> T): Provider<T> {
            val provider = MultiUnidirectionalTransformingProvider(parents, transform)
            for (parent in parents) {
                if (weak) {
                    parent.addWeakChild(provider)
                } else {
                    parent.addStrongChild(provider)
                }
            }
            provider.handleParentUpdated(parents[0]) // propagate potentially lost update during provider creation and child assignment
            return provider
        }
        
    }
    
}