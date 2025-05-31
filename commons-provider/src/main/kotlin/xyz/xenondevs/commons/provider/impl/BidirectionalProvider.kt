@file:Suppress("PackageDirectoryMismatch") // needs to be in root package for sealed hierarchy, impl directory is used for source file organization
package xyz.xenondevs.commons.provider

internal open class BidirectionalProvider<T>(
    @Volatile
    override var value: DeferredValue<T>
) : AbstractChildContainingProvider<T>(), MutableProviderImpl<T> {
    
    override fun update(value: DeferredValue<T>, ignore: Provider<*>?): Boolean {
        if (this.value > value)
            return false
        
        val runnables: List<() -> Unit>
        synchronized(this) {
            if (this.value > value)
                return false
            this.value = value
            
            // collect runnables to run outside of lock
            runnables = prepareNotifiers(ignore)
        }
        
        for (runnable in runnables) {
            runnable()
        }
        
        return true
    }
    
}

internal class BidirectionalTransformingProvider<P, T>(
    private val parent: MutableProviderImpl<P>,
    private val transform: (P) -> T,
    private val untransform: (T) -> P
) : BidirectionalProvider<T>(DeferredValue.Mapped(parent.value, transform)), HasParents {
    
    override fun handleParentUpdated(updatedParent: ProviderImpl<*>) {
        update(DeferredValue.Mapped(parent.value, transform), parent)
    }
    
    override fun update(value: DeferredValue<T>, ignore: Provider<*>?): Boolean {
        if (super.update(value, ignore) && parent != ignore) {
            parent.update(DeferredValue.Mapped(value, untransform), this)
            return true
        }
        
        return false
    }
    
}