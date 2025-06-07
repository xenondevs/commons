package xyz.xenondevs.commons.provider.mutable

import org.junit.jupiter.api.Test
import xyz.xenondevs.commons.provider.mutableProvider
import xyz.xenondevs.commons.provider.observed
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ObservedProviderTests {
    
    @Test
    fun testObservedMap() {
        var map = mapOf("a" to 1)
        
        val mapProvider = mutableProvider({ map }, { map = it })
        val mappedProvider = mapProvider.map(
            { it.mapValuesTo(LinkedHashMap()) { (_, v) -> v.toString() } },
            { it.mapValues { (_, v) -> v.toInt() } }
        )
        val observedProvider = mappedProvider.observed()
        val mappedProvider2 = observedProvider.map { it.mapValues { (_, v) -> v.toInt() } }
        
        // initial state
        assertEquals(mapOf("a" to "1"), observedProvider.get())
        assertEquals(mapOf("a" to 1), mappedProvider2.get())
        
        // mapped provider is not observed, so changes will not propagate
        mappedProvider.get()["b"] = "2"
        assertEquals(mapOf("a" to 1), map)
        assertEquals(mapOf("a" to "1", "b" to "2"), observedProvider.get())
        assertEquals(mapOf("a" to 1), mappedProvider2.get())
        
        // mutation on observed provider's map will propagate
        observedProvider.get()["c"] = "3"
        assertEquals(mapOf("a" to 1, "b" to 2, "c" to 3), map)
        assertEquals(mapOf("a" to "1", "b" to "2", "c" to "3"), observedProvider.get())
        assertEquals(mapOf("a" to 1, "b" to 2, "c" to 3), mappedProvider2.get())
    }
    
    @Test
    fun testOldMapUnobserved() {
        val mapA = mutableMapOf("a" to 1)
        val mapB = mutableMapOf("b" to 1)
        
        var updateCount = 0
        
        val mapProvider = mutableProvider(mapA)
        val observedProvider = mapProvider.observed()
        observedProvider.subscribe { updateCount++ }
        
        assertEquals(0, updateCount)
        
        val observedMapA = observedProvider.get()
        observedMapA["a"] = 2
        assertEquals(1, updateCount)
        
        mapProvider.set(mapB)
        val observedMapB = observedProvider.get()
        
        observedMapA["a"] = 3
        assertEquals(2, updateCount)
        
        observedMapB["b"] = 2
        assertEquals(3, updateCount)
    }
    
    @Test
    fun testObservedSet() {
        var set = setOf(1)
        
        val listProvider = mutableProvider({ set }, { set = it })
        val mappedProvider = listProvider.map(
            { it.mapTo(LinkedHashSet()) { e -> e.toString() } },
            { it.mapTo(LinkedHashSet()) { e -> e.toInt() } }
        )
        val observedProvider = mappedProvider.observed()
        val mappedProvider2 = observedProvider.map { it.mapTo(LinkedHashSet()) { e -> e.toInt() } }
        
        // initial state
        assertEquals(setOf("1"), observedProvider.get())
        assertEquals(setOf(1), mappedProvider2.get())
        
        // mapped provider is not observed, so changes will not propagate
        mappedProvider.get().add("2")
        assertEquals(setOf(1), set)
        assertEquals(setOf("1", "2"), observedProvider.get())
        assertEquals(setOf(1), mappedProvider2.get())
        
        // mutation on observed provider's list will propagate
        observedProvider.get().add("3")
        assertEquals(setOf(1, 2, 3), set)
        assertEquals(setOf("1", "2", "3"), observedProvider.get())
        assertEquals(setOf(1, 2, 3), mappedProvider2.get())
    }
    
    @Test
    fun testOldSetUnobserved() {
        val setA = mutableSetOf(1)
        val setB = mutableSetOf(1)
        
        var updateCount = 0
        
        val setProvider = mutableProvider(setA)
        val observedProvider = setProvider.observed()
        observedProvider.subscribe { updateCount++ }
        
        assertEquals(0, updateCount)
        
        val observedSetA = observedProvider.get()
        observedSetA += 2
        assertEquals(1, updateCount)
        
        setProvider.set(setB)
        val observedSetB = observedProvider.get()
        
        observedSetA += 3
        assertEquals(2, updateCount)
        
        observedSetB += 2
        assertEquals(3, updateCount)
    }
    
    @Test
    fun testObservedList() {
        var list = listOf(1)
        
        val listProvider = mutableProvider({ list }, { list = it })
        val mappedProvider = listProvider.map(
            { it.mapTo(ArrayList()) { e -> e.toString() } },
            { it.map { e -> e.toInt() } }
        )
        val observedProvider = mappedProvider.observed()
        val mappedProvider2 = observedProvider.map { it.map { e -> e.toInt() } }
        
        // initial state
        assertEquals(listOf("1"), observedProvider.get())
        assertEquals(listOf(1), mappedProvider2.get())
        
        // mapped provider is not observed, so changes will not propagate
        mappedProvider.get().add("2")
        assertEquals(listOf(1), list)
        assertEquals(listOf("1", "2"), observedProvider.get())
        assertEquals(listOf(1), mappedProvider2.get())
        
        // mutation on observed provider's list will propagate
        observedProvider.get().add("3")
        assertEquals(listOf(1, 2, 3), list)
        assertEquals(listOf("1", "2", "3"), observedProvider.get())
        assertEquals(listOf(1, 2, 3), mappedProvider2.get())
    }
    
    @Test
    fun testOldListUnobserved() {
        val listA = mutableListOf(1)
        val listB = mutableListOf(1)
        
        var updateCount = 0
        
        val listProvider = mutableProvider(listA)
        val observedProvider = listProvider.observed()
        observedProvider.subscribe { updateCount++ }
        
        assertEquals(0, updateCount)
        
        val observedListA = observedProvider.get()
        observedListA += 2
        assertEquals(1, updateCount)
        
        listProvider.set(listB)
        val observedListB = observedProvider.get()
        
        observedListA += 3
        assertEquals(2, updateCount)
        
        observedListB += 2
        assertEquals(3, updateCount)
    }
    
    @Test
    fun testObservedIsLazy() {
        var rootEvaluated = false
        val root = mutableProvider { 
            rootEvaluated = true
            mutableListOf<Int>()
        }
        val child = root.observed()
        
        assertFalse(rootEvaluated)
        child.get()
        assertTrue(rootEvaluated)
    }
    
    @Test
    fun testObservedProviderParentsSet() {
        val root = mutableProvider(mutableListOf<Int>())
        val child = root.observed()
        
        assertEquals(emptySet(), root.parents)
        assertEquals(setOf(root), child.parents)
    }
    
    @Test
    fun testObservedProviderChildrenSet() {
        val root = mutableProvider(mutableListOf<Int>())
        val child = root.observed()
        
        assertEquals(setOf(child), root.children)
        assertEquals(emptySet(), child.children)
    }
    
}