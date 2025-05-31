@file:JvmName("Providers")
@file:JvmMultifileClass

package xyz.xenondevs.commons.provider

import java.lang.ref.WeakReference

/**
 * Creates and returns a new [Provider] that maps to the element at [index] or throws [NoSuchElementException].
 * 
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
@JvmName("listGet")
operator fun <T> Provider<List<T>>.get(index: Int): Provider<T> =
    map { it[index] }

/**
 * Creates and returns a new [Provider] that maps to the element at [index] or throws [NoSuchElementException].
 */
@JvmName("strongListGet")
fun <T> Provider<List<T>>.strongGet(index: Int): Provider<T> =
    strongMap { it[index] }

/**
 * Creates and returns a new [Provider] that maps to the element at [index] or throws [NoSuchElementException].
 * 
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
@JvmName("listGet")
operator fun <T> Provider<List<T>>.get(index: Provider<Int>) =
    combinedProvider(this, index) { list, i -> list[i] }

/**
 * Creates and returns a new [Provider] that maps to the element at [index] or throws [NoSuchElementException].
 */
@JvmName("strongListGet")
fun <T> Provider<List<T>>.strongGet(index: Provider<Int>) =
    strongCombinedProvider(this, index) { list, i -> list[i] }

/**
 * Creates and returns a new [Provider] that maps to the element at [index] or `null` if the index is out of bounds.
 * 
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
@JvmName("listGetOrNull")
fun <T> Provider<List<T>>.getOrNull(index: Int): Provider<T?> =
    map { it.getOrNull(index) }

/**
 * Creates and returns a new [Provider] that maps to the element at [index] or `null` if the index is out of bounds.
 */
@JvmName("strongListGetOrNull")
fun <T> Provider<List<T>>.strongGetOrNull(index: Int): Provider<T?> =
    strongMap { it.getOrNull(index) }

/**
 * Creates and returns a new [Provider] that maps to the element at [index] or `null` if the index is out of bounds.
 * 
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
@JvmName("listGetOrNull")
fun <T> Provider<List<T>>.getOrNull(index: Provider<Int>) =
    combinedProvider(this, index) { list, i -> list.getOrNull(i) }

/**
 * Creates and returns a new [Provider] that maps to the element at [index] or `null` if the index is out of bounds.
 */
@JvmName("strongListGetOrNull")
fun <T> Provider<List<T>>.strongGetOrNull(index: Provider<Int>) =
    strongCombinedProvider(this, index) { list, i -> list.getOrNull(i) }

/**
 * Creates and returns a new [Provider] that maps to the element at [index] or the element at the closest valid index.
 * 
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
@JvmName("listGetCoerced")
fun <T> Provider<List<T>>.getCoerced(index: Int): Provider<T> =
    map { it[index.coerceIn(0..<it.size)] }

/**
 * Creates and returns a new [Provider] that maps to the element at [index] or the element at the closest valid index.
 */
@JvmName("strongListGetCoerced")
fun <T> Provider<List<T>>.strongGetCoerced(index: Int): Provider<T> =
    strongMap { it[index.coerceIn(0..<it.size)] }

/**
 * Creates and returns a new [Provider] that maps to the element at [index] or the element at the closest valid index.
 * 
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
@JvmName("listGetCoerced")
fun <T> Provider<List<T>>.getCoerced(index: Provider<Int>) =
    combinedProvider(this, index) { list, i -> list[i.coerceIn(0..<list.size)] }

/**
 * Creates and returns a new [Provider] that maps to the element at [index] or the element at the closest valid index.
 */
@JvmName("strongListGetCoerced")
fun <T> Provider<List<T>>.strongGetCoerced(index: Provider<Int>) =
    strongCombinedProvider(this, index) { list, i -> list[i.coerceIn(0..<list.size)] }

/**
 * Creates and returns a new [Provider] that maps to the element at [index] modulo the list size.
 * 
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
@JvmName("listGetMod")
fun <T> Provider<List<T>>.getMod(index: Int): Provider<T> =
    map { it[index % it.size] }

/**
 * Creates and returns a new [Provider] that maps to the element at [index] modulo the list size.
 */
@JvmName("strongListGetMod")
fun <T> Provider<List<T>>.strongGetMod(index: Int): Provider<T> =
    strongMap { it[index % it.size] }

/**
 * Creates and returns a new [Provider] that maps to the element at [index] modulo the list size.
 * 
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
@JvmName("listGetMod")
fun <T> Provider<List<T>>.getMod(index: Provider<Int>) =
    combinedProvider(this, index) { list, i -> list[i % list.size] }

/**
 * Creates and returns a new [Provider] that maps to the element at [index] modulo the list size.
 */
@JvmName("strongListGetMod")
fun <T> Provider<List<T>>.strongGetMod(index: Provider<Int>) =
    strongCombinedProvider(this, index) { list, i -> list[i % list.size] }

