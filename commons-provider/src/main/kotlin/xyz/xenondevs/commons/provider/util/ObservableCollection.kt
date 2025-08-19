package xyz.xenondevs.commons.provider.util

import java.util.function.Predicate

internal class ObservableCollection<E>(
    val collection: MutableCollection<E>,
    private val handleUpdate: () -> Unit
) : MutableCollection<E> by collection {
    
    override fun add(element: E): Boolean {
        val result = collection.add(element)
        handleUpdate()
        return result
    }
    
    override fun addAll(elements: Collection<E>): Boolean {
        val result = collection.addAll(elements)
        handleUpdate()
        return result
    }
    
    override fun clear() {
        collection.clear()
        handleUpdate()
    }
    
    override fun remove(element: E): Boolean {
        val result = collection.remove(element)
        handleUpdate()
        return result
    }
    
    @Suppress("ConvertArgumentToSet")
    override fun removeAll(elements: Collection<E>): Boolean {
        val result = collection.removeAll(elements)
        handleUpdate()
        return result
    }
    
    override fun removeIf(filter: Predicate<in E>): Boolean {
        val result = collection.removeIf(filter)
        handleUpdate()
        return result
    }
    
    @Suppress("ConvertArgumentToSet")
    override fun retainAll(elements: Collection<E>): Boolean {
        val result = collection.retainAll(elements)
        handleUpdate()
        return result
    }
    
    override fun iterator(): MutableIterator<E> =
        ObservableIterator(collection.iterator(), handleUpdate)
    
    override fun equals(other: Any?): Boolean = collection == other
    override fun hashCode(): Int = collection.hashCode()
    override fun toString(): String = collection.toString()
    
}