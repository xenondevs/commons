package xyz.xenondevs.commons.provider.impl

import xyz.xenondevs.commons.provider.DeferredValue
import xyz.xenondevs.commons.provider.MutableProvider
import xyz.xenondevs.commons.provider.Provider

internal open class BidirectionalProvider<T>(
    @Volatile
    override var value: DeferredValue<T>
) : AbstractProvider<T>(), MutableProviderDefaults<T> {
    
    override val parents: Set<Provider<*>>
        get() = emptySet()
    
    override fun update(value: DeferredValue<T>, ignore: Set<Provider<*>>): Boolean {
        if (this.value > value)
            return false
        
        synchronized(this) {
            if (this.value > value)
                return false
            this.value = value
        }
        
        updateHandlers.notify(ignore)
        
        return true
    }
    
    // has no parent
    override fun handleParentUpdated(updatedParent: Provider<*>) = Unit
    
}

internal class BidirectionalTransformingProvider<P, T>(
    private val parent: MutableProvider<P>,
    private val transform: (P) -> T,
    private val untransform: (T) -> P
) : BidirectionalProvider<T>(DeferredValue.Mapped(parent.value, transform)) {
    
    override val parents: Set<Provider<*>>
        get() = setOf(parent)
    
    override fun handleParentUpdated(updatedParent: Provider<*>) {
        update(DeferredValue.Mapped(parent.value, transform), setOf(parent))
    }
    
    override fun update(value: DeferredValue<T>, ignore: Set<Provider<*>>): Boolean {
        if (super.update(value, ignore) && parent !in ignore) {
            parent.update(DeferredValue.Mapped(value, untransform), setOf(this))
            return true
        }
        
        return false
    }
    
}