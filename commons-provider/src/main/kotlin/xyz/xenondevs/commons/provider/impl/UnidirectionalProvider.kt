package xyz.xenondevs.commons.provider.impl

import xyz.xenondevs.commons.provider.DeferredValue
import xyz.xenondevs.commons.provider.MutableProvider
import xyz.xenondevs.commons.provider.Provider

internal abstract class UnidirectionalProvider<P, T> : AbstractProvider<T>() {
    
    abstract override var value: DeferredValue<T>
    
    @Volatile
    private var state: Long = 0
    
    protected fun update(value: DeferredValue<T>): Boolean {
        if (this.value > value)
            return false
        
        val updateHandlers: UpdateHandlerCollection
        synchronized(this) {
            if (this.value > value)
                return false
            this.value = value
            
            // update handlers at time of value change (run outside of lock)
            updateHandlers = this.updateHandlers
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
 * A [DeferredValue] that is the result of applying [transform] to the value of [parent]
 * and the current [DeferredValue] itself.
 * Inherits the sequence number from [parent].
 */
private class SelfMappedDeferredValue<P, T>(val parent: DeferredValue<P>, val transform: (P, DeferredValue<T>) -> T) : DeferredValue<T> {
    
    override val seqNo: Long get() = parent.seqNo
    override val value: T by lazy { transform(parent.value, this) }
    
}

// .observed()
internal class ObservedValueUndirectionalTransformingProvider<P, T> private constructor(
    private val parent: MutableProvider<P>,
    private val createObservedObj: (original: P, updateHandler: () -> Unit) -> T
) : UnidirectionalProvider<P, T>() {
    
    override val parents: Set<Provider<*>>
        get() = setOf(parent)
    
    @Volatile
    override var value: DeferredValue<T> = createDeferredValue()
    
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
    
    companion object {
        
        fun <P, T> of(
            parent: MutableProvider<P>,
            weak: Boolean,
            createObservedObj: (original: P, updateHandler: () -> Unit) -> T
        ): Provider<T> {
            val provider = ObservedValueUndirectionalTransformingProvider(parent, createObservedObj)
            if (weak) {
                parent.addWeakChild(provider)
            } else {
                parent.addStrongChild(provider)
            }
            return provider
        }
        
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
            return provider
        }
        
    }
    
}