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
    
}