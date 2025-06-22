@file:Suppress("PackageDirectoryMismatch") // needs to be in root package for sealed hierarchy, impl directory is used for source file organization
package xyz.xenondevs.commons.provider

internal abstract class UnidirectionalProvider<P, T> : AbstractChildContainingProvider<T>(), HasParents {
    
    abstract override var value: DeferredValue<T>
    
    @Volatile
    private var state: Long = 0
    
    protected fun update(value: DeferredValue<T>): Boolean {
        if (this.value > value)
            return false
        
        val runnables: List<() -> Unit>
        synchronized(this) {
            if (this.value > value)
                return false
            this.value = value
            
            // collect runnables to run outside of lock
            runnables = prepareNotifiers()
        }
        
        for (runnable in runnables) {
            runnable()
        }
        
        return true
    }
    
}

internal class UnidirectionalTransformingProvider<P, T>(
    private val parent: ProviderImpl<P>,
    private val transform: (P) -> T
) : UnidirectionalProvider<P, T>() {
    
    override val parents: Set<Provider<*>>
        get() = setOf(parent)
    
    @Volatile
    override var value: DeferredValue<T> = DeferredValue.Mapped(parent.value, transform)
    
    override fun handleParentUpdated(updatedParent: ProviderImpl<*>) {
        update(DeferredValue.Mapped(parent.value, transform))
    }
    
}

internal class ObservedValueUndirectionalTransformingProvider<P, T> private constructor(
    private val parent: MutableProviderImpl<P>,
    private val createObservedObj: (original: P, updateHandler: () -> Unit) -> T
) : UnidirectionalProvider<P, T>() {
    
    override val parents: Set<Provider<*>>
        get() = setOf(parent)
    
    @Volatile
    override var value: DeferredValue<T> = createDeferredValue()
    
    private fun createDeferredValue(): DeferredValue<T> =
        DeferredValue.MappedWithSelf(parent.value) { original, deferredValue ->
            createObservedObj(original) { handleObservedUpdated(deferredValue) }
        }
    
    private fun handleObservedUpdated(value: DeferredValue<T>) {
        if (update(value)) {
            parent.update(parent.value, this)
        }
    }
    
    override fun handleParentUpdated(updatedParent: ProviderImpl<*>) {
        update(createDeferredValue())
    }
    
    companion object {
        
        fun <P, T> of(
            parent: MutableProvider<P>,
            weak: Boolean,
            createObservedObj: (original: P, updateHandler: () -> Unit) -> T
        ): Provider<T> {
            parent as MutableProviderImpl<P>
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

internal class MultiUnidirectionalTransformingProvider<P, T> private constructor(
    private val _parents: List<ProviderImpl<P>>,
    private val transform: (List<P>) -> T
) : UnidirectionalProvider<P, T>() {
    
    override val parents: Set<Provider<*>>
        get() = _parents.toSet()
    
    @Volatile
    override var value: DeferredValue<T> = DeferredValue.MappedMulti(_parents.map { it.value }, transform)
    
    override fun handleParentUpdated(updatedParent: ProviderImpl<*>) {
        update(DeferredValue.MappedMulti(_parents.map { it.value }, transform))
    }
    
    companion object {
        
        @Suppress("UNCHECKED_CAST")
        fun <P, T> of(parents: List<Provider<P>>, weak: Boolean, transform: (List<P>) -> T): Provider<T> {
            val provider = MultiUnidirectionalTransformingProvider(parents as List<ProviderImpl<P>>, transform)
            for (parent in parents) {
                if (parent is ProviderWithChildren) {
                    if (weak) {
                        parent.addWeakChild(provider)
                    } else {
                        parent.addStrongChild(provider)
                    }
                }
            }
            return provider
        }
        
    }
    
}