package com.android_a865.estimatescalculator.utils

import com.android_a865.estimatescalculator.feature_main.domain.model.InvoiceItem


/*
fun <T> MutableList<T>.addUnique(
    data: T,
    onFound: (T) -> T = { it },
    equals: (T) -> Boolean
): MutableList<T> {

    var found = false

    val items = map {
        if (equals( it )) {
            found = true
            onFound(it)
        } else it
    }.toMutableList()

    if (!found) {
        items.add(data)
    }

    return items
}
*/


fun <T> MutableList<T>.addUnique(
    item: T,
    onFound: (T) -> T = { it },
    equals: (T) -> Boolean
): List<T> {
    var found = false
    val items = map {
        if (equals(it)) {
            found = true
            onFound(it)
        } else it
    }.toMutableList()

    if (!found) items.add(item)
    return items.toList()
}
