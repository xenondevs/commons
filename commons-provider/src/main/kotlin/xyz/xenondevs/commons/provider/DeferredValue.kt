package xyz.xenondevs.commons.provider

import xyz.xenondevs.commons.provider.DeferredValue.Companion.nextSeqNo
import xyz.xenondevs.commons.provider.DeferredValue.Companion.seqNo
import java.util.concurrent.atomic.AtomicLong

/**
 * A container for a [value] that is possibly computed lazily.
 */
interface DeferredValue<out T> : Comparable<DeferredValue<*>> {
    
    /**
     * The sequence number of this [DeferredValue]. The sequence number defines the "age" of the value,
     * where a lower sequence number indicates an older value. Sequence numbers are unique for each [DeferredValue] chain,
     * but are the same for all [DeferredValues][DeferredValue] that are derived from the same source.
     * Use [nextSeqNo] to generate a new unique sequence number.
     */
    val seqNo: Long
    
    /**
     * The possibly lazily initialized value of this [DeferredValue].
     * Retrieving this value will initialize it if it hasn't been initialized yet.
     * Once initialized, the value will not change.
     */
    val value: T
    
    /**
     * Compares this [DeferredValue] with another [DeferredValue] based on their [seqNo].
     * Equivalent to `seqNo.compareTo(other.seqNo)`.
     */
    override fun compareTo(other: DeferredValue<*>): Int = seqNo.compareTo(other.seqNo)
    
    companion object {
        
        private val seqNo = AtomicLong(0L)
        
        /**
         * Generates the next unique sequence number.
         */
        fun nextSeqNo(): Long = seqNo.incrementAndGet()
        
    }
    
    /**
     * A [DeferredValue] that is directly initialized with a value.
     * Generates a new sequence number when created.
     */
    class Direct<T>(override val value: T) : DeferredValue<T> {
        
        override val seqNo: Long = nextSeqNo()
        
    }
    
    // may be able to take advantage of Stable Values (https://openjdk.org/jeps/502) in the future
    /**
     * A [DeferredValue] that is backed by [lazy].
     * Generates a new sequence number when created.
     */
    class Lazy<T>(lazy: kotlin.Lazy<T>) : DeferredValue<T> {
        
        constructor(initializer: () -> T) : this(lazy(initializer))
        
        override val seqNo: Long = nextSeqNo()
        override val value: T by lazy
        
    }
    
    /**
     * A [DeferredValue] that is the result of applying [transform] to the value of [parent].
     * Inherits the sequence number from [parent].
     */
    class Mapped<P, T>(val parent: DeferredValue<P>, val transform: (P) -> T) : DeferredValue<T> {
        
        override val seqNo: Long get() = parent.seqNo
        override val value: T by lazy { transform(parent.value) }
        
    }
    
    /**
     * A [DeferredValue] that is the result of applying [transform] to the values of all [parents].
     * Inherits the highest sequence number of all [parents].
     */
    class MappedMulti<P, T>(val parents: List<DeferredValue<P>>, val transform: (List<P>) -> T) : DeferredValue<T> {
        
        override val seqNo: Long get() = parents.maxOf { it.seqNo }
        override val value: T by lazy { transform(parents.map { it.value }) }
        
    }
    
    /**
     * A [DeferredValue] that delegates its value to [parent] but generates a new sequence number when created.
     */
    class ReEmitted<T>(val parent: DeferredValue<T>): DeferredValue<T> {
        
        override val seqNo: Long = nextSeqNo()
        override val value: T get() = parent.value
        
    }
    
}