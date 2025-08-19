package xyz.xenondevs.commons.provider.immutable

import org.junit.jupiter.api.Test
import xyz.xenondevs.commons.provider.DeferredValue
import xyz.xenondevs.commons.provider.flatten
import xyz.xenondevs.commons.provider.lazyFlatten
import xyz.xenondevs.commons.provider.mutableProvider
import xyz.xenondevs.commons.provider.provider
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FlatMapProviderTest {
    
    @Test
    fun testFlatMap() {
        val a = provider("1")
        val b = provider("2")
        val providerMap = mapOf("a" to a, "b" to b)
        val selector = mutableProvider("a")
        val flatMapped = selector.flatMap { providerMap[it]!! }
        
        assertEquals("1", flatMapped.get())
        selector.set("b")
        assertEquals("2", flatMapped.get())
        selector.set("a")
        assertEquals("1", flatMapped.get())
    }
    
    @Test
    fun testFlatMapUsesCorrectStateId() {
        val a = provider(1)
        val b = provider(2)
        val selector = mutableProvider(a)
        val flatMapped = selector.flatten()
            .map { it } // this additional map provider would reject outdated state ids
        
        selector.set(b)
        selector.set(a)
        assertEquals(1, flatMapped.get())
    }
    
    @Test
    fun testFlatMapStaticParentUpdateWasDelayed() {
        val staticParent = mutableProvider(1)
        val dynamicParents = listOf(mutableProvider(0), mutableProvider(1))
        val flatMapped = staticParent.flatMap { dynamicParents[it] }
        val flatMappedIdentity = flatMapped.map { it }
        
        // simulate a race: staticParent is set to 0 here conceptually
        val zeroValue = DeferredValue.Direct(0)
        // afterward, but before staticParent receives the update, the current dynamic parent changes
        dynamicParents[1].set(999)
        
        // because staticParent has not received its update yet, flatMapped and flatMappedIdentity temporarily contain 999
        assertEquals(999, flatMapped.get())
        assertEquals(999, flatMappedIdentity.get())
        
        // then, staticParent receives the update that happened previously
        staticParent.update(zeroValue)
        
        // even though 999 (with >seqNo) was propagated, the flat-mapped and its identity still switch
        assertEquals(0, flatMapped.get())
        assertEquals(0, flatMappedIdentity.get())
        
        // the next updates are also propagated appropriately
        dynamicParents[0].set(2)
        assertEquals(2, flatMapped.get())
        assertEquals(2, flatMappedIdentity.get())
    }
    
    @Test
    fun testFlatMapToMutableProvider() {
        val a = mutableProvider("a")
        val b = mutableProvider("b")
        val providerMap = mapOf("a" to a, "b" to b)
        val selector = mutableProvider("a")
        val flatMapped = selector.flatMapMutable { providerMap[it]!! }
        
        flatMapped.set("1")
        assertEquals("1", a.get())
        assertEquals("b", b.get())
        
        selector.set("b")
        flatMapped.set("2")
        assertEquals("1", a.get())
        assertEquals("2", b.get())
        
        b.set("3")
        assertEquals("1", a.get())
        assertEquals("3", b.get())
        assertEquals("3", flatMapped.get())
    }
    
    @Test
    fun testFlatten() {
        val inner1 = provider(1)
        val inner2 = provider(2)
        val outer = mutableProvider(inner1)
        
        val flattened = outer.flatten()
        
        assertEquals(1, flattened.get())
        outer.set(inner2)
        assertEquals(2, flattened.get())
    }
    
    @Test
    fun testFlattenMutable() {
        val inner1 = mutableProvider(1)
        val inner2 = mutableProvider(2)
        val outer = mutableProvider(inner1)
        
        val flattened = outer.flatten()
        
        assertEquals(1, flattened.get())
        outer.set(inner2)
        assertEquals(2, flattened.get())
        flattened.set(3)
        assertEquals(1, inner1.get())
        assertEquals(3, inner2.get())
        assertEquals(3, flattened.get())
    }
    
    @Test
    fun testLazyFlatMap() {
        val a = provider(1)
        val b = provider(2)
        val selector = mutableProvider(a)
        
        var flatMapTransformExecCount = 0
        var flatMapObserverFiredCount = 0
        
        val flatMapped = selector.lazyFlatMap { 
            flatMapTransformExecCount++
            it
        }
        flatMapped.observe { flatMapObserverFiredCount++ }
        
        
        // flat map transform will not be executed before its value is resolved
        selector.set(b)
        selector.set(a)
        assertEquals(0, flatMapTransformExecCount)
        assertEquals(0, flatMapObserverFiredCount)
        
        // resolve flat-mapped provider
        assertEquals(1, flatMapped.get())
        assertEquals(1, flatMapTransformExecCount)
        
        // already resolved flat-mapped provider should not run transform again
        assertEquals(1, flatMapped.get())
        assertEquals(1, flatMapTransformExecCount)
        
        // now, observer's will be fired once if the value changes
        assertEquals(0, flatMapObserverFiredCount)
        selector.set(b)
        selector.set(a)
        selector.set(b)
        assertEquals(1, flatMapObserverFiredCount)
     
        // switching the selected provider works properly
        assertEquals(2, flatMapped.get())
        assertEquals(2, flatMapTransformExecCount)
    }
    
    @Test
    fun testLazyFlatMapStaticParentUpdateWasDelayed() {
        val staticParent = mutableProvider(1)
        val dynamicParents = listOf(mutableProvider(0), mutableProvider(1))
        val flatMapped = staticParent.lazyFlatMap { dynamicParents[it] }
        val flatMappedIdentity = flatMapped.map { it }
        
        // simulate a race: staticParent is set to 0 here conceptually
        val zeroValue = DeferredValue.Direct(0)
        // afterward, but before staticParent receives the update, the current dynamic parent changes
        dynamicParents[1].set(999)
        
        // because staticParent has not received its update yet, flatMapped and flatMappedIdentity temporarily contain 999
        assertEquals(999, flatMapped.get())
        assertEquals(999, flatMappedIdentity.get())
        
        // then, staticParent receives the update that happened previously
        staticParent.update(zeroValue)
        
        // even though 999 (with >seqNo) was propagated, the flat-mapped and its identity still switch
        assertEquals(0, flatMapped.get())
        assertEquals(0, flatMappedIdentity.get())
        
        // the next updates are also propagated appropriately
        dynamicParents[0].set(2)
        assertEquals(2, flatMapped.get())
        assertEquals(2, flatMappedIdentity.get())
    }
    
    @Test
    fun testLazyFlatMapResultPropagated() {
        val staticParent = mutableProvider(0)
        val dynamicParents = listOf(
            provider(0),
            provider(1),
            provider(2)
        )
        var flatMapTransformExecCount = 0
        val lazilyFlatMapped = staticParent.lazyFlatMap { 
            flatMapTransformExecCount++
            dynamicParents[it]
        }
        val mapped = lazilyFlatMapped.map { it * 10 }
        
        assertEquals(0, flatMapTransformExecCount)
        
        staticParent.set(1)
        
        assertEquals(0, flatMapTransformExecCount)
        
        staticParent.set(2)
        
        assertEquals(0, flatMapTransformExecCount)
        
        assertEquals(20, mapped.get())
        assertEquals(1, flatMapTransformExecCount)
    }
    
    @Test
    fun testLazyFlatMapMutable() {
        val a = mutableProvider(1)
        val b = mutableProvider(2)
        val selector = mutableProvider(a)
        
        var flatMapTransformExecCount = 0
        var flatMapObserverFiredCount = 0
        
        val flatMapped = selector.lazyFlatMapMutable {
            flatMapTransformExecCount++
            it
        }
        flatMapped.observe { flatMapObserverFiredCount++ }
        
        // flat map transform will not be executed before its value is resolved
        selector.set(b)
        selector.set(a)
        assertEquals(0, flatMapTransformExecCount)
        assertEquals(0, flatMapObserverFiredCount)
        
        // resolve flat-mapped provider
        assertEquals(1, flatMapped.get())
        assertEquals(1, flatMapTransformExecCount)
        
        // already resolved flat-mapped provider should not run transform again
        assertEquals(1, flatMapped.get())
        assertEquals(1, flatMapTransformExecCount)
        
        // flat-mapped provider allows setting value
        flatMapped.set(-1)
        flatMapped.set(1)
        flatMapped.set(-1)
        assertEquals(-1, flatMapped.get())
        assertEquals(-1, a.get())
        assertEquals(3, flatMapObserverFiredCount)
        
        // now, observer's will be fired once if the value changes
        assertEquals(3, flatMapObserverFiredCount)
        selector.set(b)
        selector.set(a)
        selector.set(b)
        assertEquals(4, flatMapObserverFiredCount)
        
        // switching the selected provider works properly
        assertEquals(2, flatMapped.get())
        assertEquals(2, flatMapTransformExecCount)
    }
    
    @Test
    fun testLazyFlatten() {
        var resolved = false
        
        val inner1 = provider(1)
        val inner2 = provider(2)
        val outer = mutableProvider {
            resolved = true
            inner1
        }
        
        val flattened = outer.lazyFlatten()
        
        assertFalse(resolved)
        assertEquals(1, flattened.get())
        assertTrue(resolved)
        outer.set(inner2)
        assertEquals(2, flattened.get())
    }
    
    @Test
    fun testLazyFlattenMutable() {
        var resolved = false
        
        val inner1 = mutableProvider(1)
        val inner2 = mutableProvider(2)
        val outer = mutableProvider {
            resolved = true
            inner1
        }
        
        val flattened = outer.lazyFlatten()
        
        assertFalse(resolved)
        assertEquals(1, flattened.get())
        assertTrue(resolved)
        outer.set(inner2)
        assertEquals(2, flattened.get())
        flattened.set(3)
        assertEquals(1, inner1.get())
        assertEquals(3, inner2.get())
        assertEquals(3, flattened.get())
    }
    
    @Test
    fun testFlatMapParentsSet() {
        val a = mutableProvider(1)
        val b = mutableProvider(2)
        val selector = mutableProvider(a)
        val flatMapped = selector.flatMap { it }
        
        assertEquals(emptySet(), a.parents)
        assertEquals(emptySet(), b.parents)
        assertEquals(emptySet(), selector.parents)
        assertEquals(setOf(selector, a), flatMapped.parents)
        
        selector.set(b)
        
        assertEquals(emptySet(), a.parents)
        assertEquals(emptySet(), b.parents)
        assertEquals(emptySet(), selector.parents)
        assertEquals(setOf(selector, b), flatMapped.parents)
    }
    
    @Test
    fun testFlatMapChildrenSet() {
        val a = mutableProvider(1)
        val b = mutableProvider(2)
        val selector = mutableProvider(a)
        val flatMapped = selector.flatMap { it }
        
        assertEquals(setOf(flatMapped), a.children)
        assertEquals(emptySet(), b.children)
        assertEquals(setOf(flatMapped), selector.children)
        assertEquals(emptySet(), flatMapped.children)
        
        selector.set(b)
        
        assertEquals(emptySet(), a.children)
        assertEquals(setOf(flatMapped), b.children)
        assertEquals(setOf(flatMapped), selector.children)
        assertEquals(emptySet(), flatMapped.children)
    }
    
}