package xyz.xenondevs.commons.provider

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals

class ParallelProviderTest {
    
    companion object {
        
        @JvmStatic
        fun rootMiddleLeafThreadConfigurations(): List<Arguments> = listOf(
            Arguments.of(10, 10, 10),
            Arguments.of(0, 10, 10),
            Arguments.of(10, 0, 10),
            Arguments.of(10, 10, 0),
            Arguments.of(1, 1, 1),
            Arguments.of(0, 1, 1),
            Arguments.of(1, 0, 1),
            Arguments.of(1, 1, 0),
        )
        
    }
    
    @MethodSource("rootMiddleLeafThreadConfigurations")
    @ParameterizedTest
    fun `test parallel writes to bidirectional transforming provider`(
        nRootThreads: Int,
        nMiddleThreads: Int,
        nLeafThreads: Int
    ) {
        val root = mutableProvider(0)
        
        val middle = root.map(
            { it * 2 }, { it / 2 }
        )
        
        val leaf1 = middle.map(
            { it + 1 },
            { it - 1 }
        )
        val leaf2 = middle.map(
            { it + 2 },
            { it - 2 }
        )
        
        fun updateRoot() {
            for (i in -100_000..100_000) {
                root.set(i)
            }
        }
        
        fun updateMiddle() {
            for (i in -100_000..100_000) {
                middle.set(i)
            }
        }
        
        fun updateLeaf1() {
            for (i in -100_000..100_000) {
                leaf1.set(i)
            }
        }
        
        val rootThreads = Array(nRootThreads) { Thread { updateRoot() } }
        val middleThreads = Array(nMiddleThreads) { Thread { updateMiddle() } }
        val leafThreads = Array(nLeafThreads) { Thread { updateLeaf1() } }
        
        listOf(*rootThreads, *middleThreads, *leafThreads).shuffled()
            .onEach { it.start() }
            .onEach { it.join() }
        
        
        // 3 possible scenarios: root thread won, middle thread won, leaf thread won
        // for each scenario, assert that the other providers are in the appropriate state
        
        if (root.get() == 100_000) {
            // a root thread won
            assertEquals(200_000, middle.get())
            assertEquals(200_001, leaf1.get())
            assertEquals(200_002, leaf2.get())
        } else if (middle.get() == 100_000) {
            // a middle thread won
            assertEquals(50_000, root.get())
            assertEquals(100_001, leaf1.get())
            assertEquals(100_002, leaf2.get())
        } else if (leaf1.get() == 100_000) {
            // a leaf1 thread won
            assertEquals(49_999, root.get())
            assertEquals(99_999, middle.get())
            assertEquals(100_001, leaf2.get())
        } else {
            throw AssertionError()
        }
    }
    
    @Test
    fun testCombinedMappingProviderInit() {
        val a = mutableProvider(1)
        val b = mutableProvider(1)
        val c = mutableProvider(1)
        val d = mutableProvider(1)
        val e = mutableProvider(1)
        val f = mutableProvider(1)
        val g = mutableProvider(1)
        val h = mutableProvider(1)
        val i = mutableProvider(1)
        val j = mutableProvider(1)
        
        runParallel {
            combinedProvider(
                a, b, c, d, e, f, g, h, i, j
            ) { a, b, c, d, e, f, g, h, i, j ->
                a + b + c + d + e + f + g + h + i + j
            }
        }
    }
    
    private fun runParallel(nThreads: Int = 100, run: () -> Unit) {
        var failed = false
        val threads = Array(nThreads) {
            Thread {
                try {
                    run()
                } catch (t: Throwable) {
                    failed = true
                    throw t
                }
            }
        }
        
        threads.forEach(Thread::start)
        threads.forEach(Thread::join)
        
        if (failed)
            throw AssertionError("Failed")
    }
    
}