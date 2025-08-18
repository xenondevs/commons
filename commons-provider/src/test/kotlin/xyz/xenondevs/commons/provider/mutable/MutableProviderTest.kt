package xyz.xenondevs.commons.provider.mutable

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import xyz.xenondevs.commons.provider.DeferredValue
import xyz.xenondevs.commons.provider.mutableProvider
import java.util.concurrent.atomic.AtomicInteger

class MutableProviderTest {
    
    @Test
    fun testMutableProviderPropagateUp() {
        val top = mutableProvider(1)
        val middle = top.map({ it * 10 }, { it / 10 })
        val bottom = middle.map({ it * 10 }, { it / 10 })
        
        assertEquals(1, top.get())
        assertEquals(10, middle.get())
        assertEquals(100, bottom.get())
        
        bottom.set(200)
        
        assertEquals(2, top.get())
        assertEquals(20, middle.get())
        assertEquals(200, bottom.get())
    }
    
    @Test
    fun testMutableProviderPropagateDown() {
        val top = mutableProvider(1)
        val middle = top.map({ it * 10 }, { it / 10 })
        val bottom = middle.map({ it * 10 }, { it / 10 })
        
        assertEquals(1, top.get())
        assertEquals(10, middle.get())
        assertEquals(100, bottom.get())
        
        top.set(2)
        
        assertEquals(2, top.get())
        assertEquals(20, middle.get())
        assertEquals(200, bottom.get())
    }
    
    @Test
    fun testPropagateUpDown() {
        val top = mutableProvider(1)
        
        val branchAMiddle = top.map({ it * 10 }, { it / 10 })
        val branchABottom = branchAMiddle.map({ it * 10 }, { it / 10 })
        
        val branchBMiddle = top.map({ it + 1 }, { it - 1 })
        val branchBBottom = branchBMiddle.map({ it + 10 }, { it - 10 })
        
        assertEquals(1, top.get())
        assertEquals(10, branchAMiddle.get())
        assertEquals(100, branchABottom.get())
        assertEquals(2, branchBMiddle.get())
        assertEquals(12, branchBBottom.get())
        
        branchABottom.set(200)
        
        assertEquals(200, branchABottom.get())
        assertEquals(20, branchAMiddle.get())
        assertEquals(2, top.get())
        assertEquals(3, branchBMiddle.get())
        assertEquals(13, branchBBottom.get())
    }
    
    @Test
    fun testSetOrigin() {
        val origin = AtomicInteger(1)
        val provider = mutableProvider(origin::get, origin::set)
        
        provider.set(2)
        assertEquals(2, origin.get())
    }
    
    @Test
    fun testUpdateIgnoresOldState() {
        val provider = mutableProvider(0)
        
        val one = DeferredValue.Direct(1)
        val two = DeferredValue.Direct(2)
        
        assertEquals(0, provider.get())
        provider.update(one)
        assertEquals(1, provider.get())
        provider.update(two)
        assertEquals(2, provider.get())
        provider.update(one)
        assertEquals(2, provider.get())
    }
    
    @Test
    fun testNotifiedOnUpdatingWithEqualState() {
        val provider = mutableProvider(0)
        var updateCount = 0
        provider.observe { updateCount++ }
        
        val one = DeferredValue.Direct(1)
        val two = DeferredValue.Direct(2)
        
        assertEquals(0, updateCount)
        provider.update(one)
        assertEquals(1, updateCount)
        provider.update(one)
        assertEquals(2, updateCount)
        provider.update(two)
        assertEquals(3, updateCount)
        provider.update(one)
        assertEquals(3, updateCount)
    }
    
}