@file:Suppress("NOTHING_TO_INLINE", "UNCHECKED_CAST")

package xyz.xenondevs.commons.collections

import java.util.*
import kotlin.contracts.contract

inline fun <K, V> treeMapOf(vararg pairs: Pair<K, V>): TreeMap<K, V> {
    return TreeMap<K, V>().apply { putAll(pairs) }
}

inline fun <reified K : Enum<K>, V> enumMapOf(vararg pairs: Pair<K, V>): EnumMap<K, V> {
    return EnumMap<K, V>(K::class.java).apply { putAll(pairs) }
}

inline fun <reified K : Enum<K>, V> enumMap(): EnumMap<K, V>  {
    return EnumMap<K, V>(K::class.java)
}

inline fun <reified K : Enum<K>, V> Map<K, V>.toEnumMap(): EnumMap<K, V> {
    return toMap(EnumMap(K::class.java))
}

inline fun Map<*, *>?.isNullOrEmpty(): Boolean {
    contract {
        returns(false) implies (this@isNullOrEmpty != null)
    }
    
    return this == null || isEmpty()
}

inline fun Map<*, *>?.isNotNullOrEmpty(): Boolean {
    contract {
        returns(true) implies (this@isNotNullOrEmpty != null)
    }
    
    return this != null && isNotEmpty()
}

inline fun <K, V, M : Map<K, V>> M.takeUnlessEmpty(): M? = ifEmpty { null }

inline fun <K, V> Map<K, V>.selectValues(keys: Iterable<K>): List<V> {
    return selectValuesTo(ArrayList(), keys)
}

inline fun <K, V, C : MutableCollection<in V>> Map<K, V>.selectValuesTo(destination: C, keys: Iterable<K>): C {
    for (key in keys) {
        destination += get(key)!!
    }
    
    return destination
}

inline fun <reified R, K, V> Map<K, V>.filterIsInstanceKeys(): Map<R, V> {
    return filterIsInstanceKeysTo(LinkedHashMap())
}

inline fun <reified R, K, V, C : MutableMap<R, V>> Map<K, V>.filterIsInstanceKeysTo(destination: C): C {
    for ((key, value) in this) {
        if (key is R) destination[key] = value
    }
    
    return destination
}

inline fun <reified R, K, V> Map<K, V>.filterIsInstanceValues(): Map<K, R> {
    return filterIsInstanceValuesTo(LinkedHashMap())
}

inline fun <reified R, K, V, C : MutableMap<K, R>> Map<K, V>.filterIsInstanceValuesTo(destination: C): C {
    for ((key, value) in this) {
        if (value is R) destination[key] = value
    }
    
    return destination
}

inline fun <K, V : Any> Map<K, V?>.filterValuesNotNull(): Map<K, V> {
    return filterValuesNotNullTo(LinkedHashMap())
}

inline fun <K, V : Any, C : MutableMap<K, V>> Map<K, V?>.filterValuesNotNullTo(destination: C): C {
    return filterTo(destination as MutableMap<K, V?>) { (_, value) -> value != null } as C
}

inline fun <K, V, R> Map<K, V>.mapKeysNotNull(keySelector: (Map.Entry<K, V>) -> R?): Map<R, V> {
    return mapKeysNotNullTo(LinkedHashMap(), keySelector)
}

inline fun <K, V, R, M : MutableMap<R, V>> Map<K, V>.mapKeysNotNullTo(destination: M, keySelector: (Map.Entry<K, V>) -> R?): M {
    for (entry in this.entries) {
        val key = keySelector(entry)
        if (key != null) destination[key] = entry.value
    }
    
    return destination
}

inline fun <K, V, R> Map<K, V>.mapValuesNotNull(valueSelector: (Map.Entry<K, V>) -> R?): Map<K, R> {
    return mapValuesNotNullTo(LinkedHashMap(), valueSelector)
}

inline fun <K, V, R, M : MutableMap<K, R>> Map<K, V>.mapValuesNotNullTo(destination: M, valueSelector: (Map.Entry<K, V>) -> R?): M {
    for (entry in this.entries) {
        val value = valueSelector(entry)
        if (value != null) destination[entry.key] = value
    }
    
    return destination
}

inline fun <K, V, R> Map<K, V>.flatMap(transform: (Map.Entry<K, V>) -> Array<R>): List<R> {
    return flatMapTo(ArrayList<R>(), transform)
}

inline fun <K, V, R, C : MutableCollection<in R>> Map<K, V>.flatMapTo(destination: C, transform: (Map.Entry<K, V>) -> Array<R>): C {
    for (element in this) {
        val list = transform(element)
        destination.addAll(list)
    }
    
    return destination
}

inline fun <K, V> MutableMap<K, V>.poll(): Map.Entry<K, V>? {
    return entries.poll()
}

inline fun <K, V> MutableMap<K, V>.putOrRemove(key: K, value: V?) {
    if (value == null) remove(key) else put(key, value)
}

inline fun <K, V> MutableMap<K, V>.removeIf(predicate: (Map.Entry<K, V>) -> Boolean): MutableMap<K, V> {
    val iterator = iterator()
    while (iterator.hasNext()) {
        val entry = iterator.next()
        if (predicate(entry)) iterator.remove()
    }
    
    return this
}