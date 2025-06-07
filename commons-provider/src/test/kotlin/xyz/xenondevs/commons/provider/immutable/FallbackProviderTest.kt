package xyz.xenondevs.commons.provider.immutable

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import xyz.xenondevs.commons.provider.Provider
import xyz.xenondevs.commons.provider.mutableProvider
import xyz.xenondevs.commons.provider.orElse
import xyz.xenondevs.commons.provider.orElseLazily
import xyz.xenondevs.commons.provider.orElseNew
import xyz.xenondevs.commons.provider.provider
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class FallbackProviderTest {
    
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
    fun testOrElseNew() {
        val mutProvider = mutableProvider<MutableList<Int>?>(null)
        val provider: Provider<MutableList<Int>?> = mutProvider
        val orElse = provider.orElseNew { mutableListOf() }
        
        assertEquals(null, provider.get())
        assertEquals(emptyList(), orElse.get())
        
        orElse.get().add(1)
        assertEquals(listOf(1), orElse.get())
        mutProvider.set(null)
        assertEquals(emptyList(), orElse.get())
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
    fun testOrElseNullableProvider() {
        val provider = mutableProvider<Int?>(null)
        var fallback: Provider<Int>? = provider(10)
        val orElse1 = provider.orElse(fallback)
        fallback = null
        val orElse2 = provider.orElse(fallback as Provider<Int>?)
        
        assertEquals(null, provider.get())
        assertEquals(10, orElse1.get())
        assertEquals(null, orElse2.get())
        assertTrue(provider === orElse2)
        
        provider.set(1)
        
        assertEquals(1, provider.get())
        assertEquals(1, orElse1.get())
        assertEquals(1, orElse2.get())
    }
    
    @Test
    fun testOrElseValueIsLazy() {
        var evaluated = false
        val root = provider<Int?> {
            evaluated = true
            null
        }
        val result = root.orElse(42)
        
        assertFalse(evaluated)
        result.get()
        assertTrue(evaluated)
    }
    
    @Test
    fun testOrElseLazilyIsLazy() {
        var rootEvaluated = false
        var fallbackEvaluated = false
        
        val root = provider<Int?> {
            rootEvaluated = true
            null
        }
        val result = root.orElseLazily {
            fallbackEvaluated = true
            42
        }
        
        assertFalse(rootEvaluated)
        assertFalse(fallbackEvaluated)
        
        result.get()
        
        assertTrue(rootEvaluated)
        assertTrue(fallbackEvaluated)
    }
    
    @Test
    fun testOrElseProviderIsLazy() {
        var rootEvaluated = false
        var fallbackEvaluated = false
        
        val root = provider<Int?> {
            rootEvaluated = true
            null
        }
        val fallback = provider {
            fallbackEvaluated = true
            42
        }
        val result = root.orElse(fallback)
        
        assertFalse(rootEvaluated)
        assertFalse(fallbackEvaluated)
        
        result.get()
        
        assertTrue(rootEvaluated)
        assertTrue(fallbackEvaluated)
    }
    
    @Test
    fun testOrElseProviderParentsSet() {
        val root = mutableProvider<Int?>(null)
        val fallback = mutableProvider(42)
        val result = root.orElse(fallback)
        
        assertEquals(emptySet(), root.parents)
        assertEquals(emptySet(), fallback.parents)
        assertEquals(setOf(root, fallback), result.parents)
    }
    
    @Test
    fun testOrElseProviderChildrenSet() {
        val root = mutableProvider<Int?>(null)
        val fallback = mutableProvider(42)
        val result = root.orElse(fallback)
        
        assertEquals(setOf(result), root.children)
        assertEquals(setOf(result), fallback.children)
        assertEquals(emptySet(), result.children)
    }
    
}