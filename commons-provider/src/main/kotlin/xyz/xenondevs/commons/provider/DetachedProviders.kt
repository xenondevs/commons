@file:JvmName("Providers")
@file:JvmMultifileClass
@file:OptIn(UnstableProviderApi::class)

package xyz.xenondevs.commons.provider

import java.lang.ref.WeakReference
import kotlin.concurrent.withLock

/**
 * Creates a new [MutableProvider] that does not propagate changes upwards to the parent.
 * It will still receive changes from the parent, but changes made the returned provider
 * will not affect the parent.
 */
fun <T> MutableProvider<T>.detached(): MutableProvider<T> =
    DetachedProvider(this as AbstractProvider<T>, false)

/**
 * Creates a new [MutableProvider] that does not propagate changes upwards to the parent.
 * It will still receive changes from the parent, but changes made the returned provider
 * will not affect the parent.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
fun <T> MutableProvider<T>.weakDetached(): MutableProvider<T> =
    DetachedProvider(this as AbstractProvider<T>, true)

private class DetachedProvider<T>(
    private val parent: AbstractProvider<T>,
    weak: Boolean
) : AbstractProvider<T>(parent.lock) {
    
    init {
        lock.withLock {
            addInactiveParent(parent)
            parent.addChild(active = true, weak = weak, child = this)
        }
    }
    
    override fun pull(): T {
        return parent.get()
    }
    
}