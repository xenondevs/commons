package xyz.xenondevs.commons.provider

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CoreProviderTest {
    
    @Test
    fun testProviderPropagate() {
        val top = mutableProvider(1)
        
        val branchA = top.map { it * 10 }
        val branchB = top.map { it * 100 }
        
        val leafA = branchA.map { it + 1 }
        val leafB = branchB.map { it + 1 }
        
        assertEquals(top.get(), 1)
        assertEquals(branchA.get(), 10)
        assertEquals(branchB.get(), 100)
        assertEquals(leafA.get(), 11)
        assertEquals(leafB.get(), 101)
        
        top.set(2)
        
        assertEquals(top.get(), 2)
        assertEquals(branchA.get(), 20)
        assertEquals(branchB.get(), 200)
        assertEquals(leafA.get(), 21)
        assertEquals(leafB.get(), 201)
    }
    
    @Test
    fun testProviderIsLazy() {
        val root = mutableProvider(1)
        
        var branchATransform = 0
        var branchAUntransform = 0
        
        var leafATransform = 0
        var leafAUntransform = 0
        
        var branchBTransform = 0
        var branchBUntransform = 0
        
        var leafBTransform = 0
        var leafBUntransform = 0
        
        val branchA = root.map({
            branchATransform++
            it * 10
        }, {
            branchAUntransform++
            it / 10
        })
        val leafA = branchA.map({
            leafATransform++
            it + 1
        }, {
            leafAUntransform++
            it - 1
        })
        
        val branchB = root.map({
            branchBTransform++
            it * 100
        }, {
            branchBUntransform++
            it / 100
        })
        val leafB = branchB.map({
            leafBTransform++
            it + 1
        }, {
            leafBUntransform++
            it - 1
        })
        
        root.set(2)
        
        assertEquals(0, branchATransform)
        assertEquals(0, branchAUntransform)
        assertEquals(0, leafATransform)
        assertEquals(0, leafAUntransform)
        assertEquals(0, branchBTransform)
        assertEquals(0, branchBUntransform)
        assertEquals(0, leafBTransform)
        assertEquals(0, leafBUntransform)
        
        assertEquals(201, leafB.get()) // resolve leafB
        branchA.set(10)
        assertEquals(0, branchATransform)
        assertEquals(0, branchAUntransform)
        assertEquals(0, leafATransform)
        assertEquals(0, leafAUntransform)
        assertEquals(1, branchBTransform)
        assertEquals(0, branchBUntransform)
        assertEquals(1, leafBTransform)
        assertEquals(0, leafBUntransform)
        
        assertEquals(1, root.get()) // resolve root
        assertEquals(11, leafA.get()) // resolve leafA
        assertEquals(0, branchATransform)
        assertEquals(1, branchAUntransform)
        assertEquals(1, leafATransform)
        assertEquals(0, leafAUntransform)
        assertEquals(1, branchBTransform)
        assertEquals(0, branchBUntransform)
        assertEquals(1, leafBTransform)
        assertEquals(0, leafBUntransform)
    }
    
    @Test
    fun testProviderSubscriber() {
        var invoked = false
        var invokedWeak = false
        
        val provider = mutableProvider(0)
        provider.subscribe { invoked = true }
        provider.subscribeWeak(this) { _, _ -> invokedWeak = true }
        
        // initializing should not call subscriber
        provider.get()
        assert(!invoked)
        assert(!invokedWeak)
        
        // updating without changes to value should call subscriber
        provider.set(0)
        assert(invoked)
        assert(invokedWeak)
        
        // updating with change to value should call subscriber
        invoked = false
        invokedWeak = false
        provider.set(1)
        assert(invoked)
        assert(invokedWeak)
    }
    
    @Test
    fun testProviderRemoveSubscriber() {
        var mirror1 = -1
        var mirror2 = -1
        var mirror3 = -1
        
        val provider = mutableProvider(0)
        val subscriber1: (Int) -> Unit = { mirror1 = it }
        val subscriber2: (Any, Int) -> Unit = { _, v -> mirror2 = v }
        val subscriber3: (Any, Int) -> Unit = { _, v -> mirror3 = v }
        provider.subscribe(subscriber1)
        provider.subscribeWeak(this, subscriber2)
        provider.subscribeWeak(this, subscriber3)
        
        provider.set(0)
        assertEquals(0, mirror1)
        assertEquals(0, mirror2)
        assertEquals(0, mirror3)
        
        provider.unsubscribe(subscriber1)
        provider.unsubscribeWeak(this, subscriber2)
        provider.set(1)
        assertEquals(0, mirror1)
        assertEquals(0, mirror2)
        assertEquals(1, mirror3)
        
        provider.unsubscribeWeak(this)
        provider.set(2)
        assertEquals(0, mirror1)
        assertEquals(0, mirror2)
        assertEquals(1, mirror3)
    }
    
    @Test
    fun testProviderSubscriberCalledOutsideLock() {
        val provider = mutableProvider(0)
        val mapped = provider.map({ it + 1 }, { it - 1 })

        provider.subscribe { assertFalse(Thread.holdsLock(provider)) }
        provider.subscribeWeak(this) { _, _ -> assertFalse(Thread.holdsLock(provider)) }
        mapped.subscribe { assertFalse(Thread.holdsLock(provider)) }
        mapped.subscribeWeak(this) { _, _ -> assertFalse(Thread.holdsLock(provider)) }

        provider.set(1)
    }
    
    @Test
    fun testProviderObserver() {
        var invoked = false
        var invokedWeak = false
        var evalCount = 0
        
        val root = mutableProvider(0)
        val mapped = root.map { evalCount++; it + 1 }
        mapped.observe { invoked = true }
        mapped.observeWeak(this) { invokedWeak = true }
        
        // initializing should not call observer
        mapped.get()
        assert(!invoked)
        assert(!invokedWeak)
        
        // updating without changes to value should call observer
        root.set(0)
        assert(invoked)
        assert(invokedWeak)
        
        // updating with change to value should call observer
        invoked = false
        invokedWeak = false
        root.set(1)
        assert(invoked)
        assert(invokedWeak)
        
        // observer should not cause the provider value to be resolved
        assertEquals(1, evalCount)
    }
    
    @Test
    fun testThrowingProvider() {
        val root = mutableProvider("")
        val mapped = root.map<String> { throw UnsupportedOperationException() }
        
        assertDoesNotThrow { root.get() }
        assertThrows<UnsupportedOperationException> { mapped.get() }
        
        assertDoesNotThrow { root.set("1") }
        
        var rootSubscriberInvoked = false
        var mappedSubscriberInvoked = false
        
        root.subscribe { rootSubscriberInvoked = true }
        mapped.subscribe { mappedSubscriberInvoked = true }
        
        assertDoesNotThrow { root.set("2") }
        assertTrue(rootSubscriberInvoked)
        assertFalse(mappedSubscriberInvoked)
    }
    
}