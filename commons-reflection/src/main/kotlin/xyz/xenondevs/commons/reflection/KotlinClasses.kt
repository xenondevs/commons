package xyz.xenondevs.commons.reflection

import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

fun KClass<*>.isSubclassOfAny(vararg classes: KClass<*>) = classes.any { isSubclassOf(it) }