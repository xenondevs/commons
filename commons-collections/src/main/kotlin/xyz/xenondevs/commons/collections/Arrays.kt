package xyz.xenondevs.commons.collections

import java.util.*

inline fun <T> Array<T?>.getOrSet(index: Int, lazyValue: () -> T): T {
    var value = get(index)
    if (value == null) {
        value = lazyValue()
        set(index, value)
        
        return value
    }
    
    return value
}

inline fun <reified E : Enum<E>> Array<E>.toEnumSet(): EnumSet<E> {
    val set = enumSet<E>()
    for (element in this) {
        set.add(element)
    }
    return set
}

//<editor-fold desc="ObjectArray mapToArray">
inline fun <T, reified R> Array<T>.mapToArray(transform: (T) -> R): Array<R> {
    return Array(size) { transform(get(it)) }
}

inline fun <T> Array<T>.mapToBooleanArray(transform: (T) -> Boolean): BooleanArray {
    return BooleanArray(size) { transform(get(it)) }
}

inline fun <T> Array<T>.mapToByteArray(transform: (T) -> Byte): ByteArray {
    return ByteArray(size) { transform(get(it)) }
}

inline fun <T> Array<T>.mapToCharArray(transform: (T) -> Char): CharArray {
    return CharArray(size) { transform(get(it)) }
}

inline fun <T> Array<T>.mapToShortArray(transform: (T) -> Short): ShortArray {
    return ShortArray(size) { transform(get(it)) }
}

inline fun <T> Array<T>.mapToIntArray(transform: (T) -> Int): IntArray {
    return IntArray(size) { transform(get(it)) }
}

inline fun <T> Array<T>.mapToLongArray(transform: (T) -> Long): LongArray {
    return LongArray(size) { transform(get(it)) }
}

inline fun <T> Array<T>.mapToFloatArray(transform: (T) -> Float): FloatArray {
    return FloatArray(size) { transform(get(it)) }
}

inline fun <T> Array<T>.mapToDoubleArray(transform: (T) -> Double): DoubleArray {
    return DoubleArray(size) { transform(get(it)) }
}
//</editor-fold>

//<editor-fold desc="BooleanArray mapToArray">
inline fun BooleanArray.mapToArray(transform: (Boolean) -> Any): Array<Any> {
    return Array(size) { transform(get(it)) }
}

inline fun BooleanArray.mapToBooleanArray(transform: (Boolean) -> Boolean): BooleanArray {
    return BooleanArray(size) { transform(get(it)) }
}

inline fun BooleanArray.mapToByteArray(transform: (Boolean) -> Byte): ByteArray {
    return ByteArray(size) { transform(get(it)) }
}

inline fun BooleanArray.mapToCharArray(transform: (Boolean) -> Char): CharArray {
    return CharArray(size) { transform(get(it)) }
}

inline fun BooleanArray.mapToShortArray(transform: (Boolean) -> Short): ShortArray {
    return ShortArray(size) { transform(get(it)) }
}

inline fun BooleanArray.mapToIntArray(transform: (Boolean) -> Int): IntArray {
    return IntArray(size) { transform(get(it)) }
}

inline fun BooleanArray.mapToLongArray(transform: (Boolean) -> Long): LongArray {
    return LongArray(size) { transform(get(it)) }
}

inline fun BooleanArray.mapToFloatArray(transform: (Boolean) -> Float): FloatArray {
    return FloatArray(size) { transform(get(it)) }
}

inline fun BooleanArray.mapToDoubleArray(transform: (Boolean) -> Double): DoubleArray {
    return DoubleArray(size) { transform(get(it)) }
}
//</editor-fold>

//<editor-fold desc="ByteArray mapToArray">
inline fun ByteArray.mapToArray(transform: (Byte) -> Any): Array<Any> {
    return Array(size) { transform(get(it)) }
}

inline fun ByteArray.mapToBooleanArray(transform: (Byte) -> Boolean): BooleanArray {
    return BooleanArray(size) { transform(get(it)) }
}

inline fun ByteArray.mapToByteArray(transform: (Byte) -> Byte): ByteArray {
    return ByteArray(size) { transform(get(it)) }
}

inline fun ByteArray.mapToCharArray(transform: (Byte) -> Char): CharArray {
    return CharArray(size) { transform(get(it)) }
}

inline fun ByteArray.mapToShortArray(transform: (Byte) -> Short): ShortArray {
    return ShortArray(size) { transform(get(it)) }
}

inline fun ByteArray.mapToIntArray(transform: (Byte) -> Int): IntArray {
    return IntArray(size) { transform(get(it)) }
}

