package xyz.xenondevs.commons.provider

import xyz.xenondevs.commons.collections.isNotNullOrEmpty
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
    
    fun onChildChanged(
        changed: AbstractProvider<*>,
        preparedSubscribers: MutableList<() -> Unit>
    ) {
        if (changed in ignored)
            return
        
        parent.onChildChanged(child, push, preparedSubscribers)
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
    private var activeChildren: MutableSet<AbstractProvider<*>>? = null
    private var weakActiveChildren: MutableSet<AbstractProvider<*>>? = null
    private var inactiveChildren: MutableSet<AbstractProvider<*>>? = null
    private var weakInactiveChildren: MutableSet<AbstractProvider<*>>? = null
    private var subscribers: MutableList<(T) -> Unit>? = null
    private var weakSubscribers: MutableMap<Any, MutableList<(Any, T) -> Unit>>? = null
    private var immediateSubscribers: MutableList<(T) -> Unit>? = null
    private var weakImmediateSubscribers: MutableMap<Any, MutableList<(Any, T) -> Unit>>? = null
    private var observers: MutableList<() -> Unit>? = null
    private var weakObservers: MutableMap<Any, MutableList<(Any) -> Unit>>? = null
    
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
    
    override fun set(value: T) {
        val preparedSubscribers = ArrayList<() -> Unit>(0)
        
        lock.withLock {
            if (this.value != value) {
                this.value = value
                onSelfChanged(preparedSubscribers)
            }
        }
        
        for (subscriber in preparedSubscribers) {
            subscriber()
        }
    }
    
    override fun subscribe(action: (T) -> Unit): Unit = lock.withLock {
        if (subscribers == null)
            subscribers = ArrayList(1)
        subscribers!!.add(action)
    }
    
    fun subscribeImmediate(action: (T) -> Unit): Unit = lock.withLock {
        if (immediateSubscribers == null)
            immediateSubscribers = ArrayList(1)
        immediateSubscribers!!.add(action)
    }
    
    override fun observe(action: () -> Unit): Unit = lock.withLock {
        if (observers == null)
            observers = ArrayList(1)
        observers!!.add(action)
    }
    
    @Suppress("UNCHECKED_CAST")
    override fun <R : Any> subscribeWeak(owner: R, action: (R, T) -> Unit): Unit = lock.withLock {
        if (weakSubscribers == null)
            weakSubscribers = WeakHashMap(1)
        weakSubscribers!!.getOrPut(owner) { ArrayList(1) } += action as (Any, T) -> Unit
    }
    
    @Suppress("UNCHECKED_CAST")
    fun <R : Any> subscribeWeakImmediate(owner: R, action: (R, T) -> Unit): Unit = lock.withLock {
        if (weakImmediateSubscribers == null)
            weakImmediateSubscribers = WeakHashMap(1)
        weakImmediateSubscribers!!.getOrPut(owner) { ArrayList(1) } += action as (Any, T) -> Unit
    }
    
    @Suppress("UNCHECKED_CAST")
    override fun <R : Any> observeWeak(owner: R, action: (R) -> Unit): Unit = lock.withLock {
        if (weakObservers == null)
            weakObservers = WeakHashMap(1)
        weakObservers!!.getOrPut(owner) { ArrayList(1) } += action as (Any) -> Unit
    }
    
    override fun unsubscribe(action: (T) -> Unit): Unit = lock.withLock {
        subscribers?.remove(action)
    }
    
    fun unsubscribeImmediate(action: (T) -> Unit): Unit = lock.withLock {
        immediateSubscribers?.remove(action)
    }
    
    override fun unobserve(action: () -> Unit): Unit = lock.withLock {
        observers?.remove(action)
    }
    
    @Suppress("UNCHECKED_CAST")
    override fun <R : Any> unsubscribeWeak(owner: R, action: (R, T) -> Unit): Unit = lock.withLock {
        action as (Any, T) -> Unit
        weakSubscribers?.get(owner)?.remove(action)
    }
    
    @Suppress("UNCHECKED_CAST")
    fun <R : Any> unsubscribeWeakImmediate(owner: R, action: (R, T) -> Unit): Unit = lock.withLock {
        action as (Any, T) -> Unit
        weakImmediateSubscribers?.get(owner)?.remove(action)
    }
    
    @Suppress("UNCHECKED_CAST")
    override fun <R : Any> unobserveWeak(owner: R, action: (R, T) -> Unit): Unit = lock.withLock {
        action as (Any) -> Unit
        weakObservers?.get(owner)?.remove(action)
    }
    
    override fun <R : Any> unsubscribeWeak(owner: R): Unit = lock.withLock {
        weakSubscribers?.remove(owner)
    }
    
    fun <R : Any> unsubscribeWeakImmediate(owner: R): Unit = lock.withLock {
        weakImmediateSubscribers?.remove(owner)
    }
    
    override fun <R : Any> unobserveWeak(owner: R): Unit = lock.withLock {
        weakObservers?.remove(owner)
    }
    
    fun onSelfChanged(preparedSubscribers: MutableList<() -> Unit>) {
        assert(lock.isHeldByCurrentThread)
        
        prepareSubscribers(preparedSubscribers)
        
        activeParents?.forEach { it.onChildChanged(this, preparedSubscribers) }
        activeChildren?.forEach { it.onParentChanged(this, preparedSubscribers) }
        weakActiveChildren?.forEach { it.onParentChanged(this, preparedSubscribers) }
    }
    
    fun onParentChanged(
        changedParent: AbstractProvider<*>,
        preparedSubscribers: MutableList<() -> Unit>
    ) {
        assert(lock.isHeldByCurrentThread)
        
        value = InternalProviderValue.Uninitialized
        
        prepareSubscribers(preparedSubscribers)
        
        activeParents?.forEach {
            if (it.parent != changedParent)
                it.onChildChanged(changedParent, preparedSubscribers)
        }
        activeChildren?.forEach { it.onParentChanged(this, preparedSubscribers) }
        weakActiveChildren?.forEach { it.onParentChanged(this, preparedSubscribers) }
    }
    
    fun <C> onChildChanged(
        changedChild: AbstractProvider<C>,
        push: PushFunction<C, T>,
        preparedSubscribers: MutableList<() -> Unit>
    ) {
        assert(lock.isHeldByCurrentThread)
        
        value = PushSource(changedChild, push)
        prepareSubscribers(preparedSubscribers)
        
        activeParents?.forEach { it.onChildChanged(changedChild, preparedSubscribers) }
        activeChildren?.forEach {
            if (it != changedChild)
                it.onParentChanged(this, preparedSubscribers)
        }
        weakActiveChildren?.forEach {
            if (it != changedChild)
                it.onParentChanged(this, preparedSubscribers)
        }
    }
    
    private fun prepareSubscribers(preparedSubscribers: MutableList<() -> Unit>) {
        assert(lock.isHeldByCurrentThread)
        
        observers?.let { preparedSubscribers += it }
        weakObservers?.forEach { (owner, actions) ->
            for (action in actions) {
                preparedSubscribers += { action(owner) }
            }
        }
        
        if (subscribers.isNotNullOrEmpty()
            || weakSubscribers.isNotNullOrEmpty()
            || immediateSubscribers.isNotNullOrEmpty()
            || weakImmediateSubscribers.isNotNullOrEmpty()
        ) {
            val value = get()
            
            subscribers?.forEach { subscriber ->
                preparedSubscribers += { subscriber(value) }
            }
            weakSubscribers?.forEach { (owner, actions) ->
                for (action in actions) {
                    preparedSubscribers += { action(owner, value) }
                }
            }
            
            immediateSubscribers?.forEach { subscriber ->
                subscriber(value)
            }
            weakImmediateSubscribers?.forEach { (owner, actions) ->
                for (action in actions) {
                    action(owner, value)
                }
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
    
    private fun getOrCreateChildSet(active: Boolean, weak: Boolean): MutableSet<AbstractProvider<*>> {
        assert(lock.isHeldByCurrentThread)
        val children: MutableSet<AbstractProvider<*>>
        if (active) {
            if (weak) {
                if (weakActiveChildren == null)
                    weakActiveChildren = weakHashSet(1)
                children = weakActiveChildren!!
            } else {
                if (activeChildren == null)
                    activeChildren = HashSet(1)
                children = activeChildren!!
            }
        } else {
            if (weak) {
                if (weakInactiveChildren == null)
                    weakInactiveChildren = weakHashSet(1)
                children = weakInactiveChildren!!
            } else {
                if (inactiveChildren == null)
                    inactiveChildren = HashSet(1)
                children = inactiveChildren!!
            }
        }
        
        return children
    }
    
    private fun getChildSetOrNull(active: Boolean, weak: Boolean): MutableSet<AbstractProvider<*>>? {
        assert(lock.isHeldByCurrentThread)
        return if (active) {
            if (weak) weakActiveChildren else activeChildren
        } else {
            if (weak) weakInactiveChildren else inactiveChildren
        }
    }
    
    fun addChild(active: Boolean, weak: Boolean, child: AbstractProvider<*>) {
        assert(lock.isHeldByCurrentThread)
        if (child.lock != lock)
            throw IllegalArgumentException("Mismatching locks")
        
        getOrCreateChildSet(active, weak).add(child)
    }
    
    fun addChildren(active: Boolean, weak: Boolean, vararg children: AbstractProvider<*>) {
        assert(lock.isHeldByCurrentThread)
        
        val childSet = getOrCreateChildSet(active, weak)
        for (child in children) {
            if (child.lock != lock)
                throw IllegalArgumentException("Mismatching locks")
            
            childSet.add(child)
        }
    }
    
    fun removeChild(active: Boolean, weak: Boolean, child: AbstractProvider<*>) {
        assert(lock.isHeldByCurrentThread)
        getChildSetOrNull(active, weak)?.remove(child)
    }
    
    fun removeChildren(active: Boolean, weak: Boolean, vararg children: AbstractProvider<*>) {
        assert(lock.isHeldByCurrentThread)
        
        val childSet = getChildSetOrNull(active, weak)
            ?: return
        for (child in children) {
            childSet.remove(child)
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
internal fun AbstractProvider<*>.addAsChildTo(active: Boolean, weak: Boolean, vararg parents: AbstractProvider<*>) {
    for (parent in parents) {
        parent.addChild(active, weak, this)
    }
}

@OptIn(UnstableProviderApi::class)
internal fun changeLocks(lock: ReentrantLock, vararg providers: AbstractProvider<*>) {
    for (provider in providers) {
        provider.changeLock(lock)
    }
}