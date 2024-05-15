package xyz.xenondevs.commons.provider.mutable

/**
 * Creates a new [MutableProvider] with the given [initialValue].
 */
fun <T> mutableProvider(initialValue: T): MutableProvider<T> =
    MutableProviderWrapper(initialValue)

private class MutableProviderWrapper<T>(private val initialValue: T) : MutableProvider<T>() {
    override fun loadValue(): T = initialValue
}