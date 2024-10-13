package xyz.xenondevs.commons.provider

import xyz.xenondevs.commons.collections.weakHashSet
import java.util.*
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

private sealed interface InternalProviderValue {
    object Uninitialized : InternalProviderValue
}

@UnstableProviderApi
private class PushSource<C, P>(
    private val child: AbstractProvider<C>,
    private val push: PushFunction<C, P>
) : InternalProviderValue {
    
    fun load(): P {
        return push(child.get())
    }
    
}

@UnstableProviderApi
private typealias PushFunction<C, P> = (childValue: C) -> P

@UnstableProviderApi
private class ParentProviderWrapper<C, P>(
    val child: AbstractProvider<C>,
    val parent: AbstractProvider<P>,
    val push: PushFunction<C, P>,
    val ignored: Set<AbstractProvider<*>> // set of providers which should not cause parent.onChildChanged when updating child (only one level deep)
) {
    
    fun onChildChanged(changed: AbstractProvider<*>) {
        if (changed in ignored)
            return
        
        parent.onChildChanged(child, push)
    }
    
}

@UnstableProviderApi
abstract class AbstractProvider<T>(
    lock: ReentrantLock
) : MutableProvider<T> {
    
    @Volatile
    var lock: ReentrantLock = lock
        private set
    
    /**
     * The value of the provider.
     * May be [InternalProviderValue.Uninitialized] to pull from a parent.
     * May be [PushSource] to push from a child.
     */
    @Volatile
    private var value: Any? = InternalProviderValue.Uninitialized
    
    private var activeParents: MutableSet<ParentProviderWrapper<T, *>>? = null
    private var inactiveParents: MutableSet<AbstractProvider<*>>? = null
    private var inactiveChildren: MutableSet<AbstractProvider<*>>? = null
    private var activeChildren: MutableSet<AbstractProvider<*>>? = null
    private var subscribers: MutableList<(T) -> Unit>? = null
    private var weakSubscribers: MutableMap<Any, ArrayList<(Any, T) -> Unit>>? = null
    
    val parents: Set<AbstractProvider<*>>
        get() = lock.withLock { 
            val parents = HashSet<AbstractProvider<*>>()
            activeParents?.forEach { parents.add(it.parent) }
            inactiveParents?.forEach { parents.add(it) }
            parents
        }
    
    val children: Set<AbstractProvider<*>>
        get() = lock.withLock {
            val children = HashSet<AbstractProvider<*>>()
            activeChildren?.forEach { children.add(it) }
            inactiveChildren?.forEach { children.add(it) }
            children
        }
    
    @Suppress("UNCHECKED_CAST")
    override fun get(): T {
        val value1 = value
        if (value1 !is InternalProviderValue)
            return value1 as T
        
        lock.withLock {
            val value2 = value
            if (value2 !is InternalProviderValue)
                return value2 as T
            
            if (value2 is PushSource<*, *>) {
                value = value2.load()
            } else {
                value = pull()
            }
            
            return value as T
        }
    }
    
    override fun set(value: T) = lock.withLock {
        if (this.value != value) {
            this.value = value
            onSelfChanged()
        }
    }
    
    override fun subscribe(action: (T) -> Unit): Unit = lock.withLock {
        if (subscribers == null)
            subscribers = ArrayList(1)
        subscribers!!.add(action)
    }
    
    @Suppress("UNCHECKED_CAST")
    override fun <R : Any> subscribeWeak(owner: R, action: (R, T) -> Unit): Unit = lock.withLock {
        if (weakSubscribers == null)
            weakSubscribers = WeakHashMap(1)
        weakSubscribers!!.getOrPut(owner) { ArrayList(1) } += action as (Any, T) -> Unit
    }
    
    override fun unsubscribe(action: (T) -> Unit): Unit = lock.withLock {
        subscribers?.remove(action)
    }
    
    @Suppress("UNCHECKED_CAST")
    override fun <R : Any> unsubscribeWeak(owner: R, action: (R, T) -> Unit): Unit = lock.withLock {
        action as (Any, T) -> Unit
        weakSubscribers?.get(owner)?.remove(action)
    }
    
    override fun <R : Any> unsubscribeWeak(owner: R): Unit = lock.withLock {
        weakSubscribers?.remove(owner)
    }
    
    fun onSelfChanged() {
        assert(lock.isHeldByCurrentThread)
        
        fireSubscribers()
        
        activeParents?.forEach { it.onChildChanged(this) }
        activeChildren?.forEach { it.onParentChanged(this) }
    }
    
    fun onParentChanged(changedParent: AbstractProvider<*>) {
        assert(lock.isHeldByCurrentThread)
        
        value = InternalProviderValue.Uninitialized
        
        fireSubscribers()
        
        activeParents?.forEach {
            if (it.parent != changedParent)
                it.onChildChanged(changedParent)
        }
        activeChildren?.forEach { it.onParentChanged(this) }
    }
    
    fun <C> onChildChanged(changedChild: AbstractProvider<C>, push: PushFunction<C, T>) {
        assert(lock.isHeldByCurrentThread)
        
        value = PushSource(changedChild, push)
        fireSubscribers()
        
        activeParents?.forEach { it.onChildChanged(changedChild) }
        activeChildren?.forEach {
            if (it != changedChild)
                it.onParentChanged(this)
        }
    }
    
    private fun fireSubscribers() { // TODO: fire outside of lock ?
        assert(lock.isHeldByCurrentThread)
        
        if (subscribers.isNullOrEmpty() && weakSubscribers.isNullOrEmpty())
            return
        
        val value = get()
        subscribers?.forEach { it(value) }
        weakSubscribers?.forEach { (owner, actions) ->
            for (action in actions) {
                action(owner, value)
            }
        }
    }
    
    fun <P> addParent(
        parent: AbstractProvider<P>,
        ignored: Set<AbstractProvider<*>> = emptySet(),
        push: PushFunction<T, P>
    ) {
        assert(lock.isHeldByCurrentThread)
        if (parent.lock != lock)
            throw IllegalArgumentException("Mismatching locks")
        
        if (activeParents == null)
            activeParents = HashSet(1)
        activeParents!!.add(ParentProviderWrapper(this, parent, push, ignored))
    }
    
    fun removeParent(parent: AbstractProvider<*>) {
        assert(lock.isHeldByCurrentThread)
        activeParents?.removeIf { it.parent == parent }
    }
    
    fun addInactiveParent(parent: AbstractProvider<*>) {
        assert(lock.isHeldByCurrentThread)
        if (parent.lock != lock)
            throw IllegalArgumentException("Mismatching locks")
        
        if (inactiveParents == null)
            inactiveParents = HashSet(1)
        inactiveParents!!.add(parent)
    }
    
    fun removeInactiveParent(parent: AbstractProvider<*>) {
        assert(lock.isHeldByCurrentThread)
        inactiveParents?.remove(parent)
    }
    
    fun addInactiveParents(vararg parents: AbstractProvider<*>) {
        assert(lock.isHeldByCurrentThread)
        
        for (parent in parents) {
            if (parent.lock != lock)
                throw IllegalArgumentException("Mismatching locks")
            
            if (inactiveParents == null)
                inactiveParents = HashSet(1)
            inactiveParents!!.add(parent)
        }
    }
    
    fun addChild(child: AbstractProvider<*>) {
        assert(lock.isHeldByCurrentThread)
        if (child.lock != lock)
            throw IllegalArgumentException("Mismatching locks")
        
        if (activeChildren == null)
            activeChildren = weakHashSet(1)
        activeChildren!!.add(child)
    }
    
    fun removeChild(child: AbstractProvider<*>) {
        assert(lock.isHeldByCurrentThread)
        activeChildren?.remove(child)
    }
    
    fun addInactiveChild(child: AbstractProvider<*>) {
        assert(lock.isHeldByCurrentThread)
        if (child.lock != lock)
            throw IllegalArgumentException("Mismatching locks")
        
        if (inactiveChildren == null)
            inactiveChildren = weakHashSet(1)
        inactiveChildren!!.add(child)
    }
    
    fun removeInactiveChild(child: AbstractProvider<*>) {
        assert(lock.isHeldByCurrentThread)
        inactiveChildren?.remove(child)
    }
    
    fun addChildren(vararg children: AbstractProvider<*>) {
        assert(lock.isHeldByCurrentThread)
        
        for (child in children) {
            if (child.lock != lock)
                throw IllegalArgumentException("Mismatching locks")
            
            if (activeChildren == null)
                activeChildren = weakHashSet(1)
            activeChildren!!.add(child)
        }
    }
    
    fun addInactiveChildren(vararg children: AbstractProvider<*>) {
        assert(lock.isHeldByCurrentThread)
        
        for (child in children) {
            if (child.lock != lock)
                throw IllegalArgumentException("Mismatching locks")
            
            if (inactiveChildren == null)
                inactiveChildren = weakHashSet(1)
            inactiveChildren!!.add(child)
        }
    }
    
    fun changeLock(lock: ReentrantLock): Unit = lock.withLock {
        if (this.lock == lock)
            return
        this.lock = lock
        
        activeChildren?.forEach { it.changeLock(lock) }
        inactiveChildren?.forEach { it.changeLock(lock) }
        activeParents?.forEach { it.parent.changeLock(lock) }
        inactiveParents?.forEach { it.changeLock(lock) }
    }
    
    abstract fun pull(): T
    
}

@OptIn(UnstableProviderApi::class)
internal fun AbstractProvider<*>.addAsChildTo(vararg parents: AbstractProvider<*>) {
    for (parent in parents) {
        parent.addChild(this)
    }
}

@OptIn(UnstableProviderApi::class)
internal fun changeLocks(lock: ReentrantLock, vararg providers: AbstractProvider<*>) {
    for (provider in providers) {
        provider.changeLock(lock)
    }
}