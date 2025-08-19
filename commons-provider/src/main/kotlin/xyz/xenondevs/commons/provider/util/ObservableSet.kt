package xyz.xenondevs.commons.provider.util

internal class ObservableSet<E>(
    val set: MutableSet<E>,
    private val handleUpdate: () -> Unit,
) : MutableSet<E> by set {
    
    override fun add(element: E): Boolean {
        val result = set.add(element)
        handleUpdate()
        return result
    }
    
    override fun addAll(elements: Collection<E>): Boolean {
        val result = set.addAll(elements)
        handleUpdate()
        return result
    }
    
    override fun clear() {
        set.clear()
        handleUpdate()
    }
    
    override fun remove(element: E): Boolean {
        val result = set.remove(element)
        handleUpdate()
        return result
    }
    
    @Suppress("ConvertArgumentToSet")
    override fun removeAll(elements: Collection<E>): Boolean {
        val result = set.removeAll(elements)
        handleUpdate()
        return result
    }
    
    @Suppress("ConvertArgumentToSet")
    override fun retainAll(elements: Collection<E>): Boolean {
        val result = set.retainAll(elements)
        handleUpdate()
        return result
    }
    
    override fun iterator(): MutableIterator<E> =
        ObservableIterator(set.iterator(), handleUpdate)
    
    override fun equals(other: Any?): Boolean = set == other
    override fun hashCode(): Int = set.hashCode()
    override fun toString(): String = set.toString()
    
}