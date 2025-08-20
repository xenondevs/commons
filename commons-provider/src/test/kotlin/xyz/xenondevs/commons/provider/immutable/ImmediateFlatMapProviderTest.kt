package xyz.xenondevs.commons.provider.immutable

import org.junit.jupiter.api.Test
import xyz.xenondevs.commons.provider.DeferredValue
import xyz.xenondevs.commons.provider.immediateFlatten
import xyz.xenondevs.commons.provider.mutableProvider
import xyz.xenondevs.commons.provider.provider
import kotlin.test.assertEquals

class ImmediateFlatMapProviderTest {
    
    @Test
    fun testImmediateFlatMap() {
        val a = provider("1")
        val b = provider("2")
        val providerMap = mapOf("a" to a, "b" to b)
        val selector = mutableProvider("a")
        val flatMapped = selector.immediateFlatMap { providerMap[it]!! }
        
        assertEquals("1", flatMapped.get())
        selector.set("b")
        assertEquals("2", flatMapped.get())
        selector.set("a")
        assertEquals("1", flatMapped.get())
    }
    
    @Test
    fun testImmediateFlatMapUsesCorrectStateId() {
        val a = provider(1)
        val b = provider(2)
        val selector = mutableProvider(a)
        val flatMapped = selector.immediateFlatten()
            .map { it } // this additional map provider would reject outdated state ids
        
        selector.set(b)
        selector.set(a)
        assertEquals(1, flatMapped.get())
    }
    
    @Test
    fun testImmediateFlatMapStaticParentUpdateWasDelayed() {
        val staticParent = mutableProvider(1)
        val dynamicParents = listOf(mutableProvider(0), mutableProvider(1))
        val flatMapped = staticParent.immediateFlatMap { dynamicParents[it] }
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
    fun testImmediateFlatMapToMutableProvider() {
        val a = mutableProvider("a")
        val b = mutableProvider("b")
        val providerMap = mapOf("a" to a, "b" to b)
        val selector = mutableProvider("a")
        val flatMapped = selector.immediateFlatMapMutable { providerMap[it]!! }
        
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
    fun testImmediateFlatten() {
        val inner1 = provider(1)
        val inner2 = provider(2)
        val outer = mutableProvider(inner1)
        
        val flattened = outer.immediateFlatten()
        
        assertEquals(1, flattened.get())
        outer.set(inner2)
        assertEquals(2, flattened.get())
    }
    
    @Test
    fun testImmediateFlattenMutable() {
        val inner1 = mutableProvider(1)
        val inner2 = mutableProvider(2)
        val outer = mutableProvider(inner1)
        
        val flattened = outer.immediateFlatten()
        
        assertEquals(1, flattened.get())
        outer.set(inner2)
        assertEquals(2, flattened.get())
        flattened.set(3)
        assertEquals(1, inner1.get())
        assertEquals(3, inner2.get())
        assertEquals(3, flattened.get())
    }
    
    @Test
    fun testImmediateFlatMapParentsSet() {
        val a = mutableProvider(1)
        val b = mutableProvider(2)
        val selector = mutableProvider(a)
        val flatMapped = selector.immediateFlatMap { it }
        
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
    fun testImmediateFlatMapChildrenSet() {
        val a = mutableProvider(1)
        val b = mutableProvider(2)
        val selector = mutableProvider(a)
        val flatMapped = selector.immediateFlatMap { it }
        
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