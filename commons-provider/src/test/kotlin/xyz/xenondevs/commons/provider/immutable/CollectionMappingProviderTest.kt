package xyz.xenondevs.commons.provider.immutable

import org.junit.jupiter.api.Test
import xyz.xenondevs.commons.provider.flatMapCollection
import xyz.xenondevs.commons.provider.flattenIterables
import xyz.xenondevs.commons.provider.mapEach
import xyz.xenondevs.commons.provider.mapEachNotNull
import xyz.xenondevs.commons.provider.mergeMaps
import xyz.xenondevs.commons.provider.provider
import kotlin.test.assertEquals

class CollectionMappingProviderTest {
    
    @Test
    fun testMapEach() {
        val provider = provider(listOf(1, 2, 3))
        val mapped = provider.mapEach { it + 1 }
        
        assertEquals(listOf(1, 2, 3), provider.get())
        assertEquals(listOf(2, 3, 4), mapped.get())
    }
    
    @Test
    fun testMapEachNotNull() {
        val provider = provider(listOf(1, null, null, 2, 3, null))
        val mapped = provider.mapEachNotNull { it?.plus(1) }
        
        assertEquals(listOf(1, null, null, 2, 3, null), provider.get())
        assertEquals(listOf(2, 3, 4), mapped.get())
    }
    
    @Test
    fun testFlatMapCollection() {
        val provider = provider(listOf("AB", "CD"))
        val flatMapped = provider.flatMapCollection { it.toCharArray().asList() }
        
        assertEquals(listOf("AB", "CD"), provider.get())
        assertEquals(listOf('A', 'B', 'C', 'D'), flatMapped.get())
    }
    
    @Test
    fun testFlattenIterables() {
        val provider = provider(listOf(listOf(1, 2), listOf(3, 4)))
        val flattened = provider.flattenIterables()
        
        assertEquals(listOf(listOf(1, 2), listOf(3, 4)), provider.get())
        assertEquals(listOf(1, 2, 3, 4), flattened.get())
    }
    
    @Test
    fun testMerged() {
        val provider = provider(listOf(mapOf("a" to 1, "b" to 2), mapOf("b" to -2, "c" to 3)))
        val merged = provider.mergeMaps()
        
        assertEquals(listOf(mapOf("a" to 1, "b" to 2), mapOf("b" to -2, "c" to 3)), provider.get())
        assertEquals(mapOf("a" to 1, "b" to -2, "c" to 3), merged.get())
    }
    
}