package xyz.xenondevs.commons.provider.mutable

import org.junit.jupiter.api.Test
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
        
        // initial state
        assertEquals(mapOf("a" to "1"), observedProvider.get())
        
        // mapped provider is not observed, so changes will not propagate upwards
        mappedProvider.get()["b"] = "2"
        assertEquals(mapOf("a" to "1", "b" to "2"), observedProvider.get())
        assertEquals(mapOf("a" to 1), map)
        
        // mutation on observed provider's map will propagate upwards
        observedProvider.get()["c"] = "3"
        assertEquals(mapOf("a" to "1", "b" to "2", "c" to "3"), observedProvider.get())
        assertEquals(mapOf("a" to 1, "b" to 2, "c" to 3), map)
        
        // changing provider value will propagate upwards
        observedProvider.set(mutableMapOf("a" to "0"))
        assertEquals(mapOf("a" to "0"), observedProvider.get())
        assertEquals(mapOf("a" to 0), map)
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
        
        // initial state
        assertEquals(setOf("1"), observedProvider.get())
        
        // mapped provider is not observed, so changes will not propagate upwards
        mappedProvider.get().add("2")
        assertEquals(setOf("1", "2"), observedProvider.get())
        assertEquals(setOf(1), set)
        
        // mutation on observed provider's list will propagate upwards
        observedProvider.get().add("3")
        assertEquals(setOf("1", "2", "3"), observedProvider.get())
        assertEquals(setOf(1, 2, 3), set)
        
        // changing provider value will propagate upwards
        observedProvider.set(mutableSetOf("0"))
        assertEquals(setOf("0"), observedProvider.get())
        assertEquals(setOf(0), set)
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
        
        // initial state
        assertEquals(listOf("1"), observedProvider.get())
        
        // mapped provider is not observed, so changes will not propagate upwards
        mappedProvider.get().add("2")
        assertEquals(listOf("1", "2"), observedProvider.get())
        assertEquals(listOf(1), list)
        
        // mutation on observed provider's list will propagate upwards
        observedProvider.get().add("3")
        assertEquals(listOf("1", "2", "3"), observedProvider.get())
        assertEquals(listOf(1, 2, 3), list)
        
        // changing provider value will propagate upwards
        observedProvider.set(mutableListOf("0"))
        assertEquals(listOf("0"), observedProvider.get())
        assertEquals(listOf(0), list)
    }
    
}