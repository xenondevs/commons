package xyz.xenondevs.commons.reflection

import kotlin.reflect.KFunction
import kotlin.reflect.KParameter

fun <T> KFunction<T>.hasEmptyArguments(): Boolean {
    val params = parameters
    
    if (params.isEmpty()) return true
    return params.size == 1 && params[0].kind == KParameter.Kind.INSTANCE
}