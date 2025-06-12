package xyz.xenondevs.commons.reflection

import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicReference
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import kotlin.test.assertFalse
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
    
    @Test
    fun testEqualsIgnorePlatformTypes() {
        assertTrue(typeOf<String>().equalsIgnorePlatformTypes(typeOf<String>()))
        assertFalse(typeOf<String>().equalsIgnorePlatformTypes(typeOf<String?>()))
        
        assertTrue(typeOf<AtomicReference<String>>().equalsIgnorePlatformTypes(typeOf<AtomicReference<String>>()))
        assertFalse(typeOf<AtomicReference<String>>().equalsIgnorePlatformTypes(typeOf<AtomicReference<String?>>()))
        
        val referenceWithPlatformType = AtomicReference("")
        assertTrue(typeOfValue(referenceWithPlatformType).equalsIgnorePlatformTypes(typeOf<AtomicReference<String>>()))
        assertFalse(typeOfValue(referenceWithPlatformType).equalsIgnorePlatformTypes(typeOf<AtomicReference<String?>>()))
    }
    
    private inline fun <reified T> typeOfValue(value: T) = typeOf<T>()

}