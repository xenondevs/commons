package xyz.xenondevs.commons.provider.mutable

import org.junit.jupiter.api.Test
import xyz.xenondevs.commons.provider.immutable.map
import kotlin.test.assertEquals

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
    
}