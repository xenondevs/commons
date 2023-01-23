@file:Suppress("UnusedReceiverParameter")

package xyz.xenondevs.commons.reflection

import kotlin.reflect.KClass
import kotlin.reflect.KClassifier
import kotlin.reflect.KType
import kotlin.reflect.KTypeProjection
import kotlin.reflect.full.createType
import kotlin.reflect.typeOf

val KType.classifierClass: KClass<*>?
    get() = classifier as? KClass<*>

inline val <reified T> T.type: KType
    get() = typeOf<T>()

inline val <reified T> T.typeArguments: List<KType?>
    get() = type.arguments.map { it.type }

inline val <reified K> Map<K, *>.keyType: KType
    get() = typeOf<K>()

inline val <reified V> Map<*, V>.valueType: KType
    get() = typeOf<V>()

fun createType(
    classifier: KClassifier,
    arguments: List<KTypeProjection> = emptyList(),
    nullable: Boolean = false,
    annotations: List<Annotation> = emptyList()
) = classifier.createType(arguments, nullable, annotations)