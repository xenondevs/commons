package xyz.xenondevs.commons.provider.util

import java.util.function.BiFunction
import java.util.function.Function

internal class ObservableMap<K, V>(
    val map: MutableMap<K, V>,
    private val handleUpdate: () -> Unit
) : MutableMap<K, V> by map {
    
    override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
        get() = ObservableSet(map.entries, handleUpdate)
    override val keys: MutableSet<K>
        get() = ObservableSet(map.keys, handleUpdate)
    override val values: MutableCollection<V>
        get() = ObservableCollection(map.values, handleUpdate)
    
    override fun clear() {
        map.clear()
        handleUpdate()
    }
    
    override fun put(key: K, value: V): V? {
        val v = map.put(key, value)
        handleUpdate()
        return v
    }
    
    override fun putAll(from: Map<out K, V>) {
        map.putAll(from)
        handleUpdate()
    }
    
    override fun remove(key: K): V? {
        val v = map.remove(key)
        handleUpdate()
        return v
    }
    
    override fun remove(key: K, value: V): Boolean {
        val b = map.remove(key, value)
        handleUpdate()
        return b
    }
    
    override fun compute(key: K, remappingFunction: BiFunction<in K, in V?, out V?>): V? {
        val v = map.compute(key, remappingFunction)
        handleUpdate()
        return v
    }
    
    override fun computeIfAbsent(key: K, mappingFunction: Function<in K, out V>): V {
        val v = map.computeIfAbsent(key, mappingFunction)
        handleUpdate()
        return v
    }
    
    override fun computeIfPresent(key: K, remappingFunction: BiFunction<in K, in V & Any, out V?>): V? {
        val v = map.computeIfPresent(key, remappingFunction)
        handleUpdate()
        return v
    }
    
    override fun merge(key: K, value: V & Any, remappingFunction: BiFunction<in V & Any, in V & Any, out V?>): V? {
        val v = map.merge(key, value, remappingFunction)
        handleUpdate()
        return v
    }
    
    override fun putIfAbsent(key: K, value: V): V? {
        val v = map.putIfAbsent(key, value)
        handleUpdate()
        return v
    }
    
    override fun replace(key: K, oldValue: V, newValue: V): Boolean {
        val b = map.replace(key, oldValue, newValue)
        handleUpdate()
        return b
    }
    
    override fun replace(key: K, value: V): V? {
        val v = map.replace(key, value)
        handleUpdate()
        return v
    }
    
    override fun replaceAll(function: BiFunction<in K, in V, out V>) {
        map.replaceAll(function)
        handleUpdate()
    }
    
    override fun equals(other: Any?): Boolean = map == other
    override fun hashCode(): Int = map.hashCode()
    override fun toString(): String = map.toString()
    
}