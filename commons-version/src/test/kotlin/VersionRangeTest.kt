
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import xyz.xenondevs.commons.version.Version

class VersionRangeTest {
    
    @ParameterizedTest
    @MethodSource("testInClosedRangeCases")
    fun testInClosedRange(start: String, end: String, test: String) {
        assert(Version(test) in Version(start)..Version(end))
    }
    
    @ParameterizedTest
    @MethodSource("testNotInClosedRangeCases")
    fun testNotInClosedRange(start: String, end: String, test: String) {
        assert(Version(test) !in Version(start)..Version(end))
    }
    
    @ParameterizedTest
    @MethodSource("testInOpenEndRangeCases")
    fun testInOpenEndRange(start: String, end: String, test: String) {
        assert(Version(test) in Version(start)..<Version(end))
    }
    
    @ParameterizedTest
    @MethodSource("testNotInOpenEndRangeCases")
    fun testNotInOpenEndRange(start: String, end: String, test: String) {
        assert(Version(test) !in Version(start)..<Version(end))
    }
    
    companion object { 
        
        @JvmStatic
        fun testInClosedRangeCases(): List<Arguments> = listOf(
            Arguments.of("0", "1", "0.1"),
            Arguments.of("0.1", "0.2", "0.1"),
            Arguments.of("0.1", "0.2", "0.2"),
            Arguments.of("0.1-alpha", "0.1-beta", "0.1-alpha.44+abc")
        )
        
        @JvmStatic
        fun testNotInClosedRangeCases(): List<Arguments> = listOf(
            Arguments.of("0", "1", "1.1"),
            Arguments.of("0.1-alpha+a", "0.1-beta+b", "0.1-beta.44+abc"),
        )
        
        @JvmStatic
        fun testInOpenEndRangeCases(): List<Arguments> = listOf(
            Arguments.of("0", "1", "0.1"),
            Arguments.of("0.1", "0.2", "0.1"),
            Arguments.of("0.1-alpha", "0.1-beta", "0.1-alpha.44+abc")
        )
        
        @JvmStatic
        fun testNotInOpenEndRangeCases(): List<Arguments> = listOf(
            Arguments.of("0", "1", "1.1"),
            Arguments.of("0.1", "0.2", "0.2"),
            Arguments.of("0.1-alpha+a", "0.1-beta+b", "0.1-beta.44+abc"),
        )
        
    }
}