import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import xyz.xenondevs.commons.version.Version
import kotlin.test.assertEquals

class VersionTest {
    
    @ParameterizedTest
    @ValueSource(strings = [
        "",
        ".",
        "ABC",
        "1..0",
        "1.2.3-GAMMA", // invalid pre-release stage
        "1.0-alpha-HELLO",
        "1.0-alpha.1.", // trailing dot
        "1.0.0-alpha+Hello+World", // invalid character + in metadata
    ])
    fun testInvalidVersion(version: String) {
        assertThrows<IllegalArgumentException> { Version(version) }
    }
    
    @ParameterizedTest
    @ValueSource(strings = [
        "1",
        "0",
        "1.0.0",
        "0.0.1",
        "0.0.0.0.0.1",
        "1.0-snapshot",
        "1.0-alpha",
        "1.0-beta",
        "1.0-rc",
        "1.0-alpha.1",
        "1.0-aLpHa.1",
        "1.0-alpha.1.0.0",
        "1.0-alpha.1+build.732.amd64",
    ])
    fun testValidVersion(version: String) {
        assertDoesNotThrow { Version(version) }
    }
    
    @ParameterizedTest
    @MethodSource("testEqualsCases")
    fun testEquals(v1: String, v2: String) {
        assert(Version(v1) == Version(v2))
    }
    
    @ParameterizedTest
    @MethodSource("testEqualsCases")
    fun testHashCodeEquals(v1: String, v2: String) {
        assert(Version(v1).hashCode() == Version(v2).hashCode())
    }
    
    @ParameterizedTest
    @MethodSource("testFirstSmallerSecondCases")
    fun testFirstSmallerSecond(smaller: String, larger: String) {
        assert(Version(smaller) < Version(larger))
    }
    
    @ParameterizedTest
    @ValueSource(strings = [
        "1",
        "1.0",
        "1.0.0",
        "1.0.0.0",
        "0.0.1",
        "1.0-alpha",
        "1.0-alpha.0",
        "1.0-alpha.1",
        "1.0-alpha.1.0.0",
        "1.0-alpha.1.0.0+abc.def.ghi",
        "1.0-AlPhA.1.0.0+AbC.DEF.ghi"
    ])
    fun testRoundTrip(version: String) {
        assertEquals(version, Version(version).toString())
    }
    
    @ParameterizedTest
    @MethodSource("testToStringOmitZerosCases")
    fun testToStringOmitZeros(version: String, expected: String) {
        assertEquals(expected, Version(version).toString(omitZeros = true))
    }
    
    @ParameterizedTest
    @MethodSource("testEqualsIgnoringCases")
    fun testEqualsIgnoring(v1: String, v2: String, ignoreIdx: Int) {
        assert(Version(v1).equals(Version(v2), ignoreIdx))
    }
    
    @ParameterizedTest
    @MethodSource("testNotEqualsIgnoringCases")
    fun testNotEqualsIgnoring(v1: String, v2: String, ignoreIdx: Int) {
        assert(!Version(v1).equals(Version(v2), ignoreIdx))
    }
    
    companion object {
        
        @JvmStatic
        fun testEqualsCases(): List<Arguments> = listOf(
            Arguments.of("1", "1.0.0.0.0"),
            Arguments.of("1.0.0", "1"),
            Arguments.of("1.0.0-alpha", "1.0.0-alpha.0"),
            Arguments.of("1.0.0-alpha", "1.0.0-alpha.0.0.0"),
            Arguments.of("1.0", "1.0+abc.def.ghi")
        )
        
        @JvmStatic
        fun testFirstSmallerSecondCases(): List<Arguments> = listOf(
            Arguments.of("0", "1"),
            Arguments.of("0.2", "0.10"),
            Arguments.of("1.0-snapshot", "1.0-alpha"),
            Arguments.of("1.0-alpha", "1.0-beta"),
            Arguments.of("1.0-beta", "1.0-rc"),
            Arguments.of("1.0-rc", "1.0"),
            Arguments.of("1.0-snapshot.1", "1.0-snapshot.2"),
            Arguments.of("1.0-alpha.1", "1.0-alpha.2"),
            Arguments.of("1.0-beta.1", "1.0-beta.2"),
            Arguments.of("1.0-rc.1", "1.0-rc.2")
        )
        
        @JvmStatic
        fun testToStringOmitZerosCases(): List<Arguments> = listOf(
            Arguments.of("1.0.0", "1"),
            Arguments.of("1.2.0.0", "1.2"),
            Arguments.of("1.2.0-alpha.1.0.0", "1.2-alpha.1"),
            Arguments.of("1.0-alpha.0", "1-alpha"),
            Arguments.of("0", "0")
        )
        
        @JvmStatic
        fun testEqualsIgnoringCases(): List<Arguments> = listOf(
            Arguments.of("0", "1", 0),
            Arguments.of("1.0", "1.1", 1),
            Arguments.of("1.0.0", "1.0.1", 2)
        )
        
        @JvmStatic
        fun testNotEqualsIgnoringCases(): List<Arguments> = listOf(
            Arguments.of("0", "1", 1),
            Arguments.of("1.0", "1.1", 2),
            Arguments.of("1.0.0", "1.0.1", 3)
        )
        
    }
    
}