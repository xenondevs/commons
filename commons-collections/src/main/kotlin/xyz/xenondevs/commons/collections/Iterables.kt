@file:Suppress("NOTHING_TO_INLINE")

package xyz.xenondevs.commons.collections

import java.util.ArrayList
import java.util.NoSuchElementException

inline fun <T> Iterable<T>.findNthOrNull(n: Int, predicate: (T) -> Boolean): T? {
    var count = 0
    for (element in this) {
        if (predicate(element)) {
            if (count == n) return element
            count++
        }
    }
    
    return null
}

inline fun <T> Iterable<T>.findNth(n: Int, predicate: (T) -> Boolean): T {
    return findNthOrNull(n, predicate)
        ?: throw NoSuchElementException("No ${n + 1} element(s) matching predicate found.")
}

inline fun <reified R> Iterable<*>.findNthOfTypeOrNull(n: Int): R? {
    var count = 0
    for (element in this) {
        if (element is R) {
            if (count == n) return element
            count++
        }
    }
    
    return null
}

inline fun <reified R> Iterable<*>.findNthOfType(n: Int): R {
    return findNthOfTypeOrNull<R>(n)
        ?: throw NoSuchElementException("No ${n + 1} element(s) of type ${R::class.java} found.")
}

inline fun <T, K, V> Iterable<T>.associateNotNull(transform: (T) -> Pair<K, V>?): Map<K, V> {
    return associateNotNullTo(LinkedHashMap(), transform)
}

inline fun <T, K, V> Iterable<T>.associateNotNullTo(destination: MutableMap<K, V>, transform: (T) -> Pair<K, V>?): Map<K, V> {
    for (element in this) {
        val pair = transform(element)
        if (pair != null) {
            destination[pair.first] = pair.second
        }
    }
    
    return destination
}

inline fun <K, V> Iterable<V>.associateByNotNull(keySelector: (V) -> K?): Map<K, V> {
    return associateByNotNullTo(LinkedHashMap(), keySelector)
}

inline fun <K, V, M : MutableMap<K, V>> Iterable<V>.associateByNotNullTo(destination: M, keySelector: (V) -> K?): M {
    for (element in this) {
        val key = keySelector(element)
        if (key != null) destination[key] = element
    }
    
    return destination
}

inline fun <K, V> Iterable<K>.associateWithNotNull(valueSelector: (K) -> V?): Map<K, V> {
    return associateWithNotNullTo(LinkedHashMap(), valueSelector)
}

inline fun <K, V, M : MutableMap<K, V>> Iterable<K>.associateWithNotNullTo(destination: M, valueSelector: (K) -> V?): M {
    for (element in this) {
        val value = valueSelector(element)
        if (value != null) destination[element] = value
    }
    
    return destination
}

inline fun <reified R> Iterable<*>.firstInstanceOfOrNull(): R? {
    val iterator = iterator()
    while (iterator.hasNext()) {
        val element = iterator.next()
        
        if (element is R)
            return element
    }
    
    return null
}

inline fun <T, R> Iterable<T>.flatMap(transform: (T) -> Array<R>): List<R> {
    return flatMapTo(ArrayList<R>(), transform)
}

inline fun <T, R, C : MutableCollection<in R>> Iterable<T>.flatMapTo(destination: C, transform: (T) -> Array<R>): C {
    for (element in this) {
        val list = transform(element)
        destination.addAll(list)
    }
    
    return destination
}

inline fun <E> MutableIterable<E>.poll(): E? {
    val iterator = iterator()
    return if (iterator.hasNext()) {
        val element = iterator.next()
        iterator.remove()
        element
    } else null
}

inline fun <E> MutableIterable<E>.removeFirstWhere(test: (E) -> Boolean): Boolean {
    val iterator = iterator()
    while (iterator.hasNext()) {
        if (test(iterator.next())) {
            iterator.remove()
            return true
        }
    }
    
    return false
}

inline fun <E> MutableIterable<E>.pollFirstWhere(test: (E) -> Boolean): E? {
    val iterator = iterator()
    while (iterator.hasNext()) {
        val element = iterator.next()
        if (test(element)) {
            iterator.remove()
            return element
        }
    }
    
    return null
}

inline fun <T> Iterable<T>.sumOf(transform: (T) -> Float): Float {
    var sum = 0f
    for (element in this) {
        sum += transform(element)
    }
    
    return sum
}