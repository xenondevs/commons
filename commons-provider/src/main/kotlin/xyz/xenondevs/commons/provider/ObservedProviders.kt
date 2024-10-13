@file:OptIn(UnstableProviderApi::class)

package xyz.xenondevs.commons.provider

import xyz.xenondevs.commons.collections.observed.ObservableList
import xyz.xenondevs.commons.collections.observed.ObservableMap
import xyz.xenondevs.commons.collections.observed.ObservableSet
import kotlin.concurrent.withLock

/**
 * Creates and returns a new [Provider] that observes the list of [this][MutableProvider]
 * and propagates changes appropriately.
 */
@JvmName("observedList")
fun <E> MutableProvider<out MutableList<E>>.observed(): Provider<MutableList<E>> =
    ObservedListProvider(this as AbstractProvider<MutableList<E>>)

/**
 * Creates and returns a new [Provider] that observes the map of [this][MutableProvider]
 * and propagates changes appropriately.
 */
@JvmName("observedMap")
fun <K, V> MutableProvider<out MutableMap<K, V>>.observed(): Provider<MutableMap<K, V>> =
    ObservedMapProvider(this as AbstractProvider<MutableMap<K, V>>)

/**
 * Creates and returns a new [Provider] that observes the set of [this][MutableProvider]
 * and propagates changes appropriately.
 */
@JvmName("observedSet")
fun <E> MutableProvider<out MutableSet<E>>.observed(): Provider<MutableSet<E>> =
    ObservedSetProvider(this as AbstractProvider<MutableSet<E>>)

private class ObservedListProvider<T>(
    private val parent: AbstractProvider<MutableList<T>>
) : AbstractProvider<MutableList<T>>(parent.lock) {
    
    init {
        lock.withLock {
            parent.addChild(this)
            addParent(parent) { lock.withLock { (it as ObservableList<T>).list } }
        }
    }
    
    override fun pull(): MutableList<T> {
        val list = parent.get()
        return ObservableList(list) { lock.withLock { onSelfChanged() } }
    }
    
}

private class ObservedMapProvider<K, V>(
    private val parent: AbstractProvider<MutableMap<K, V>>
) : AbstractProvider<MutableMap<K, V>>(parent.lock) {
    
    init {
        lock.withLock {
            parent.addChild(this)
            addParent(parent) { (it as ObservableMap<K, V>).map }
        }
    }
    
    override fun pull(): MutableMap<K, V> {
        val map = parent.get()
        return ObservableMap(map) { lock.withLock { onSelfChanged() } }
    }
    
}

private class ObservedSetProvider<T>(
    private val parent: AbstractProvider<MutableSet<T>>
) : AbstractProvider<MutableSet<T>>(parent.lock) {
    
    init {
        lock.withLock {
            parent.addChild(this)
            addParent(parent) { (it as ObservableSet<T>).set }
        }
    }
    
    override fun pull(): MutableSet<T> {
        val set = parent.get()
        return ObservableSet(set) { lock.withLock { onSelfChanged() } }
    }
    
}