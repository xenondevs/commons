package xyz.xenondevs.commons.provider

import org.junit.jupiter.api.Test

class ParallelProviderTest {
    
    @Test
    fun testCombinedMappingProvider2() {
        val a = mutableProvider(1)
        val b = mutableProvider(1)
        
        runParallel { combinedProvider(a, b) { a, b -> a + b } }
    }
    
    @Test
    fun testCombinedMappingProvider3() {
        val a = mutableProvider(1)
        val b = mutableProvider(1)
        val c = mutableProvider(1)
        
        runParallel {
            combinedProvider(
                a, b, c
            ) { a, b, c ->
                a + b + c
            }
        }
    }
    
    @Test
    fun testCombinedMappingProvider4() {
        val a = mutableProvider(1)
        val b = mutableProvider(1)
        val c = mutableProvider(1)
        val d = mutableProvider(1)
        
        runParallel {
            combinedProvider(
                a, b, c, d
            ) { a, b, c, d ->
                a + b + c + d 
            }
        }
    }
    
    @Test
    fun testCombinedMappingProvider5() {
        val a = mutableProvider(1)
        val b = mutableProvider(1)
        val c = mutableProvider(1)
        val d = mutableProvider(1)
        val e = mutableProvider(1)
        
        runParallel {
            combinedProvider(
                a, b, c, d, e
            ) { a, b, c, d, e ->
                a + b + c + d + e
            }
        }
    }
    
    @Test
    fun testCombinedMappingProvider6() {
        val a = mutableProvider(1)
        val b = mutableProvider(1)
        val c = mutableProvider(1)
        val d = mutableProvider(1)
        val e = mutableProvider(1)
        val f = mutableProvider(1)
        
        runParallel {
            combinedProvider(
                a, b, c, d, e, f
            ) { a, b, c, d, e, f ->
                a + b + c + d + e + f
            }
        }
    }
    
    @Test
    fun testCombinedMappingProvider7() {
        val a = mutableProvider(1)
        val b = mutableProvider(1)
        val c = mutableProvider(1)
        val d = mutableProvider(1)
        val e = mutableProvider(1)
        val f = mutableProvider(1)
        val g = mutableProvider(1)
        
        runParallel {
            combinedProvider(
                a, b, c, d, e, f, g
            ) { a, b, c, d, e, f, g ->
                a + b + c + d + e + f + g
            }
        }
    }
    
    @Test
    fun testCombinedMappingProvider8() {
        val a = mutableProvider(1)
        val b = mutableProvider(1)
        val c = mutableProvider(1)
        val d = mutableProvider(1)
        val e = mutableProvider(1)
        val f = mutableProvider(1)
        val g = mutableProvider(1)
        val h = mutableProvider(1)
        
        runParallel {
            combinedProvider(
                a, b, c, d, e, f, g, h
            ) { a, b, c, d, e, f, g, h->
                a + b + c + d + e + f + g + h
            }
        }
    }
    
    @Test
    fun testCombinedMappingProvider9() {
        val a = mutableProvider(1)
        val b = mutableProvider(1)
        val c = mutableProvider(1)
        val d = mutableProvider(1)
        val e = mutableProvider(1)
        val f = mutableProvider(1)
        val g = mutableProvider(1)
        val h = mutableProvider(1)
        val i = mutableProvider(1)
        
        runParallel {
            combinedProvider(
                a, b, c, d, e, f, g, h, i
            ) { a, b, c, d, e, f, g, h, i ->
                a + b + c + d + e + f + g + h + i
            }
        }
    }
    
    @Test
    fun testCombinedMappingProvider10() {
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
    
    private fun runParallel(run: () -> Unit) {
        var failed = false
        val threads = Array(100) {
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