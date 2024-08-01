package xyz.xenondevs.commons.provider.mutable

import xyz.xenondevs.commons.collections.observed.ObservableList
import xyz.xenondevs.commons.collections.observed.ObservableMap
import xyz.xenondevs.commons.collections.observed.ObservableSet
import xyz.xenondevs.commons.provider.AbstractProvider
import xyz.xenondevs.commons.provider.Provider

/**
 * Creates and returns a new [MutableProvider] that observes the list of [this][MutableProvider]
 * and propagates changes appropriately.
 */
@JvmName("observedList")
fun <E> MutableProvider<out MutableList<E>>.observed(): MutableProvider<MutableList<E>> =
    ObservedListProvider(this)

/**
 * Creates and returns a new [MutableProvider] that observes the map of [this][MutableProvider]
 * and propagates changes appropriately.
 */
@JvmName("observedMap")
fun <K, V> MutableProvider<out MutableMap<K, V>>.observed(): MutableProvider<MutableMap<K, V>> =
    ObservedMapProvider(this)

/**
 * Creates and returns a new [MutableProvider] that observes the set of [this][MutableProvider]
 * and propagates changes appropriately.
 */
@JvmName("observedSet")
fun <E> MutableProvider<out MutableSet<E>>.observed(): MutableProvider<MutableSet<E>> =
    ObservedSetProvider(this)

private class ObservedListProvider<T>(
    private val provider: MutableProvider<out MutableList<T>>
) : AbstractProvider<MutableList<T>>() {
    
    init {
        provider.addChild(this)
    }
    
    override fun loadValue(): MutableList<T> {
        val list = provider.get()
        return ObservableList(provider.get()) { set(list) }
    }
    
    @Suppress("UNCHECKED_CAST")
    override fun set(value: MutableList<T>, ignoredChildren: Set<Provider<*>>) {
        super.set(value, ignoredChildren)
        (provider as MutableProvider<MutableList<T>>).set(value, setOf(this))
    }
    
}

private class ObservedMapProvider<K, V>(
    private val provider: MutableProvider<out MutableMap<K, V>>
) : AbstractProvider<MutableMap<K, V>>() {
    
    init {
        provider.addChild(this)
    }
    
    override fun loadValue(): MutableMap<K, V> {
        val map = provider.get()
        return ObservableMap(provider.get()) { set(map) }
    }
    
    @Suppress("UNCHECKED_CAST")
    override fun set(value: MutableMap<K, V>, ignoredChildren: Set<Provider<*>>) {
        super.set(value, ignoredChildren)
        (provider as MutableProvider<MutableMap<K, V>>).set(value, setOf(this))
    }
    
}

private class ObservedSetProvider<T>(
    private val provider: MutableProvider<out MutableSet<T>>
) : AbstractProvider<MutableSet<T>>() {
    
    init {
        provider.addChild(this)
    }
    
    override fun loadValue(): MutableSet<T> {
        val set = provider.get()
        return ObservableSet(provider.get()) { set(set) }
    }
    
    @Suppress("UNCHECKED_CAST")
    override fun set(value: MutableSet<T>, ignoredChildren: Set<Provider<*>>) {
        super.set(value, ignoredChildren)
        (provider as MutableProvider<MutableSet<T>>).set(value, setOf(this))
    }
    
}