package xyz.xenondevs.commons.guava

import com.google.common.collect.Table

inline fun <R, C, V> Table<R, C, V>.replaceAll(replacer: (R, C, V) -> V) {
    val rows = rowKeySet()
    val columns = columnKeySet()
    for (row in rows) {
        for (column in columns) {
            val value = get(row, column)
            if (value != null) {
                put(row, column, replacer(row, column, value))
            }
        }
    }
}

inline fun <R, C, V> Table<R, C, V>.forEach(action: (R, C, V) -> Unit) {
    for (cell in cellSet()) {
        action(cell.rowKey, cell.columnKey, cell.value)
    }
}

operator fun <R, C, V> Table<R, C, V>.set(row: R, column: C, value: V) = put(row, column, value)
operator fun <R, C, V> Table<R, C, V>.iterator(): Iterator<Table.Cell<R, C, V>> = cellSet().iterator()
operator fun <R, C, V> Table.Cell<R, C, V>.component1(): R = rowKey
operator fun <R, C, V> Table.Cell<R, C, V>.component2(): C = columnKey
operator fun <R, C, V> Table.Cell<R, C, V>.component3(): V = value