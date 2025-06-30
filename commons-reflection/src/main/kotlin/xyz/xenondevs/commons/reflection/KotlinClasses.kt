package xyz.xenondevs.commons.reflection

import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

fun KClass<*>.isSubclassOfAny(vararg classes: KClass<*>) = classes.any { isSubclassOf(it) }

/**
 * Returns the simple name of this class, including the simple names of all its enclosing classes,
 * or `null` if the class or any enclosing class has no name (for example, if it is an anonymous class).
 */
val KClass<*>.simpleNestedName: String?
    get() {
        var str = simpleName ?: return null
        var enclosingClass = java.enclosingClass
        while (enclosingClass != null) {
            str = "${enclosingClass.simpleName ?: return null}.$str"
            enclosingClass = enclosingClass.enclosingClass
        }
        return str
    }