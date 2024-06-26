package xyz.xenondevs.commons.provider.immutable

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import xyz.xenondevs.commons.provider.Provider
import xyz.xenondevs.commons.provider.mutable.mutableProvider
import kotlin.test.assertEquals

class MappingProviderTests {
    
    @Test
    fun testMap() {
        val provider = provider(1)
        val mapped = provider.map { it + 1 }
        
        assertEquals(1, provider.get())
        assertEquals(2, mapped.get())
    }
    
    @Test
    fun testMapNonNull() {
        val mutProvider = mutableProvider<Int?>(null)
        val provider: Provider<Int?> = mutProvider
        
        val mapped = provider.mapNonNull { it + 1 }
        
        assertEquals(null, provider.get())
        assertEquals(null, mapped.get())
        
        mutProvider.set(1)
        
        assertEquals(1, provider.get())
        assertEquals(2, mapped.get())
    }
    
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
    fun testOrElse() {
        val mutProvider = mutableProvider<Int?>(null)
        val provider: Provider<Int?> = mutProvider
        val orElse = provider.orElse(1)
        
        assertEquals(null, provider.get())
        assertEquals(1, orElse.get())
        
        mutProvider.set(2)
        
        assertEquals(2, provider.get())
        assertEquals(2, orElse.get())
    }
    
    @Test
    fun testOrElseChained() {
        val mutProvider = mutableProvider<Int?>(null)
        val provider: Provider<Int?> = mutProvider
        val orElse1: Provider<Int?> = provider.orElse(null)
        val orElse2: Provider<Int> = orElse1.orElse(1)
        
        assertEquals(null, provider.get())
        assertEquals(null, orElse1.get())
        assertEquals(1, orElse2.get())
        
        mutProvider.set(2)
        
        assertEquals(2, provider.get())
        assertEquals(2, orElse1.get())
        assertEquals(2, orElse2.get())
    }
    
    @Test
    fun testOrElseProvider() {
        val mutProvider = mutableProvider<Int?>(null)
        val provider: Provider<Int?> = mutProvider
        val fallbackProvider = mutableProvider<Int?>(null)
        
        val orElse = provider.orElse(fallbackProvider)
        
        assertEquals(null, provider.get())
        assertEquals(null, fallbackProvider.get())
        assertEquals(null, orElse.get())
        
        fallbackProvider.set(1)
        
        assertEquals(null, provider.get())
        assertEquals(1, fallbackProvider.get())
        assertEquals(1, orElse.get())
        
        mutProvider.set(2)
        
        assertEquals(2, provider.get())
        assertEquals(1, fallbackProvider.get())
        assertEquals(2, orElse.get())
        
        fallbackProvider.set(null)
        
        assertEquals(2, provider.get())
        assertEquals(null, fallbackProvider.get())
        assertEquals(2, orElse.get())
    }
    
    @Test
    fun testRequire() {
        val provider = mutableProvider(-1)
        val required = provider.require({ it >= 0 }, { "Positive value" })
        
        assertThrows<IllegalArgumentException> { required.get() }
        provider.set(1)
        assertEquals(1, required.get())
    }
    
    @Test
    fun testRequireNonNull() {
        val provider = mutableProvider<Int?>(null)
        val required = provider.requireNotNull()
        
        assertThrows<IllegalArgumentException> { required.get() }
        provider.set(1)
        assertEquals(1, required.get())
    }
    
    @Test
    fun testFlatMap() {
        val provider = provider(listOf("AB", "CD"))
        val flatMapped = provider.flatMap { it.toCharArray().asList() }
        
        assertEquals(listOf("AB", "CD"), provider.get())
        assertEquals(listOf('A', 'B', 'C', 'D'), flatMapped.get())
    }
    
    @Test
    fun testFlatten() {
        val provider = provider(listOf(listOf(1, 2), listOf(3, 4)))
        val flattened = provider.flatten()
        
        assertEquals(listOf(listOf(1, 2), listOf(3, 4)), provider.get())
        assertEquals(listOf(1, 2, 3, 4), flattened.get())
    }
    
    @Test
    fun testMerged() {
        val provider = provider(listOf(mapOf("a" to 1, "b" to 2), mapOf("b" to -2, "c" to 3)))
        val merged = provider.merged()
        
        assertEquals(listOf(mapOf("a" to 1, "b" to 2), mapOf("b" to -2, "c" to 3)), provider.get())
        assertEquals(mapOf("a" to 1, "b" to -2, "c" to 3), merged.get())
    }
    
}