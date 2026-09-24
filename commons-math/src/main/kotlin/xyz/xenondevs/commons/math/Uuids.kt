package xyz.xenondevs.commons.math

import java.util.*
import java.util.concurrent.ThreadLocalRandom

/**
 * Generates a random [UUID] using the insecure [ThreadLocalRandom.current].
 */
fun insecureRandomUuid(): UUID {
    val random = ThreadLocalRandom.current()
    return UUID(
        (random.nextLong() and -0xF001L) or 0x4000L,
        (random.nextLong() and 0x3FFF_FFFF_FFFF_FFFFL) or Long.MIN_VALUE
    )
}