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

val KType.nonNullTypeArguments: List<KType>
    get() = arguments.map { it.type ?: throw TypeInformationException.starProjection(this) }

inline val <reified K> Map<K, *>.keyType: KType
    get() = typeOf<K>()

inline val <reified V> Map<*, V>.valueType: KType
    get() = typeOf<V>()

fun KClass<*>.createStarProjectedType(
    nullable: Boolean = false,
    annotations: List<Annotation> = emptyList()
) = createType(
    Array(typeParameters.size) { KTypeProjection.STAR }.asList(),
    nullable,
    annotations
)

@JvmName("KClassifierCreateTypeKTypeProjection")
fun KClassifier.createType(
    vararg arguments: KTypeProjection
) = createType(arguments.asList())

@JvmName("KClassifierCreateTypeKType")
fun KClassifier.createType(
    vararg arguments: KType?
) = createType(arguments.map { if (it == null) KTypeProjection.STAR else KTypeProjection.invariant(it) })

@JvmName("createStarProjectedType1")
fun createStarProjectedType(
    classifier: KClass<*>,
    nullable: Boolean = false,
    annotations: List<Annotation> = emptyList()
) = classifier.createStarProjectedType(nullable, annotations)

fun createType(
    classifier: KClassifier,
    arguments: List<KTypeProjection> = emptyList(),
    nullable: Boolean = false,
    annotations: List<Annotation> = emptyList()
) = classifier.createType(arguments, nullable, annotations)

@JvmName("createTypeKTypeProjection")
fun createType(
    classifier: KClassifier,
    vararg arguments: KTypeProjection
) = classifier.createType(*arguments)

@JvmName("createTypeKType")
fun createType(
    classifier: KClassifier,
    vararg arguments: KType?
) = classifier.createType(*arguments)

private class TypeInformationException(message: String?) : Exception(message) {
    
    companion object {
        
        fun starProjection(type: KType): TypeInformationException {
            val nullArguments = type.arguments.asSequence()
                .withIndex()
                .filter { it.value.type == null }
                .map { it.index }
                .joinToString()
            
            return TypeInformationException("Type $type has a star projection as type argument at: ${nullArguments}.")
        }
        
    }
    
}