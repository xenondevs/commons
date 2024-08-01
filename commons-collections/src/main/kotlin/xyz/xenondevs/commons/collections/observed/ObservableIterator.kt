package xyz.xenondevs.commons.collections.observed

class ObservableIterator<T>(
    private val iterator: MutableIterator<T>,
    private val handleUpdate: () -> Unit
) : MutableIterator<T> by iterator {
    
    override fun remove() {
        iterator.remove()
        handleUpdate()
    }
    
    override fun equals(other: Any?): Boolean = iterator == other
    override fun hashCode(): Int = iterator.hashCode()
    override fun toString(): String = iterator.toString()
    
}