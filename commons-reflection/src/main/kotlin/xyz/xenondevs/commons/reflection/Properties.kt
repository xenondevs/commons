@file:Suppress("UNCHECKED_CAST")

package xyz.xenondevs.commons.reflection

import kotlin.jvm.internal.CallableReference
import kotlin.jvm.internal.PropertyReference0Impl
import kotlin.reflect.KProperty0
import kotlin.reflect.KProperty1
import kotlin.reflect.KProperty2
import kotlin.reflect.full.memberExtensionProperties
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

fun KProperty0<*>.hasRuntimeDelegate(): Boolean {
    return getRuntimeDelegate() != null
}

fun KProperty0<*>.getRuntimeDelegate(): Any? {
    require(this is CallableReference)
    
    val receiver = boundReceiver
    if (receiver == CallableReference.NO_RECEIVER) {
        isAccessible = true
        return getDelegate()
    }
    
    val property = PropertyReference0Impl(receiver, receiver::class.java, name, signature, 0) as KProperty0<*>
    property.isAccessible = true
    return property.getDelegate()
}

fun <T> KProperty1<T, *>.hasRuntimeDelegate(receiver: T & Any): Boolean {
    return getRuntimeDelegate(receiver) != null
}

fun <T> KProperty1<T, *>.getRuntimeDelegate(receiver: T & Any): Any? {
    val property = receiver::class.memberProperties.first { it.name == name } as KProperty1<T, *>
    property.isAccessible = true
    return property.getDelegate(receiver)
}

fun <T, E> KProperty2<T, E, *>.hasRuntimeDelegate(receiver: T & Any): Boolean {
    return getRuntimeDelegate(receiver) != null
}

fun <T, E> KProperty2<T, E, *>.getRuntimeDelegate(receiver: T & Any): Any? {
    val property = receiver::class.memberExtensionProperties.first { it.name == name } as KProperty2<T, E, *>
    property.isAccessible = true
    return property.getDelegate(receiver, null as E)
}