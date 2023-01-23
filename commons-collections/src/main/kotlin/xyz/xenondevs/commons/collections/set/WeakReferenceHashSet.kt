package xyz.xenondevs.commons.collections.set

import java.lang.ref.ReferenceQueue
import java.lang.ref.WeakReference

internal class WeakReferenceHashSet<T> : HashSet<WeakReference<T>>() {
    
    private val queue = ReferenceQueue<T>()
    
    //<editor-fold desc="overwritten methods to remove stale references", defaultstate="collapsed">
    override fun add(element: WeakReference<T>): Boolean {
        removeStaleReferences()
        return super.add(element)
    }
    
    override fun addAll(elements: Collection<WeakReference<T>>): Boolean {
        removeStaleReferences()
        return super.addAll(elements)
    }
    //</editor-fold>
    
    fun addReferenced(element: T) {
        super.add(WeakReference(element, queue))
    }
    
    operator fun plusAssign(element: T) {
        addReferenced(element)
    }
    
    private fun removeStaleReferences() {
        while (true) {
            val ref = queue.poll() ?: break
            remove(ref)
        }
    }
    
}