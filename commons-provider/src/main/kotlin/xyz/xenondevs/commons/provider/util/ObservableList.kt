package xyz.xenondevs.commons.provider.util

import java.util.function.Predicate
import java.util.function.UnaryOperator

internal class ObservableList<E>(
    val list: MutableList<E>,
    private val handleUpdate: () -> Unit
): MutableList<E> by list {
    
    override fun add(element: E): Boolean {
        val result = list.add(element)
        handleUpdate()
        return result
    }
    
    override fun add(index: Int, element: E) {
        list.add(index, element)
        handleUpdate()
    }
    
    override fun addAll(index: Int, elements: Collection<E>): Boolean {
        val result = list.addAll(index, elements)
        handleUpdate()
        return result
    }
    
    override fun addAll(elements: Collection<E>): Boolean {
        val result = list.addAll(elements)
        handleUpdate()
        return result
    }
    
    override fun clear() {
        list.clear()
        handleUpdate()
    }
    
    override fun remove(element: E): Boolean {
        val result = list.remove(element)
        handleUpdate()
        return result
    }
    
    override fun removeAll(elements: Collection<E>): Boolean {
        val result = list.removeAll(elements)
        handleUpdate()
        return result
    }
    
    override fun removeAt(index: Int): E {
        val element = list.removeAt(index)
        handleUpdate()
        return element
    }
    
    override fun replaceAll(operator: UnaryOperator<E>) {
        list.replaceAll(operator)
        handleUpdate()
    }
    
    override fun retainAll(elements: Collection<E>): Boolean {
        val result = list.retainAll(elements)
        handleUpdate()
        return result
    }
    
    override fun set(index: Int, element: E): E {
        val oldElement = list.set(index, element)
        handleUpdate()
        return oldElement
    }
    
    override fun removeIf(filter: Predicate<in E>): Boolean {
        val result = list.removeIf(filter)
        handleUpdate()
        return result
    }
    
    override fun sort(c: Comparator<in E>) {
        list.sortWith(c)
        handleUpdate()
    }
    
    override fun subList(fromIndex: Int, toIndex: Int): MutableList<E> =
        ObservableList(list.subList(fromIndex, toIndex), handleUpdate)
    
    override fun listIterator(): MutableListIterator<E> =
        ObservableListIterator(list.listIterator(), handleUpdate)
    
    override fun listIterator(index: Int): MutableListIterator<E> =
        ObservableListIterator(list.listIterator(index), handleUpdate)
    
    override fun equals(other: Any?): Boolean = list == other
    override fun hashCode(): Int = list.hashCode()
    override fun toString(): String = list.toString()
    
}