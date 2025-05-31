@file:Suppress("PackageDirectoryMismatch") // needs to be in root package for sealed hierarchy, impl directory is used for source file organization
package xyz.xenondevs.commons.provider

internal data class DynamicParent<P : ProviderImpl<*>>(
    val state: Long, // the state of staticParent.value from which the dynamic parent was retrieved
    val provider: P
)

internal abstract class AbstractFlatMappedProvider<P, T, DP : ProviderImpl<T>>(
    private val staticParent: ProviderImpl<P>,
    private val transform: (P) -> DP
) : AbstractChildContainingProvider<T>(), HasParents {
    
    @Volatile
    protected var dynamicParent: DynamicParent<DP> = run {
        val staticParentValue = staticParent.value
        val dynamicParent = transform(staticParentValue.value)
        if (dynamicParent is ProviderWithChildren<*>) {
            dynamicParent.addWeakChild(this)
        }
        DynamicParent(staticParentValue.state, dynamicParent)
    }
    
    override val value: DeferredValue<T>
        get() {
            val (minState, dynamicParent) = dynamicParent
            return DeferredValue.Delegate(dynamicParent.value, minState)
        }
    
    override fun handleParentUpdated(updatedParent: ProviderImpl<*>) {
        if (updatedParent == dynamicParent.provider) {
            callUpdate()
        } else if (updatedParent == staticParent) {
            synchronized(this) {
                val staticParentValue = staticParent.value
                if (dynamicParent.state > staticParentValue.state)
                    return
                
                val curDynamicParent = dynamicParent.provider
                val newDynamicParent = transform(staticParentValue.value)
                if (newDynamicParent != curDynamicParent) {
                    (curDynamicParent as? ProviderWithChildren<*>)?.removeWeakChild(this)
                    (newDynamicParent as? ProviderWithChildren<*>)?.addWeakChild(this)
                    this.dynamicParent = DynamicParent(staticParentValue.state, newDynamicParent)
                }
            }
            
            callUpdate()
        }
    }
    
    protected fun callUpdate() {
        for (runnable in prepareNotifiers()) {
            runnable()
        }
    }
    
}

internal class UnidirectionalFlatMappedProvider<P, T>(
    staticParent: ProviderImpl<P>,
    transform: (P) -> ProviderImpl<T>
) : AbstractFlatMappedProvider<P, T, ProviderImpl<T>>(staticParent, transform)

internal class BidirectionalFlatMappedProvider<P, T>(
    staticParent: ProviderImpl<P>,
    transform: (P) -> MutableProviderImpl<T>
) : AbstractFlatMappedProvider<P, T, MutableProviderImpl<T>>(staticParent, transform), MutableProviderImpl<T> {
    
    override fun update(value: DeferredValue<T>, ignore: Provider<*>?): Boolean {
        if (dynamicParent.provider.update(value, this)) {
            callUpdate()
            return true
        }
        
        return false
    }
    
}