/**
 * Creates and returns a new [Provider] that maps to the element at [index] or throws [NoSuchElementException].
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
@JvmName("arrayGet")
operator fun <T> Provider<Array<T>>.get(index: Int): Provider<T> =
    map { it[index] }

/**
 * Creates and returns a new [Provider] that maps to the element at [index] or throws [NoSuchElementException].
 */
@JvmName("strongArrayGet")
fun <T> Provider<Array<T>>.strongGet(index: Int): Provider<T> =
    strongMap { it[index] }

/**
 * Creates and returns a new [Provider] that maps to the element at [index] or throws [NoSuchElementException].
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
@JvmName("arrayGet")
operator fun <T> Provider<Array<T>>.get(index: Provider<Int>) =
    combinedProvider(this, index) { list, i -> list[i] }

/**
 * Creates and returns a new [Provider] that maps to the element at [index] or throws [NoSuchElementException].
 */
@JvmName("strongArrayGet")
fun <T> Provider<Array<T>>.strongGet(index: Provider<Int>) =
    strongCombinedProvider(this, index) { list, i -> list[i] }

/**
 * Creates and returns a new [Provider] that maps to the element at [index] or `null` if the index is out of bounds.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
@JvmName("arrayGetOrNull")
fun <T> Provider<Array<T>>.getOrNull(index: Int): Provider<T?> =
    map { it.getOrNull(index) }

/**
 * Creates and returns a new [Provider] that maps to the element at [index] or `null` if the index is out of bounds.
 */
@JvmName("strongArrayGetOrNull")
fun <T> Provider<Array<T>>.strongGetOrNull(index: Int): Provider<T?> =
    strongMap { it.getOrNull(index) }

/**
 * Creates and returns a new [Provider] that maps to the element at [index] or `null` if the index is out of bounds.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
@JvmName("arrayGetOrNull")
fun <T> Provider<Array<T>>.getOrNull(index: Provider<Int>) =
    combinedProvider(this, index) { list, i -> list.getOrNull(i) }

/**
 * Creates and returns a new [Provider] that maps to the element at [index] or `null` if the index is out of bounds.
 */
@JvmName("strongArrayGetOrNull")
fun <T> Provider<Array<T>>.strongGetOrNull(index: Provider<Int>) =
    strongCombinedProvider(this, index) { list, i -> list.getOrNull(i) }

/**
 * Creates and returns a new [Provider] that maps to the element at [index] or the element at the closest valid index.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
@JvmName("arrayGetCoerced")
fun <T> Provider<Array<T>>.getCoerced(index: Int): Provider<T> =
    map { it[index.coerceIn(0..<it.size)] }

/**
 * Creates and returns a new [Provider] that maps to the element at [index] or the element at the closest valid index.
 */
@JvmName("strongArrayGetCoerced")
fun <T> Provider<Array<T>>.strongGetCoerced(index: Int): Provider<T> =
    strongMap { it[index.coerceIn(0..<it.size)] }

/**
 * Creates and returns a new [Provider] that maps to the element at [index] or the element at the closest valid index.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
@JvmName("arrayGetCoerced")
fun <T> Provider<Array<T>>.getCoerced(index: Provider<Int>) =
    combinedProvider(this, index) { list, i -> list[i.coerceIn(0..<list.size)] }

/**
 * Creates and returns a new [Provider] that maps to the element at [index] or the element at the closest valid index.
 */
@JvmName("strongArrayGetCoerced")
fun <T> Provider<Array<T>>.strongGetCoerced(index: Provider<Int>) =
    strongCombinedProvider(this, index) { list, i -> list[i.coerceIn(0..<list.size)] }

/**
 * Creates and returns a new [Provider] that maps to the element at [index] modulo the array size.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
@JvmName("arrayGetMod")
fun <T> Provider<Array<T>>.getMod(index: Int): Provider<T> =
    map { it[index % it.size] }

/**
 * Creates and returns a new [Provider] that maps to the element at [index] modulo the array size.
 */
@JvmName("strongArrayGetMod")
fun <T> Provider<Array<T>>.strongGetMod(index: Int): Provider<T> =
    strongMap { it[index % it.size] }

/**
 * Creates and returns a new [Provider] that maps to the element at [index] modulo the array size.
 * 
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
@JvmName("arrayGetMod")
fun <T> Provider<Array<T>>.getMod(index: Provider<Int>) =
    combinedProvider(this, index) { list, i -> list[i % list.size] }

/**
 * Creates and returns a new [Provider] that maps to the element at [index] modulo the array size.
 */
@JvmName("strongArrayGetMod")
fun <T> Provider<Array<T>>.strongGetMod(index: Provider<Int>) =
    strongCombinedProvider(this, index) { list, i -> list[i % list.size] }