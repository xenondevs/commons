@file:Suppress("PackageDirectoryMismatch") // needs to be in root package for sealed hierarchy, impl directory is used for source file organization
package xyz.xenondevs.commons.provider

internal abstract class AbstractLazyFlatMappedProvider<P, T, DP : ProviderImpl<T>>(
    private val staticParent: ProviderImpl<P>,
    private val transform: (P) -> DP
) : AbstractChildContainingProvider<T>(), HasParents {
    
    @Volatile
    protected var lazyDynamicParent: Lazy<DynamicParent<DP>> = lazy(::createDynamicParent)
    
    override val value: DeferredValue<T>
        get() {
            val ldp = lazyDynamicParent
            return DeferredValue.DelegateLambda(
                { ldp.value.provider.value },
                { ldp.value.state }
            )
        }
    
    override fun handleParentUpdated(updatedParent: ProviderImpl<*>) {
        val ldp = lazyDynamicParent
        if (ldp.isInitialized() && updatedParent == ldp.value.provider) {
            callUpdate()
        } else if (updatedParent == staticParent) {
            synchronized(this) {
                val ldp1 = lazyDynamicParent
                if (!ldp1.isInitialized())
                    return
                
                val staticParentValue = staticParent.value
                if (ldp1.value.state > staticParentValue.state)
                    return
                
                // reset lazyDynamicParent
                (ldp1.value.provider as? ProviderWithChildren<*>)?.removeWeakChild(this)
                lazyDynamicParent = lazy(::createDynamicParent)
            }
            
            callUpdate()
        }
    }
    
    protected fun callUpdate() {
        for (runnable in prepareNotifiers()) {
            runnable()
        }
    }
    
    private fun createDynamicParent(): DynamicParent<DP> {
        val staticParentValue = staticParent.value
        val dynamicParent = transform(staticParentValue.value)
        if (dynamicParent is ProviderWithChildren<*>) {
            dynamicParent.addWeakChild(this)
        }
        return DynamicParent(staticParentValue.state, dynamicParent)
    }
    
}

internal class UnidirectionalLazyFlatMappedProvider<P, T>(
    staticParent: ProviderImpl<P>,
    transform: (P) -> ProviderImpl<T>
) : AbstractLazyFlatMappedProvider<P, T, ProviderImpl<T>>(staticParent, transform)

internal class BidirectionalLazyFlatMappedProvider<P, T>(
    staticParent: ProviderImpl<P>,
    transform: (P) -> MutableProviderImpl<T>
) : AbstractLazyFlatMappedProvider<P, T, MutableProviderImpl<T>>(staticParent, transform), MutableProviderImpl<T> {
    
    override fun update(value: DeferredValue<T>, ignore: Provider<*>?): Boolean {
        if (lazyDynamicParent.value.provider.update(value, this)) {
            callUpdate()
            return true
        }
        
        return false
    }
    
}