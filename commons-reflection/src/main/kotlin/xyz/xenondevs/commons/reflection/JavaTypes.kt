package xyz.xenondevs.commons.reflection

import java.lang.reflect.GenericArrayType
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.WildcardType
import kotlin.Array
import kotlin.UnsupportedOperationException
import kotlin.reflect.jvm.javaType
import kotlin.reflect.typeOf
import java.lang.reflect.Array as ReflectArray

inline fun <reified T> javaTypeOf(): Type = typeOf<T>().javaType

val Type.rawType: Class<*>
    get() = when (this) {
        is ParameterizedType -> rawType as Class<*>
        is WildcardType -> upperBounds[0] as Class<*>
        is GenericArrayType -> ReflectArray.newInstance(genericComponentType as Class<*>, 0)::class.java
        is Class<*> -> this
        else -> throw UnsupportedOperationException()
    }

class ParameterizedTypeImpl(
    private val ownerType: Type?,
    private val rawType: Type,
    private vararg val actualTypeArguments: Type
) : ParameterizedType {
    
    override fun getOwnerType(): Type? = ownerType
    override fun getRawType(): Type = rawType
    override fun getActualTypeArguments(): Array<out Type> = actualTypeArguments
    
}

class GenericArrayTypeImpl(
    private val genericComponentType: Type
) : GenericArrayType {
    
    override fun getGenericComponentType() = genericComponentType
    
}

class WildcardTypeImpl(
    private val upperBounds: Array<out Type>,
    private val lowerBounds: Array<out Type>
) : WildcardType {
    
    override fun getUpperBounds(): Array<out Type> = upperBounds
    override fun getLowerBounds(): Array<out Type> = lowerBounds
    
}