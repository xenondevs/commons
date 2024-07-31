package xyz.xenondevs.commons.provider.mutable

import xyz.xenondevs.commons.provider.AbstractProvider
import xyz.xenondevs.commons.provider.Provider

/**
 * Creates and returns a new [MutableProvider] that wraps [this][MutableProvider],
 * makes all methods synchronized and fields volatile.
 */
fun <T> MutableProvider<T>.synchronized(): MutableProvider<T> =
    SynchronizedVolatileMutableProvider(this)

private class SynchronizedVolatileMutableProvider<T>(private val delegate: MutableProvider<T>) : AbstractProvider<T>() {
    
    @Volatile
    override var children: MutableSet<Provider<*>>? = null
    
    @Volatile
    override var subscribers: MutableList<(T) -> Unit>? = null
    
    @Volatile
    override var weakSubscribers: MutableMap<Any, MutableList<(Any, T) -> Unit>>? = null
    
    @Volatile
    override var isInitialized = false
    
    @Volatile
    override var value: T? = null
    
    init {
        delegate.addChild(this)
    }
    
    @Synchronized
    override fun loadValue(): T {
        return delegate.get()
    }
    
    @Synchronized
    override fun set(value: T, ignoredChildren: Set<Provider<*>>) {
        super.set(value, ignoredChildren)
        delegate.set(value, setOf(this))
    }
    
    @Synchronized
    override fun get(): T {
        return super.get()
    }
    
    @Synchronized
    override fun update() {
        super.update()
    }
    
    @Synchronized
    override fun addChild(provider: Provider<*>) {
        super.addChild(provider)
    }
    
    @Synchronized
    override fun subscribe(action: (T) -> Unit) {
        super.subscribe(action)
    }
    
    @Synchronized
    override fun <R : Any> subscribeWeak(owner: R, action: (R, T) -> Unit) {
        super.subscribeWeak(owner, action)
    }
    
    @Synchronized
    override fun removeChild(provider: Provider<*>) {
        super.removeChild(provider)
    }
    
    @Synchronized
    override fun unsubscribe(action: (T) -> Unit) {
        super.unsubscribe(action)
    }
    
    @Synchronized
    override fun <R : Any> unsubscribeWeak(owner: R, action: (R, T) -> Unit) {
        super.unsubscribeWeak(owner, action)
    }
    
    @Synchronized
    override fun <R : Any> unsubscribeWeak(owner: R) {
        super.unsubscribeWeak(owner)
    }
    
}