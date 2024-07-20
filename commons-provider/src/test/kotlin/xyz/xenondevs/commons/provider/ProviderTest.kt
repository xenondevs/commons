package xyz.xenondevs.commons.provider

import org.junit.jupiter.api.Test
import xyz.xenondevs.commons.provider.immutable.map
import kotlin.test.assertEquals

class ProviderTest {
    
    @Test
    fun testProviderPropagate() {
        var value = 1
        
        val top: Provider<Int> = object : AbstractProvider<Int>() {
            override fun loadValue(): Int = value
        }
        
        val branchA = top.map { it * 10 }
        val branchB = top.map { it * 100 }
        
        val leafA = branchA.map { it + 1 }
        val leafB = branchB.map { it + 1 }
        
        assertEquals(top.get(), 1)
        assertEquals(branchA.get(), 10)
        assertEquals(branchB.get(), 100)
        assertEquals(leafA.get(), 11)
        assertEquals(leafB.get(), 101)
        
        value = 2
        top.update()
        
        assertEquals(top.get(), 2)
        assertEquals(branchA.get(), 20)
        assertEquals(branchB.get(), 200)
        assertEquals(leafA.get(), 21)
        assertEquals(leafB.get(), 201)
    }
    
    @Test
    fun testProviderSubscriber() {
        var value = 0
        var invoked = false
        
        val provider: Provider<Int> = object : AbstractProvider<Int>() {
            override fun loadValue(): Int = value
        }
        provider.subscribe { invoked = true }
        
        // initializing should not call update handler
        provider.get()
        assert(!invoked)
        
        // updating without changes to value should not call update handler
        provider.update()
        assert(!invoked)
        
        // updating with change to value should call update handler
        value = 1
        provider.update()
        assert(invoked)
    }
    
}