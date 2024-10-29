@file:JvmName("Providers")
@file:JvmMultifileClass
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
fun <E> Provider<MutableList<E>>.observed(): Provider<MutableList<E>> =
    ObservedListProvider(this as AbstractProvider<MutableList<E>>, mutable = false, weak = false)

/**
 * Creates and returns a new [Provider] that observes the map of [this][MutableProvider]
 * and propagates changes appropriately.
 */
@JvmName("observedMap")
fun <K, V> Provider<MutableMap<K, V>>.observed(): Provider<MutableMap<K, V>> =
    ObservedMapProvider(this as AbstractProvider<MutableMap<K, V>>, mutable = false, weak = false)

/**
 * Creates and returns a new [Provider] that observes the set of [this][MutableProvider]
 * and propagates changes appropriately.
 */
@JvmName("observedSet")
fun <E> Provider<MutableSet<E>>.observed(): Provider<MutableSet<E>> =
    ObservedSetProvider(this as AbstractProvider<MutableSet<E>>, mutable = false, weak = false)

/**
 * Creates and returns a new [Provider] that observes the list of [this][MutableProvider]
 * and propagates changes appropriately.
 */
@JvmName("observedList")
fun <E> MutableProvider<out MutableList<E>>.observed(): Provider<MutableList<E>> =
    ObservedListProvider(this as AbstractProvider<MutableList<E>>, mutable = true, weak = false)

/**
 * Creates and returns a new [Provider] that observes the map of [this][MutableProvider]
 * and propagates changes appropriately.
 */
@JvmName("observedMap")
fun <K, V> MutableProvider<out MutableMap<K, V>>.observed(): Provider<MutableMap<K, V>> =
    ObservedMapProvider(this as AbstractProvider<MutableMap<K, V>>, mutable = true, weak = false)

/**
 * Creates and returns a new [Provider] that observes the set of [this][MutableProvider]
 * and propagates changes appropriately.
 */
@JvmName("observedSet")
fun <E> MutableProvider<out MutableSet<E>>.observed(): Provider<MutableSet<E>> =
    ObservedSetProvider(this as AbstractProvider<MutableSet<E>>, mutable = true, weak = false)

/**
 * Creates and returns a new [Provider] that observes the list of [this][MutableProvider]
 * and propagates changes appropriately.
 */
@JvmName("weakObservedList")
fun <E> Provider<MutableList<E>>.weakObserved(): Provider<MutableList<E>> =
    ObservedListProvider(this as AbstractProvider<MutableList<E>>, mutable = false, weak = true)

/**
 * Creates and returns a new [Provider] that observes the map of [this][MutableProvider]
 * and propagates changes appropriately.
 */
@JvmName("weakObservedMap")
fun <K, V> Provider<MutableMap<K, V>>.weakObserved(): Provider<MutableMap<K, V>> =
    ObservedMapProvider(this as AbstractProvider<MutableMap<K, V>>, mutable = false, weak = true)

/**
 * Creates and returns a new [Provider] that observes the set of [this][MutableProvider]
 * and propagates changes appropriately.
 */
@JvmName("weakObservedSet")
fun <E> Provider<MutableSet<E>>.weakObserved(): Provider<MutableSet<E>> =
    ObservedSetProvider(this as AbstractProvider<MutableSet<E>>, mutable = false, weak = true)

/**
 * Creates and returns a new [Provider] that observes the list of [this][MutableProvider]
 * and propagates changes appropriately.
 */
@JvmName("weakObservedList")
fun <E> MutableProvider<out MutableList<E>>.weakObserved(): Provider<MutableList<E>> =
    ObservedListProvider(this as AbstractProvider<MutableList<E>>, mutable = true, weak = true)

/**
 * Creates and returns a new [Provider] that observes the map of [this][MutableProvider]
 * and propagates changes appropriately.
 */
@JvmName("weakObservedMap")
fun <K, V> MutableProvider<out MutableMap<K, V>>.weakObserved(): Provider<MutableMap<K, V>> =
    ObservedMapProvider(this as AbstractProvider<MutableMap<K, V>>, mutable = true, weak = true)

/**
 * Creates and returns a new [Provider] that observes the set of [this][MutableProvider]
 * and propagates changes appropriately.
 */
@JvmName("weakObservedSet")
fun <E> MutableProvider<out MutableSet<E>>.weakObserved(): Provider<MutableSet<E>> =
    ObservedSetProvider(this as AbstractProvider<MutableSet<E>>, mutable = true, weak = true)

private class ObservedListProvider<T>(
    private val parent: AbstractProvider<MutableList<T>>,
    mutable: Boolean,
    weak: Boolean
) : AbstractProvider<MutableList<T>>(parent.lock) {
    
    private var observedList: ObservableList<T>? = null
    
    init {
        lock.withLock {
            parent.addChild(active = true, weak = weak, child = this)
            if (mutable) {
                addParent(parent) { lock.withLock { (it as ObservableList<T>).list } }
            } else {
                addInactiveParent(parent)
            }
        }
    }
    
    override fun pull(): MutableList<T> {
        val list = parent.get()
        var observedList: ObservableList<T>? = null
        observedList = ObservableList(list) {
            if (this.observedList === observedList) {
                val preparedSubscribers = ArrayList<() -> Unit>(0)
                lock.withLock { onSelfChanged(preparedSubscribers) }
                preparedSubscribers.forEach { it() }
            }
        }
        this.observedList = observedList
        return observedList
    }
    
}

private class ObservedMapProvider<K, V>(
    private val parent: AbstractProvider<MutableMap<K, V>>,
    mutable: Boolean,
    weak: Boolean
) : AbstractProvider<MutableMap<K, V>>(parent.lock) {
    
    private var observedMap: ObservableMap<K, V>? = null
    
    init {
        lock.withLock {
            parent.addChild(active = true, weak = weak, child = this)
            if (mutable) {
                addParent(parent) { (it as ObservableMap<K, V>).map }
            } else {
                addInactiveParent(parent)
            }
        }
    }
    
    override fun pull(): MutableMap<K, V> {
        val map = parent.get()
        var observedMap: ObservableMap<K, V>? = null
        observedMap = ObservableMap(map) {
            if (this.observedMap === observedMap) {
                val preparedSubscribers = ArrayList<() -> Unit>(0)
                lock.withLock { onSelfChanged(preparedSubscribers) }
                preparedSubscribers.forEach { it() }
            }
        }
        this.observedMap = observedMap
        return observedMap
    }
    
}

private class ObservedSetProvider<T>(
    private val parent: AbstractProvider<MutableSet<T>>,
    mutable: Boolean,
    weak: Boolean
) : AbstractProvider<MutableSet<T>>(parent.lock) {
    
    private var observedSet: ObservableSet<T>? = null
    
    init {
        lock.withLock {
            parent.addChild(active = true, weak = weak, child = this)
            if (mutable) {
                addParent(parent) { (it as ObservableSet<T>).set }
            } else {
                addInactiveParent(parent)
            }
        }
    }
    
    override fun pull(): MutableSet<T> {
        val set = parent.get()
        var observedSet: ObservableSet<T>? = null
        observedSet = ObservableSet(set) {
            if (this.observedSet === observedSet) {
                val preparedSubscribers = ArrayList<() -> Unit>(0)
                lock.withLock { onSelfChanged(preparedSubscribers) }
                preparedSubscribers.forEach { it() }
            }
        }
        this.observedSet = observedSet
        return observedSet
    }
    
}