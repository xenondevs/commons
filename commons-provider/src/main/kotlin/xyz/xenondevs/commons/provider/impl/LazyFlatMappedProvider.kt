package xyz.xenondevs.commons.provider.impl

import xyz.xenondevs.commons.provider.DeferredValue
import xyz.xenondevs.commons.provider.MutableProvider
import xyz.xenondevs.commons.provider.Provider
import kotlin.math.max

// TODO: does this break the contract of value never changing?
private class LambdaDelegatedDeferredValue<T>(val getDelegate: () -> DeferredValue<T>, val getMinState: () -> Long) : DeferredValue<T> {
    
    override val seqNo: Long get() = max(getMinState(), getDelegate().seqNo)
    override val value: T get() = getDelegate().value
    
}

internal abstract class AbstractLazyFlatMappedProvider<P, T, DP : Provider<T>>(
    private val staticParent: Provider<P>,
    private val transform: (P) -> DP
) : AbstractProvider<T>() {
    
    @Volatile
    protected var lazyDynamicParent: Lazy<DynamicParent<DP>> = lazy(::createDynamicParent)
    
    override val parents: Set<Provider<*>>
        get() {
            val ldp = lazyDynamicParent
            if (ldp.isInitialized()) {
                return setOf(staticParent, ldp.value.provider)
            } else {
                return setOf(staticParent)
            }
        }
    
    override val value: DeferredValue<T>
        get() {
            val ldp = lazyDynamicParent
            return LambdaDelegatedDeferredValue(
                { ldp.value.provider.value },
                { ldp.value.state }
            )
        }
    
    // TODO: are there problems with update handlers?
    override fun handleParentUpdated(updatedParent: Provider<*>) {
        val ldp = lazyDynamicParent
        if (ldp.isInitialized() && updatedParent == ldp.value.provider) {
            updateHandlers.notify()
        } else if (updatedParent == staticParent) {
            synchronized(this) {
                val ldp1 = lazyDynamicParent
                if (!ldp1.isInitialized())
                    return
                
                val staticParentValue = staticParent.value
                if (ldp1.value.state > staticParentValue.seqNo)
                    return
                
                // reset lazyDynamicParent
                ldp1.value.provider.removeWeakChild(this)
                lazyDynamicParent = lazy(::createDynamicParent)
            }
            
            updateHandlers.notify()
        }
    }
    
    private fun createDynamicParent(): DynamicParent<DP> {
        val staticParentValue = staticParent.value
        val dynamicParent = transform(staticParentValue.value)
        dynamicParent.addWeakChild(this)
        return DynamicParent(staticParentValue.seqNo, dynamicParent)
    }
    
}

internal class UnidirectionalLazyFlatMappedProvider<P, T>(
    staticParent: Provider<P>,
    transform: (P) -> Provider<T>
) : AbstractLazyFlatMappedProvider<P, T, Provider<T>>(staticParent, transform)

internal class BidirectionalLazyFlatMappedProvider<P, T>(
    staticParent: Provider<P>,
    transform: (P) -> MutableProvider<T>
) : AbstractLazyFlatMappedProvider<P, T, MutableProvider<T>>(staticParent, transform), MutableProviderDefaults<T> {
    
    override fun update(value: DeferredValue<T>, ignore: Set<Provider<*>>): Boolean {
        if (lazyDynamicParent.value.provider.update(value, setOf(this))) {
            updateHandlers.notify(ignore)
            return true
        }
        
        return false
    }
    
}