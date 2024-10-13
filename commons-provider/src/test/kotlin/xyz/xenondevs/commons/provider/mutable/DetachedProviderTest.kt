package xyz.xenondevs.commons.provider.mutable

import org.junit.jupiter.api.Test
import xyz.xenondevs.commons.provider.detached
import xyz.xenondevs.commons.provider.mutableProvider
import kotlin.test.assertEquals

class DetachedProviderTest {
    
    @Test
    fun testDetachedProvider() {
        val parent = mutableProvider(1)
        val detached = parent.detached()
        
        assertEquals(1, parent.get())
        assertEquals(1, detached.get())
        
        parent.set(2)
        
        assertEquals(2, parent.get())
        assertEquals(2, detached.get())
        
        detached.set(3)
        
        assertEquals(2, parent.get())
        assertEquals(3, detached.get())
        
        parent.set(4)
        
        assertEquals(4, parent.get())
        assertEquals(4, detached.get())
    }
    
}