package xyz.xenondevs.commons.collections

import java.util.*

object CollectionUtils {
    
    fun <T> sortDependencies(collection: Collection<T>, dependenciesMapper: (T) -> Set<T>): List<T> {
        val dependencies = collection.associateWith { dependenciesMapper(it).filter(collection::contains) }
        val inp = collection.toMutableList()
        val out = LinkedList<T>()
        while (inp.isNotEmpty()) {
            inp.removeIf {
                if (out.containsAll(dependencies[it]!!)) {
                    out.add(it)
                    return@removeIf true
                }
                return@removeIf false
            }
        }
        return out
    }
    
}