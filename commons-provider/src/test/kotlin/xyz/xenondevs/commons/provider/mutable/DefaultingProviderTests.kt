package xyz.xenondevs.commons.provider.mutable

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class DefaultingProviderTests {
    
    @Test
    fun testDefaultsToValue() {
        val provider = mutableProvider<Int?>(null)
        val defaultingProvider = provider.defaultsTo(1)
        
        // receiving the value from defaultingProvider should propagate default value upwards,
        // but not earlier to keep lazy behavior
        assertEquals(null, provider.get())
        assertEquals(1, defaultingProvider.get())
        assertEquals(1, provider.get())
        
        // expected write propagation
        defaultingProvider.set(2)
        assertEquals(2, defaultingProvider.get())
        assertEquals(2, provider.get())
    }
    
    @Test
    fun testDefaultsToLazyValue() {
        val provider = mutableProvider<Int?>(null)
        val defaultingProvider = provider.defaultsToLazily { 1 }
        
        // receiving the value from defaultingProvider should propagate default value upwards,
        // but not earlier to keep lazy behavior
        assertEquals(null, provider.get())
        assertEquals(1, defaultingProvider.get())
        assertEquals(1, provider.get())
        
        // expected write propagation
        defaultingProvider.set(2)
        assertEquals(2, defaultingProvider.get())
        assertEquals(2, provider.get())
    }
    
    @Test
    fun testDefaultsToProvider() {
        val provider = mutableProvider<Int?>(null)
        val fallback = mutableProvider(1)
        val defaultingProvider = provider.defaultsTo(fallback)
        
        // receiving the value from defaultingProvider should propagate default value upwards,
        // but not earlier to keep lazy behavior
        assertEquals(1, fallback.get())
        assertEquals(null, provider.get())
        assertEquals(1, defaultingProvider.get())
        assertEquals(1, provider.get())
        
        // since the value has already been propagated upwards, future changes to the fallback
        // should not affect the provider
        fallback.set(2)
        assertEquals(2, fallback.get())
        assertEquals(1, defaultingProvider.get())
        assertEquals(1, provider.get())
        
        // expected write propagation
        defaultingProvider.set(3)
        assertEquals(3, defaultingProvider.get())
        assertEquals(3, provider.get())
    }
    
}