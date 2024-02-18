package xyz.xenondevs.commons.provider.mutable

fun <T> mutableProvider(initialValue: T): MutableProvider<T> =
    MutableProviderWrapper(initialValue)

private class MutableProviderWrapper<T>(private val initialValue: T) : MutableProvider<T>() {
    override fun loadValue(): T = initialValue
}