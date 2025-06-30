package xyz.xenondevs.commons.reflection

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

private class A {
    
    companion object {
        val X = object : Any() {}
    }
    
    class B {
        inner class C
    }
}

class KClassExtensionTests {
    
    @Test
    fun testSimpleNestedName() {
        assertEquals("A", A::class.simpleNestedName)
        assertEquals("A.B", A.B::class.simpleNestedName)
        assertEquals("A.B.C", A.B.C::class.simpleNestedName)
        assertEquals(null, A.X::class.simpleNestedName)
    }
    
}