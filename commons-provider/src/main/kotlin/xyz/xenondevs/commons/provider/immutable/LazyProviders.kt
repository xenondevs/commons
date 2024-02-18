package xyz.xenondevs.commons.provider.immutable

import xyz.xenondevs.commons.provider.Provider

fun <T> lazyProvider(initializer: () -> Provider<T>): Provider<T> =
    LazyProvider(initializer)

fun <T> lazyProviderWrapper(initializer: () -> T): Provider<T> =
    LazyProvider { ProviderWrapper(initializer()) }

fun <T> combinedLazyProvider(initializer: () -> List<Provider<T>>): Provider<List<T>> =
    LazyProvider { CombinedProvider(initializer()) }

private class LazyProvider<T>(private val initializer: () -> Provider<T>) : Provider<T>() {
    
    private var _provider: Provider<T>? = null
    private val provider: Provider<T>
        get() {
            if (_provider == null)
                _provider = initializer().also { it.addChild(this) }
            
            return _provider!!
        }
    
    override fun loadValue(): T = provider.get()
    
}