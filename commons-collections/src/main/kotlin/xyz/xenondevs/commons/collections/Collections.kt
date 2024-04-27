@file:Suppress("UNCHECKED_CAST")

package xyz.xenondevs.commons.collections

import java.util.*
import kotlin.contracts.contract

fun <E, C : Collection<E>> C.takeUnlessEmpty(): C? = ifEmpty { null }

fun Collection<*>?.isNullOrEmpty(): Boolean {
    contract {
        returns(false) implies (this@isNullOrEmpty != null)
    }
    
    return this == null || isEmpty()
}

fun Collection<*>?.isNotNullOrEmpty(): Boolean {
    contract {
        returns(true) implies (this@isNotNullOrEmpty != null)
    }
    
    return this != null && isNotEmpty()
}

inline fun <T, reified R> Collection<T>.mapToArray(transform: (T) -> R): Array<R> {
    val array = arrayOfNulls<R>(size)
    
    var i = 0
    for (element in this) {
        array[i++] = transform(element)
    }
    
    return array as Array<R>
}

inline fun <T> Collection<T>.mapToBooleanArray(transform: (T) -> Boolean): BooleanArray {
    val array = BooleanArray(size)
    
    var i = 0
    for (element in this) {
        array[i++] = transform(element)
    }
    
    return array
}

inline fun <T> Collection<T>.mapToByteArray(transform: (T) -> Byte): ByteArray {
    val array = ByteArray(size)
    
    var i = 0
    for (element in this) {
        array[i++] = transform(element)
    }
    
    return array
}

inline fun <T> Collection<T>.mapToCharArray(transform: (T) -> Char): CharArray {
    val array = CharArray(size)
    
    var i = 0
    for (element in this) {
        array[i++] = transform(element)
    }
    
    return array
}

inline fun <T> Collection<T>.mapToShortArray(transform: (T) -> Short): ShortArray {
    val array = ShortArray(size)
    
    var i = 0
    for (element in this) {
        array[i++] = transform(element)
    }
    
    return array
}

inline fun <T> Collection<T>.mapToIntArray(transform: (T) -> Int): IntArray {
    val array = IntArray(size)
    
    var i = 0
    for (element in this) {
        array[i++] = transform(element)
    }
    
    return array
}

inline fun <T> Collection<T>.mapToLongArray(transform: (T) -> Long): LongArray {
    val array = LongArray(size)
    
    var i = 0
    for (element in this) {
        array[i++] = transform(element)
    }
    
    return array
}

inline fun <T> Collection<T>.mapToFloatArray(transform: (T) -> Float): FloatArray {
    val array = FloatArray(size)
    
    var i = 0
    for (element in this) {
        array[i++] = transform(element)
    }
    
    return array
}

inline fun <T> Collection<T>.mapToDoubleArray(transform: (T) -> Double): DoubleArray {
    val array = DoubleArray(size)
    
    var i = 0
    for (element in this) {
        array[i++] = transform(element)
    }
    
    return array
}

inline fun <T> Collection<T>.mapToBitSet(transform: (T) -> Boolean): BitSet {
    val bitSet = BitSet(size)
    
    for ((i, element) in this.withIndex()) {
        if (transform(element))
            bitSet.set(i)
    }
    
    return bitSet
}

inline fun <T> Collection<T>.mapToBits(transform: (T) -> Boolean): ByteArray {
    val array = ByteArray((size + 7) / 8)
    
    for ((i, element) in withIndex()) {
        if (transform(element)) {
            array[i / 8] = (array[i / 8].toInt() or (1 shl (i % 8))).toByte()
        }
    }
    
    return array
}

fun <E> Collection<E>.intersects(other: Collection<E>): Boolean {
    if (size > other.size) {
        for (element in other) {
            if (contains(element))
                return true
        }
    } else {
        for (element in this) {
            if (other.contains(element))
                return true
        }
    }
    
    return false
}

inline fun <reified E : Enum<E>> Collection<E>.toEnumSet(): EnumSet<E> {
    if (isEmpty())
        return enumSet()
    
    return EnumSet.copyOf(this)
}
