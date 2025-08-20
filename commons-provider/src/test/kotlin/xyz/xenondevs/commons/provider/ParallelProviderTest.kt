package xyz.xenondevs.commons.provider

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import xyz.xenondevs.commons.provider.util.ObservableList
import java.util.concurrent.Executors
import java.util.concurrent.Future
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
    
    @Test
    fun `test for lost update on provider creation via map`() {
        testForProviderCreationLostUpdate(0, 1, 2) { provider -> provider.map { it + 1 } }
    }
    
    @Test
    fun `test for lost update on provider creation via strongMap`() {
        testForProviderCreationLostUpdate(0, 1, 2) { provider -> provider.strongMap { it + 1 } }
    }
    
    @Test
    fun `test for lost update on provider creation via immediateFlatMap`() {
        val selection = listOf(provider(1), provider(2))
        testForProviderCreationLostUpdate(0, 1, 2) { provider -> provider.immediateFlatMap { selection[it] } }
    }
    
    @Test
    fun `test for lost update on provider creation via strongImmediateFlatMap`() {
        val selection = listOf(provider(1), provider(2))
        testForProviderCreationLostUpdate(0, 1, 2) { provider -> provider.strongImmediateFlatMap { selection[it] } }
    }
    
    @Test
    fun `test for lost update on provider creation via immediateFlatMapMutable`() {
        val selection = listOf(mutableProvider(1), mutableProvider(2))
        testForProviderCreationLostUpdate(0, 1, 2) { provider -> provider.immediateFlatMapMutable { selection[it] } }
    }
    
    @Test
    fun `test for lost update on provider creation via strongImmediateFlatMapMutable`() {
        val selection = listOf(mutableProvider(1), mutableProvider(2))
        testForProviderCreationLostUpdate(0, 1, 2) { provider -> provider.strongImmediateFlatMapMutable { selection[it] } }
    }
    
    @Test
    fun `test for lost update on provider creation via flatMap`() {
        val selection = listOf(provider(1), provider(2))
        testForProviderCreationLostUpdate(0, 1, 2) { provider -> provider.flatMap { selection[it] } }
    }
    
    @Test
    fun `test for lost update on provider creation via strongFlatMap`() {
        val selection = listOf(provider(1), provider(2))
        testForProviderCreationLostUpdate(0, 1, 2) { provider -> provider.strongFlatMap { selection[it] } }
    }
    
    @Test
    fun `test for lost update on provider creation via flatMapMutable`() {
        val selection = listOf(mutableProvider(1), mutableProvider(2))
        testForProviderCreationLostUpdate(0, 1, 2) { provider -> provider.flatMapMutable { selection[it] } }
    }
    
    @Test
    fun `test for lost update on provider creation via strongFlatMapMutable`() {
        val selection = listOf(mutableProvider(1), mutableProvider(2))
        testForProviderCreationLostUpdate(0, 1, 2) { provider -> provider.strongFlatMapMutable { selection[it] } }
    }
    
    @Test
    fun `test for lost update on provider creation via mapObserved`() {
        testForProviderCreationLostUpdate(
            mutableListOf(),
            mutableListOf("a"),
            mutableListOf("a")
        ) { provider -> provider.mapObserved(::ObservableList) }
    }
    
    @Test
    fun `test for lost update on provider creation via strongMapObserved`() {
        testForProviderCreationLostUpdate(
            mutableListOf(),
            mutableListOf("a"),
            mutableListOf("a")
        ) { provider -> provider.strongMapObserved(::ObservableList) }
    }
    
    @Test
    fun `test for lost update on provider creation via combinedProvider`() {
        val b = provider("b")
        testForProviderCreationLostUpdate("", "a", "ab") { combinedProvider(it, b) { a, b -> a + b } }
    }
    
    @Test
    fun `test for lost update on provider creation via strongCombinedProvider`() {
        val b = provider("b")
        testForProviderCreationLostUpdate("", "a", "ab") { strongCombinedProvider(it, b) { a, b -> a + b } }
    }
    
    private fun <T, R> testForProviderCreationLostUpdate(
        initialRootValue: T,
        newRootValue: T,
        expectedResult: R,
        createResultProvider: (MutableProvider<T>) -> Provider<R>
    ) {
        val executor = Executors.newFixedThreadPool(100)
        repeat(100_000) {
            val root: MutableProvider<T> = mutableProvider(initialRootValue)
            val result: Future<Provider<R>> = executor.submit<Provider<R>> { createResultProvider(root) }
            executor.submit { root.set(newRootValue) }.get()
            
            assertEquals(expectedResult, result.get().get(), "Iteration $it")
        }
    }
    
}