package xyz.xenondevs.commons.provider

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class DeferredValueTest {
    
    @Test
    fun `test whether DeferredValue of immediate flat-mapping provider is stable`() {
        val root = mutableProvider(0)
        val selection = listOf(
            mutableProvider("0"),
            mutableProvider("1"),
            mutableProvider("2")
        )
        
        val flatMapped = root.immediateFlatMap { selection[it] }
        
        val deferredValueAt0 = flatMapped.value
        
        root.set(1)
        
        val deferredValueAt1 = flatMapped.value
        
        assertEquals("1", flatMapped.get())
        assertEquals("0", deferredValueAt0.value)
        assertEquals("1", deferredValueAt1.value)
        assertEquals("1", flatMapped.value.value)
        
        root.set(2)
        
        val deferredValueAt2 = flatMapped.value
        
        assertEquals("2", flatMapped.get())
        assertEquals("0", deferredValueAt0.value)
        assertEquals("1", deferredValueAt1.value)
        assertEquals("2", deferredValueAt2.value)
        assertEquals("2", flatMapped.value.value)
        
        selection[2].set("X")
        
        val deferredValueAt2X = flatMapped.value
        
        assertEquals("X", flatMapped.get())
        assertEquals("0", deferredValueAt0.value)
        assertEquals("1", deferredValueAt1.value)
        assertEquals("2", deferredValueAt2.value)
        assertEquals("X", deferredValueAt2X.value)
        assertEquals("X", flatMapped.value.value)
    }
    
    @Test
    fun `test whether DeferredValue of flat-mapping provider is stable`() {
        val root = mutableProvider(0)
        val selection = listOf(
            mutableProvider("0"),
            mutableProvider("1"),
            mutableProvider("2")
        )
        
        val flatMapped = root.flatMap { selection[it] }
        
        val deferredValueAt0 = flatMapped.value
        
        root.set(1)
        
        val deferredValueAt1 = flatMapped.value
        
        assertEquals("1", flatMapped.get())
        assertEquals("1", deferredValueAt0.value) // due to lazy-ness
        assertEquals("1", deferredValueAt1.value)
        assertEquals("1", flatMapped.value.value)
        
        root.set(2)
        
        val deferredValueAt2 = flatMapped.value
        
        assertEquals("2", flatMapped.get())
        assertEquals("1", deferredValueAt0.value)
        assertEquals("1", deferredValueAt1.value)
        assertEquals("2", deferredValueAt2.value)
        assertEquals("2", flatMapped.value.value)
        
        selection[2].set("X")
        
        val deferredValueAt2X = flatMapped.value
        
        assertEquals("X", flatMapped.get())
        assertEquals("1", deferredValueAt0.value)
        assertEquals("1", deferredValueAt1.value)
        assertEquals("2", deferredValueAt2.value)
        assertEquals("X", deferredValueAt2X.value)
        assertEquals("X", flatMapped.value.value)
    }
    
}