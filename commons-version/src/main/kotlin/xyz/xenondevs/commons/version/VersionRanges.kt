package xyz.xenondevs.commons.version

/**
 * A range of versions, from [start] (inclusive) to [endInclusive] (inclusive).
 */
data class ClosedVersionRange(
    override val start: Version,
    override val endInclusive: Version
) : ClosedRange<Version> {
    
    override fun toString(): String = "[$start; $endInclusive]"
    
}

/**
 * A range of versions, from [start] (inclusive) to [endExclusive] (exclusive).
 */
data class OpenEndVersionRange(
    override val start: Version,
    override val endExclusive: Version
): OpenEndRange<Version> {
    
    override fun toString(): String = "[$start; $endExclusive["
    
}