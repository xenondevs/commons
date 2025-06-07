package xyz.xenondevs.commons.provider.immutable

import org.junit.jupiter.api.Test
import xyz.xenondevs.commons.provider.Provider
import xyz.xenondevs.commons.provider.mapNonNull
import xyz.xenondevs.commons.provider.mutableProvider
import xyz.xenondevs.commons.provider.provider
import kotlin.test.assertEquals

class MappingProviderTest {
    
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
    fun testMapParentsSet() {
        val root = mutableProvider(1)
        val child = root.map { it + 1 }
        val grandChild = child.map { it + 1 }
        
        assertEquals(emptySet(), root.parents)
        assertEquals(setOf(root), child.parents)
        assertEquals(setOf(child), grandChild.parents)
    }
    
    @Test
    fun testMapChildrenSet() {
        val root = mutableProvider(1)
        val child = root.map { it + 1 }
        val grandChild = child.map { it + 1 }
        
        assertEquals(setOf(child), root.children)
        assertEquals(setOf(grandChild), child.children)
        assertEquals(emptySet(), grandChild.children)
    }
    
}