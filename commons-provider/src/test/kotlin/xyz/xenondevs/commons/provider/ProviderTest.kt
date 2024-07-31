package xyz.xenondevs.commons.provider

import org.junit.jupiter.api.Test
import xyz.xenondevs.commons.provider.immutable.map
import xyz.xenondevs.commons.provider.immutable.provider
import kotlin.test.assertEquals

class ProviderTest {
    
    @Test
    fun testProviderPropagate() {
        var value = 1
        
        val top: Provider<Int> = provider { value }
        
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
    fun testProviderRemoveChild() {
        var value = 1
        val top: Provider<Int> = provider { value }
        val middle = top.map { it + 1 }
        val bottom = middle.map { it + 1 }
        
        assertEquals(3, bottom.get())
        
        value = 2
        top.update()
        
        assertEquals(4, bottom.get())
        
        top.removeChild(middle)
        value = 3
        top.update()
        
        assertEquals(4, bottom.get())
        
        middle.update()
        
        assertEquals(5, bottom.get())
    }
    
    @Test
    fun testProviderSubscriber() {
        var value = 0
        var invoked = false
        var invokedWeak = false
        
        val provider: Provider<Int> = provider { value }
        provider.subscribe { invoked = true }
        provider.subscribeWeak(this) { _, _ -> invokedWeak = true }
        
        // initializing should not call update handler
        provider.get()
        assert(!invoked)
        assert(!invokedWeak)
        
        // updating without changes to value should not call update handler
        provider.update()
        assert(!invoked)
        assert(!invokedWeak)
        
        // updating with change to value should call update handler
        value = 1
        provider.update()
        assert(invoked)
        assert(invokedWeak)
    }
    
    @Test
    fun testProviderRemoveSubscriber() {
        var value = 0
        var mirror1 = -1
        var mirror2 = -1
        var mirror3 = -1
        
        val provider: Provider<Int> = provider { value }
        val subscriber1: (Int) -> Unit = { mirror1 = it }
        val subscriber2: (Any, Int) -> Unit = { _, v -> mirror2 = v }
        val subscriber3: (Any, Int) -> Unit = { _, v -> mirror3 = v }
        provider.subscribe(subscriber1)
        provider.subscribeWeak(this, subscriber2)
        provider.subscribeWeak(this, subscriber3)
        
        provider.update()
        assertEquals(0, mirror1)
        assertEquals(0, mirror2)
        assertEquals(0, mirror3)
        
        value = 1
        provider.unsubscribe(subscriber1)
        provider.unsubscribeWeak(this, subscriber2)
        provider.update()
        assertEquals(0, mirror1)
        assertEquals(0, mirror2)
        assertEquals(1, mirror3)
        
        value = 2
        provider.unsubscribeWeak(this)
        provider.update()
        assertEquals(0, mirror1)
        assertEquals(0, mirror2)
        assertEquals(1, mirror3)
    }
    
}