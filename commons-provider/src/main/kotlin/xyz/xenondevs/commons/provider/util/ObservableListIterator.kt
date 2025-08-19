package xyz.xenondevs.commons.provider.util

internal class ObservableListIterator<T>(
    val iterator: MutableListIterator<T>,
    private val handleUpdate: () -> Unit
): MutableListIterator<T> by iterator {
    
    override fun add(element: T) {
        iterator.add(element)
        handleUpdate()
    }
    
    override fun remove() {
        iterator.remove()
        handleUpdate()
    }
    
    override fun set(element: T) {
        iterator.set(element)
        handleUpdate()
    }
    
    override fun equals(other: Any?): Boolean = iterator == other
    override fun hashCode(): Int = iterator.hashCode()
    override fun toString(): String = iterator.toString()
    
}