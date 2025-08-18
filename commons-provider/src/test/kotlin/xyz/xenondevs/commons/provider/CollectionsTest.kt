package xyz.xenondevs.commons.provider

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertInstanceOf
import xyz.xenondevs.commons.provider.util.with
import xyz.xenondevs.commons.provider.util.without
import kotlin.test.assertEquals
import kotlin.test.assertNotSame
import kotlin.test.assertSame

class CollectionsTest {
    
    @Test
    fun `test Collection#with`() {
        val original = setOf("a", "b")
        val new = original.with("c", ::HashSet)
        
        assertEquals(setOf("a", "b", "c"), new)
        assertNotSame(original, new)
        assertInstanceOf<HashSet<*>>(new)
    }
    
    @Test
    fun `test Collection#with, element is already contained in collection`() {
        val original = setOf("a", "b")
        val new = original.with("b", ::HashSet)
        
        assertSame(original, new)
    }
    
    @Test
    fun `test Collection#without`() {
        val original = setOf("a", "b")
        val new = original.without("b", ::HashSet)
        
        assertEquals(setOf("a"), new)
        assertNotSame(original, new)
        assertInstanceOf<HashSet<*>>(new)
    }
    
    @Test
    fun `test Collection#without, element is not contained in collection`() {
        val original = setOf("a", "b")
        val new = original.without("c", ::HashSet)
        
        assertEquals(setOf("a", "b"), new)
        assertSame(original, new)
    }
    
    @Test
    fun `test MapK,SetV#with`() {
        val original = mapOf(
            "a" to setOf("aa", "ab"),
            "b" to setOf("ba", "bb")
        )
        val new1 = original.with("a", "ac", ::HashMap, ::HashSet)
        
        assertEquals(setOf("aa", "ab", "ac"), new1["a"])
        assertEquals(setOf("ba", "bb"), new1["b"])
        assertNotSame(new1, original)
        assertInstanceOf<HashMap<*, *>>(new1)
        assertInstanceOf<HashSet<*>>(new1["a"])
        
        val new2 = original.with("c", "ca", ::HashMap, ::HashSet)
        assertNotSame(original, new2)
        assertEquals(setOf("aa", "ab"), new2["a"])
        assertEquals(setOf("ba", "bb"), new2["b"])
        assertEquals(setOf("ca"), new2["c"])
        assertInstanceOf<HashMap<*, *>>(new2)
        assertInstanceOf<HashSet<*>>(new2["c"])
    }
    
    @Test
    fun `test MapK,SetV#with, value is already contained in set`() {
        val original = mapOf(
            "a" to setOf("aa", "ab"),
            "b" to setOf("ba", "bb")
        )
        
        val new = original.with("a", "aa", ::HashMap, ::HashSet)
        assertSame(original, new)
    }
    
    @Test
    fun `test MapK,SetV#without`() {
        val original = mapOf(
            "a" to setOf("aa", "ab"),
            "b" to setOf("ba", "bb")
        )
        
        val new1 = original.without("a", "ab", ::HashMap, ::HashSet)
        assertEquals(setOf("aa"), new1["a"])
        assertEquals(setOf("ba", "bb"), new1["b"])
        assertNotSame(new1, original)
        assertInstanceOf<HashMap<*, *>>(new1)
        assertInstanceOf<HashSet<*>>(new1["a"])
        
        val new2 = new1.without("a", "aa", ::HashMap, ::HashSet)
        assertEquals(null, new2["a"])
        assertEquals(setOf("ba", "bb"), new2["b"])
        assertNotSame(new2, original)
        assertInstanceOf<HashMap<*, *>>(new2)
    }
    
    @Test
    fun `test MapK,SetV#without, key is not in map`() {
        val original = mapOf(
            "a" to setOf("aa", "ab"),
            "b" to setOf("ba", "bb")
        )
        
        val new = original.without("c", "ca", ::HashMap, ::HashSet)
        assertSame(original, new)
    }
    
    @Test
    fun `test MapK,SetV#without, value is not in set`() {
        val original = mapOf(
            "a" to setOf("aa", "ab"),
            "b" to setOf("ba", "bb")
        )
        
        val new = original.without("a", "ac", ::HashMap, ::HashSet)
        assertSame(original, new)
    }
    
}