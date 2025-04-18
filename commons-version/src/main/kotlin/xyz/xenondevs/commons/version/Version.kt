package xyz.xenondevs.commons.version

import kotlin.math.max

private val RELEASE_STAGES: Map<String, Int> = buildMap {
    this["rc"] = -1
    this["beta"] = -2
    this["alpha"] = -3
    this["snapshot"] = -4
}

private const val VERSION_NUMBER_GROUP = """((?:0|[1-9]\d*)(?:\.(?:0|[1-9]\d*))*)"""
private const val RELEASE_STAGES_GROUP = """(?i)(snapshot|alpha|beta|rc)(?-i)"""
private const val METADATA_GROUP = """((?:[0-9A-Za-z-]+)(?:\.[0-9A-Za-z-]+)*)"""
private val VERSION_REGEX = Regex($$"""^$$VERSION_NUMBER_GROUP(?:-$$RELEASE_STAGES_GROUP(?:\.$$VERSION_NUMBER_GROUP)?)?(?:\+$$METADATA_GROUP)?$""")

/**
 * Represents a version in a format consisting of the following three components:
 * 1. Dot-separated version number of infinite length (ex. `1.0.0.0.0.1`, `1.0.0`, `1`, etc.).
 * Individual components must be non-negative integers and cannot contain leading zeros, except for `0` itself.
 * 2. Optional pre-release stage (any of `snapshot`, `alpha`, `beta`, `rc` (ignoring capitalization)), separated by a dash from the main
 * version number and followed by an optional pre-release version number as in (1). (ex. `1.0.0-alpha`, `1.0.0-beta.1.2.3`)
 * 3. Optional metadata, separated by a plus sign from the main version number. Metadata consists of one or multiple dot-separated
 * identifiers, which themselves are non-empty alphanumeric strings. (ex. `1.0.0+build.123`, `1.0.0+build.123+build.456`)
 *
 * When comparing version numbers, the dot-separated components of version numbers are compared individually,
 * such that `1.11 > 1.2` and `no pre-release stage > rc > beta > alpha > snapshot`.
 * Metadata is ignored when comparing versions.
 * 
 * This class loosely follows the [SemVer](https://semver.org/) specification, but allows variable-length version numbers
 * and restricts the pre-release stages to `snapshot`, `alpha`, `beta` and `rc`.
 */
class Version : Comparable<Version> {
    
    private val version: IntArray
    private val stageVersion: IntArray
    private val stage: String? // stage in original capitalization
    
    /**
     * The metadata of the version, or `null` if there is none.
     */
    val metadata: String?
    
    /**
     * Whether this version is a full release, i.e. it has no pre-release stage (`alpha`, `beta`, `rc` or `snapshot`).
     */
    val isFullRelease: Boolean
        get() = stageVersion.isEmpty()
    
    /**
     * Creates a new release version with the given version number and no metadata.
     */
    constructor(vararg version: Int) {
        this.version = version
        this.stageVersion = intArrayOf()
        this.stage = null
        this.metadata = null
    }
    
    /**
     * Creates a new version by parsing the given [version] string.
     * @throws IllegalArgumentException If the version string is invalid.
     */
    constructor(version: String) {
        val result = VERSION_REGEX.matchEntire(version)
            ?: throw IllegalArgumentException("$version does not match version regex $VERSION_REGEX")
        
        val ver = result.groupValues[1]
        val stage = result.groupValues[2].takeUnless(String::isBlank)
        val stageVer = result.groupValues[3].takeUnless(String::isBlank)
        
        this.version = ver.split('.').map { it.toIntOrNull() ?: 0 }.toIntArray()
        
        if (stage != null) {
            this.stageVersion = buildList<Int> {
                this += RELEASE_STAGES[stage.lowercase()] ?: throw IllegalArgumentException("Unknown release stage: $stage")
                if (stageVer != null) this += stageVer.split('.').map { it.toIntOrNull() ?: 0 }
            }.toIntArray()
            this.stage = stage
        } else {
            this.stageVersion = intArrayOf()
            this.stage = null
        }
        
        this.metadata = result.groupValues[4].takeUnless(String::isBlank)
    }
    
