@file:Suppress("UNCHECKED_CAST")

package xyz.xenondevs.commons.reflection

import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodHandles
import java.lang.reflect.Method
import kotlin.jvm.functions.FunctionN
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

/**
 * A utility function to call lambdas using a variable amount of arguments.
 * 
 * This function is a workaround until proper [reflection on lambdas](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect.jvm/reflect.html) is implemented.
 */
fun <V> Function<V>.call(vararg args: Any?): V {
    return when (args.size) {
        0 -> (this as Function0<V>).invoke()
        1 -> (this as Function1<Any?, V>).invoke(args[0])
        2 -> (this as Function2<Any?, Any?, V>).invoke(args[0], args[1])
        3 -> (this as Function3<Any?, Any?, Any?, V>).invoke(args[0], args[1], args[2])
        4 -> (this as Function4<Any?, Any?, Any?, Any?, V>).invoke(args[0], args[1], args[2], args[3])
        5 -> (this as Function5<Any?, Any?, Any?, Any?, Any?, V>).invoke(args[0], args[1], args[2], args[3], args[4])
        6 -> (this as Function6<Any?, Any?, Any?, Any?, Any?, Any?, V>).invoke(args[0], args[1], args[2], args[3], args[4], args[5])
        7 -> (this as Function7<Any?, Any?, Any?, Any?, Any?, Any?, Any?, V>).invoke(args[0], args[1], args[2], args[3], args[4], args[5], args[6])
        8 -> (this as Function8<Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, V>).invoke(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7])
        9 -> (this as Function9<Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, V>).invoke(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8])
        10 -> (this as Function10<Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, V>).invoke(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9])
        11 -> (this as Function11<Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, V>).invoke(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10])
        12 -> (this as Function12<Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, V>).invoke(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11])
        13 -> (this as Function13<Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, V>).invoke(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12])
        14 -> (this as Function14<Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, V>).invoke(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13])
        15 -> (this as Function15<Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, V>).invoke(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14])
        16 -> (this as Function16<Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, V>).invoke(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15])
        17 -> (this as Function17<Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, V>).invoke(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16])
        18 -> (this as Function18<Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, V>).invoke(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16], args[17])
        19 -> (this as Function19<Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, V>).invoke(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16], args[17], args[18])
        20 -> (this as Function20<Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, V>).invoke(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16], args[17], args[18], args[19])
        21 -> (this as Function21<Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, V>).invoke(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16], args[17], args[18], args[19], args[20])
        22 -> (this as Function22<Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, Any?, V>).invoke(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16], args[17], args[18], args[19], args[20], args[21])
        else -> (this as FunctionN<V>).invoke(*args)
    }
}