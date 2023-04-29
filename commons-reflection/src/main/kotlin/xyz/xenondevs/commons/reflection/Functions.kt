package xyz.xenondevs.commons.reflection

import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodHandles
import java.lang.reflect.Method
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.jvm.javaMethod

fun <T> KFunction<T>.hasEmptyArguments(): Boolean {
    val params = parameters
    
    if (params.isEmpty()) return true
    return params.size == 1 && params[0].kind == KParameter.Kind.INSTANCE
}

fun Method.toMethodHandle(): MethodHandle =
    MethodHandles.lookup().unreflect(this)

fun Method.toMethodHandle(instance: Any): MethodHandle =
    MethodHandles.lookup().unreflect(this).bindTo(instance)

fun KFunction<*>.toMethodHandle(): MethodHandle =
    MethodHandles.lookup().unreflect(javaMethod!!)

fun KFunction<*>.toMethodHandle(instance: Any): MethodHandle =
    MethodHandles.lookup().unreflect(javaMethod!!).bindTo(instance)