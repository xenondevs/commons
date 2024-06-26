package xyz.xenondevs.commons.provider.mutable

import org.junit.jupiter.api.Test
import xyz.xenondevs.commons.provider.Provider
import kotlin.test.assertEquals

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
    fun testOrElseProvider() {
        val provider = mutableProvider<Int?>(null)
        val mutFallback = mutableProvider(1)
        val fallback: Provider<Int> = mutFallback
        val orElse = provider.orElse(fallback)
        
        assertEquals(null, provider.get())
        assertEquals(1, fallback.get())
        assertEquals(1, orElse.get())
        
        mutFallback.set(2)
        
        assertEquals(null, provider.get())
        assertEquals(2, fallback.get())
        assertEquals(2, orElse.get())
        
        orElse.set(3)
        
        assertEquals(3, provider.get())
        assertEquals(2, fallback.get())
        assertEquals(3, orElse.get())
    }
    
    @Test
    fun testOrElseNullableProvider() {
        val provider = mutableProvider<Int?>(null)
        val fallback = mutableProvider<Int?>(null)
        val orElse = provider.orElse(fallback)
        
        assertEquals(null, provider.get())
        assertEquals(null, fallback.get())
        assertEquals(null, orElse.get())
        
        fallback.set(1)
        
        assertEquals(null, provider.get())
        assertEquals(1, fallback.get())
        assertEquals(1, orElse.get())
        
        orElse.set(2)
        
        assertEquals(2, provider.get())
        assertEquals(1, fallback.get())
        assertEquals(2, orElse.get())
        
        orElse.set(null)
        
        assertEquals(null, provider.get())
        assertEquals(null, fallback.get())
        assertEquals(null, orElse.get())
    }
    
}