    override fun toString(): String =
        toString(omitZeros = false)
    
    /**
     * Converts this version to a string, using the given [separator]
     *
     * @param separator The separator to use between the version parts. Defaults to `.`.
     * @param omitZeros Whether to omit trailing zeros in the version number (i.e. `1.0.0` -> `1`). Defaults to `false`.
     */
    fun toString(separator: String = ".", omitZeros: Boolean = false): String {
        val sb = StringBuilder()
        
        fun isAllZeros(start: Int, array: IntArray) =
            array.copyOfRange(start, array.size).all { it == 0 }
        
        fun appendVersion(start: Int, array: IntArray) {
            for (i in start..array.lastIndex) {
                sb.append(array[i])
                if (i < array.lastIndex) {
                    if (omitZeros && isAllZeros(i + 1, array))
                        break
                    sb.append(separator)
                }
            }
        }
        
        appendVersion(0, version)
        
        if (stageVersion.isNotEmpty()) {
            sb.append("-")
            sb.append(stage)
            if (stageVersion.size > 1 && (!omitZeros || !isAllZeros(1, stageVersion))) {
                sb.append(".")
                appendVersion(1, stageVersion)
            }
        }
        
        if (metadata != null) {
            sb.append("+")
            sb.append(metadata)
        }
        
        return sb.toString()
    }
    
    override fun compareTo(other: Version) = compareTo(other, -1)
    
    /**
     * Compares this [Version] to [other].
     *
     * @param ignoreIdx Specifies the first index of the main version number that should not be compared.
     * (ex: with `ignoreIdx = 2`, `1.0.1` equals `1.0.2`)
     */
    fun compareTo(other: Version, ignoreIdx: Int): Int {
        val compare = compareVersionArray(version, other.version, ignoreIdx)
        if (compare != 0 || ignoreIdx != -1) // only compare stage version if an exact comparison (ignoreIdx = -1) was requested
            return compare
        
        return compareVersionArray(stageVersion, other.stageVersion, -1)
    }
    
    private fun compareVersionArray(a: IntArray, b: IntArray, ignoreIdx: Int): Int {
        val size = max(a.size, b.size)
        
        for (i in 0..<size) {
            if (i == ignoreIdx)
                return 0
            
            val myPart = a.getOrElse(i) { 0 }
            val otherPart = b.getOrElse(i) { 0 }
            
            val compare = myPart.compareTo(otherPart)
            if (compare != 0)
                return compare
        }
        
        return 0
    }
    
    /**
     * Creates a new [ClosedVersionRange] from this version to the given [other] version.
     */
    operator fun rangeTo(other: Version) = ClosedVersionRange(this, other)
    
    /**
     * Creates a new [OpenEndVersionRange] from this version to the given [other] version.
     */
    operator fun rangeUntil(other: Version) = OpenEndVersionRange(this, other)
    
    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (other !is Version)
            return false
        
        return equals(other, -1)
    }
    
    /**
     * Checks if this version is equal to [other].
     *
     * @param ignoreIdx Specifies the first index of the main version number that should not be compared.
     * (ex: with `ignoreIdx = 2`, `1.0.1` equals `1.0.2`)
     */
    fun equals(other: Version, ignoreIdx: Int): Boolean =
        compareTo(other, ignoreIdx) == 0
    
    override fun hashCode(): Int {
        val lastNonZeroVersion = version.indexOfLast { it != 0 }
        val lastNonZeroStageVersion = stageVersion.indexOfLast { it != 0 }
        
        var hashCode = 1
        for (i in 0..lastNonZeroVersion) {
            hashCode = 31 * hashCode + version[i]
        }
        for (i in 0..lastNonZeroStageVersion) {
            hashCode = 31 * hashCode + stageVersion[i]
        }
        
        return hashCode
    }
    
}