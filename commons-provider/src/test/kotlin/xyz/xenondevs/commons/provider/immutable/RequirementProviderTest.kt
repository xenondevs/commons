package xyz.xenondevs.commons.provider.immutable

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import xyz.xenondevs.commons.provider.mutableProvider
import xyz.xenondevs.commons.provider.require
import xyz.xenondevs.commons.provider.requireNotNull
import kotlin.test.assertEquals

class RequirementProviderTest {
    
    @Test
    fun testRequire() {
        val provider = mutableProvider(-1)
        val required = provider.require({ it >= 0 }, { "Positive value" })
        
        assertThrows<IllegalArgumentException> { required.get() }
        provider.set(1)
        assertEquals(1, required.get())
    }
    
    @Test
    fun testRequireNonNull() {
        val provider = mutableProvider<Int?>(null)
        val required = provider.requireNotNull()
        
        assertThrows<IllegalArgumentException> { required.get() }
        provider.set(1)
        assertEquals(1, required.get())
    }
    
}