package xyz.xenondevs.commons.reflection

import org.junit.jupiter.api.Test
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import kotlin.test.assertTrue

class TypeExtensionTests {
    
    @Test
    fun testEqualsIgnoreNullability() {
        // non-generic
        assertTrue((null as KType?).equalsIgnoreNullability(null))
        assertTrue(typeOf<String>().equalsIgnoreNullability(typeOf<String>()))
        assertTrue(typeOf<String>().equalsIgnoreNullability(typeOf<String?>()))
        assertTrue(typeOf<String?>().equalsIgnoreNullability(typeOf<String?>()))
        assertTrue(typeOf<String?>().equalsIgnoreNullability(typeOf<String>()))
        assertTrue(typeOf<String>().equalsIgnoreNullability(typeOf<String?>()))
        
        // generic
        assertTrue(typeOf<List<String>>().equalsIgnoreNullability(typeOf<List<String?>>()))
        assertTrue(typeOf<List<String>>().equalsIgnoreNullability(typeOf<List<String>?>()))
        assertTrue(typeOf<List<String>>().equalsIgnoreNullability(typeOf<List<String?>?>()))
        
        assertTrue(typeOf<List<String?>>().equalsIgnoreNullability(typeOf<List<String>>()))
        assertTrue(typeOf<List<String>?>().equalsIgnoreNullability(typeOf<List<String>>()))
        assertTrue(typeOf<List<String?>?>().equalsIgnoreNullability(typeOf<List<String>>()))
        
        assertTrue(typeOf<List<String>>().equalsIgnoreNullability(typeOf<List<String>>()))
        assertTrue(typeOf<List<String?>>().equalsIgnoreNullability(typeOf<List<String?>>()))
        assertTrue(typeOf<List<String>?>().equalsIgnoreNullability(typeOf<List<String>?>()))
        assertTrue(typeOf<List<String?>?>().equalsIgnoreNullability(typeOf<List<String?>?>()))
    }
    
}