inline fun ByteArray.mapToLongArray(transform: (Byte) -> Long): LongArray {
    return LongArray(size) { transform(get(it)) }
}

inline fun ByteArray.mapToFloatArray(transform: (Byte) -> Float): FloatArray {
    return FloatArray(size) { transform(get(it)) }
}

inline fun ByteArray.mapToDoubleArray(transform: (Byte) -> Double): DoubleArray {
    return DoubleArray(size) { transform(get(it)) }
}
//</editor-fold>

//<editor-fold desc="CharArray mapToArray">
inline fun CharArray.mapToArray(transform: (Char) -> Any): Array<Any> {
    return Array(size) { transform(get(it)) }
}

inline fun CharArray.mapToBooleanArray(transform: (Char) -> Boolean): BooleanArray {
    return BooleanArray(size) { transform(get(it)) }
}

inline fun CharArray.mapToByteArray(transform: (Char) -> Byte): ByteArray {
    return ByteArray(size) { transform(get(it)) }
}

inline fun CharArray.mapToCharArray(transform: (Char) -> Char): CharArray {
    return CharArray(size) { transform(get(it)) }
}

inline fun CharArray.mapToShortArray(transform: (Char) -> Short): ShortArray {
    return ShortArray(size) { transform(get(it)) }
}

inline fun CharArray.mapToIntArray(transform: (Char) -> Int): IntArray {
    return IntArray(size) { transform(get(it)) }
}

inline fun CharArray.mapToLongArray(transform: (Char) -> Long): LongArray {
    return LongArray(size) { transform(get(it)) }
}

inline fun CharArray.mapToFloatArray(transform: (Char) -> Float): FloatArray {
    return FloatArray(size) { transform(get(it)) }
}

inline fun CharArray.mapToDoubleArray(transform: (Char) -> Double): DoubleArray {
    return DoubleArray(size) { transform(get(it)) }
}
//</editor-fold>

//<editor-fold desc="ShortArray mapToArray">
inline fun ShortArray.mapToArray(transform: (Short) -> Any): Array<Any> {
    return Array(size) { transform(get(it)) }
}

inline fun ShortArray.mapToBooleanArray(transform: (Short) -> Boolean): BooleanArray {
    return BooleanArray(size) { transform(get(it)) }
}

inline fun ShortArray.mapToByteArray(transform: (Short) -> Byte): ByteArray {
    return ByteArray(size) { transform(get(it)) }
}

inline fun ShortArray.mapToCharArray(transform: (Short) -> Char): CharArray {
    return CharArray(size) { transform(get(it)) }
}

inline fun ShortArray.mapToShortArray(transform: (Short) -> Short): ShortArray {
    return ShortArray(size) { transform(get(it)) }
}

inline fun ShortArray.mapToIntArray(transform: (Short) -> Int): IntArray {
    return IntArray(size) { transform(get(it)) }
}

inline fun ShortArray.mapToLongArray(transform: (Short) -> Long): LongArray {
    return LongArray(size) { transform(get(it)) }
}

inline fun ShortArray.mapToFloatArray(transform: (Short) -> Float): FloatArray {
    return FloatArray(size) { transform(get(it)) }
}

inline fun ShortArray.mapToDoubleArray(transform: (Short) -> Double): DoubleArray {
    return DoubleArray(size) { transform(get(it)) }
}
//</editor-fold>

//<editor-fold desc="IntArray mapToArray">
inline fun IntArray.mapToArray(transform: (Int) -> Any): Array<Any> {
    return Array(size) { transform(get(it)) }
}

inline fun IntArray.mapToBooleanArray(transform: (Int) -> Boolean): BooleanArray {
    return BooleanArray(size) { transform(get(it)) }
}

inline fun IntArray.mapToByteArray(transform: (Int) -> Byte): ByteArray {
    return ByteArray(size) { transform(get(it)) }
}

inline fun IntArray.mapToCharArray(transform: (Int) -> Char): CharArray {
    return CharArray(size) { transform(get(it)) }
}

inline fun IntArray.mapToShortArray(transform: (Int) -> Short): ShortArray {
    return ShortArray(size) { transform(get(it)) }
}

inline fun IntArray.mapToIntArray(transform: (Int) -> Int): IntArray {
    return IntArray(size) { transform(get(it)) }
}

inline fun IntArray.mapToLongArray(transform: (Int) -> Long): LongArray {
    return LongArray(size) { transform(get(it)) }
}

inline fun IntArray.mapToFloatArray(transform: (Int) -> Float): FloatArray {
    return FloatArray(size) { transform(get(it)) }
}

