@file:OptIn(UnstableProviderApi::class)

package xyz.xenondevs.commons.provider

import kotlin.concurrent.withLock

/**
 * Creates a new [MutableProvider] that does not propagate changes upwards to the parent.
 * It will still receive changes from the parent, but changes made the returned provider
 * will not affect the parent.
 */
fun <T> MutableProvider<T>.detached(): MutableProvider<T> =
    DetachedProvider(this as AbstractProvider<T>)

private class DetachedProvider<T>(
    private val parent: AbstractProvider<T>
) : AbstractProvider<T>(parent.lock) {
    
    init {
        lock.withLock {
            addInactiveParent(parent)
            parent.addChild(this)
        }
    }
    
    override fun pull(): T {
        return parent.get()
    }
    
}