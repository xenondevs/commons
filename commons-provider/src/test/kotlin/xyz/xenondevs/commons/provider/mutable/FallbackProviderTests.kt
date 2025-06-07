package xyz.xenondevs.commons.provider.mutable

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import xyz.xenondevs.commons.provider.mutableProvider
import xyz.xenondevs.commons.provider.orElse
import xyz.xenondevs.commons.provider.orElseLazily
import xyz.xenondevs.commons.provider.orElseNew
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class FallbackProviderTests {
    
    @Test
    fun testOrElse() {
        val provider = mutableProvider<Int?>(null)
        val orElse = provider.orElse(-1)
        
        assertEquals(null, provider.get())
        assertEquals(-1, orElse.get())
        
        provider.set(1)
        
        assertEquals(1, provider.get())
        assertEquals(1, orElse.get())
        
        orElse.set(2)
        
        assertEquals(2, provider.get())
        assertEquals(2, orElse.get())
        
        orElse.set(-1)
        
        assertEquals(null, provider.get())
        assertEquals(-1, orElse.get())
    }
    
    @Test
    fun testOrElseLazily() {
        var lazyCalled = 0
        
        val provider = mutableProvider<Int?>(0)
        val orElse = provider.orElseLazily {
            lazyCalled++
            1
        }
        
        orElse.get()
        assertEquals(0, lazyCalled)
        provider.set(null)
        assertEquals(0, lazyCalled)
        orElse.get()
        assertEquals(1, lazyCalled)
        
        // test that lazy lambda is not called more than once
        provider.set(0)
        orElse.get()
        provider.set(null)
        orElse.get()
        assertEquals(1, lazyCalled)
    }
    
    @Test
    fun testOrElseNew() {
        val parent = mutableProvider<MutableSet<Int>?>(null)
        val orElse = parent.orElseNew { mutableSetOf() }
        
        // test that orElse provides an empty set
        val set = orElse.get()
        set += 1
        assertEquals(setOf(1), set)
        assertEquals(setOf(1), orElse.get())
        
        // test that placing a non-empty set in orElse updates parent provider
        orElse.set(mutableSetOf(1))
        assertEquals(setOf(1), orElse.get())    
        assertEquals<Set<Int>?>(setOf(1), parent.get())
        
        // test that placing an empty set in orElse clears parent provider
        orElse.set(mutableSetOf())
        assertEquals(emptySet(), orElse.get())
        assertEquals(null, parent.get())
        
        // test that placing null in the parent updates orElse to empty set
        orElse.set(mutableSetOf(1))
        parent.set(null)
        assertEquals(emptySet(), orElse.get())
    }
    
    @Test
    fun testOrElseValueIsLazy() {
        var evaluated = false
        val root = mutableProvider<Int?> {
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
        
        val root = mutableProvider<Int?> {
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
    
}