inline fun IntArray.mapToDoubleArray(transform: (Int) -> Double): DoubleArray {
    return DoubleArray(size) { transform(get(it)) }
}
//</editor-fold>

//<editor-fold desc="LongArray mapToArray">
inline fun LongArray.mapToArray(transform: (Long) -> Any): Array<Any> {
    return Array(size) { transform(get(it)) }
}

inline fun LongArray.mapToBooleanArray(transform: (Long) -> Boolean): BooleanArray {
    return BooleanArray(size) { transform(get(it)) }
}

inline fun LongArray.mapToByteArray(transform: (Long) -> Byte): ByteArray {
    return ByteArray(size) { transform(get(it)) }
}

inline fun LongArray.mapToCharArray(transform: (Long) -> Char): CharArray {
    return CharArray(size) { transform(get(it)) }
}

inline fun LongArray.mapToShortArray(transform: (Long) -> Short): ShortArray {
    return ShortArray(size) { transform(get(it)) }
}

inline fun LongArray.mapToIntArray(transform: (Long) -> Int): IntArray {
    return IntArray(size) { transform(get(it)) }
}

inline fun LongArray.mapToLongArray(transform: (Long) -> Long): LongArray {
    return LongArray(size) { transform(get(it)) }
}

inline fun LongArray.mapToFloatArray(transform: (Long) -> Float): FloatArray {
    return FloatArray(size) { transform(get(it)) }
}

inline fun LongArray.mapToDoubleArray(transform: (Long) -> Double): DoubleArray {
    return DoubleArray(size) { transform(get(it)) }
}
//</editor-fold>

//<editor-fold desc="FloatArray mapToArray">
inline fun FloatArray.mapToArray(transform: (Float) -> Any): Array<Any> {
    return Array(size) { transform(get(it)) }
}

inline fun FloatArray.mapToBooleanArray(transform: (Float) -> Boolean): BooleanArray {
    return BooleanArray(size) { transform(get(it)) }
}

inline fun FloatArray.mapToByteArray(transform: (Float) -> Byte): ByteArray {
    return ByteArray(size) { transform(get(it)) }
}

inline fun FloatArray.mapToCharArray(transform: (Float) -> Char): CharArray {
    return CharArray(size) { transform(get(it)) }
}

inline fun FloatArray.mapToShortArray(transform: (Float) -> Short): ShortArray {
    return ShortArray(size) { transform(get(it)) }
}

inline fun FloatArray.mapToIntArray(transform: (Float) -> Int): IntArray {
    return IntArray(size) { transform(get(it)) }
}

inline fun FloatArray.mapToLongArray(transform: (Float) -> Long): LongArray {
    return LongArray(size) { transform(get(it)) }
}

inline fun FloatArray.mapToFloatArray(transform: (Float) -> Float): FloatArray {
    return FloatArray(size) { transform(get(it)) }
}

inline fun FloatArray.mapToDoubleArray(transform: (Float) -> Double): DoubleArray {
    return DoubleArray(size) { transform(get(it)) }
}
//</editor-fold>

//<editor-fold desc="DoubleArray mapToArray">
inline fun DoubleArray.mapToArray(transform: (Double) -> Any): Array<Any> {
    return Array(size) { transform(get(it)) }
}

inline fun DoubleArray.mapToBooleanArray(transform: (Double) -> Boolean): BooleanArray {
    return BooleanArray(size) { transform(get(it)) }
}

inline fun DoubleArray.mapToByteArray(transform: (Double) -> Byte): ByteArray {
    return ByteArray(size) { transform(get(it)) }
}

inline fun DoubleArray.mapToCharArray(transform: (Double) -> Char): CharArray {
    return CharArray(size) { transform(get(it)) }
}

inline fun DoubleArray.mapToShortArray(transform: (Double) -> Short): ShortArray {
    return ShortArray(size) { transform(get(it)) }
}

inline fun DoubleArray.mapToIntArray(transform: (Double) -> Int): IntArray {
    return IntArray(size) { transform(get(it)) }
}

inline fun DoubleArray.mapToLongArray(transform: (Double) -> Long): LongArray {
    return LongArray(size) { transform(get(it)) }
}

inline fun DoubleArray.mapToFloatArray(transform: (Double) -> Float): FloatArray {
    return FloatArray(size) { transform(get(it)) }
}

inline fun DoubleArray.mapToDoubleArray(transform: (Double) -> Double): DoubleArray {
    return DoubleArray(size) { transform(get(it)) }
}
//</editor-fold>