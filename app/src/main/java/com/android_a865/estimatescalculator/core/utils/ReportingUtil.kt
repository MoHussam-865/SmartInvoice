package com.android_a865.estimatescalculator.core.utils


fun <T> MutableList<T>.addUnique(
    data: T,
    equals: (T) -> Boolean
) {
    var found = false
    forEach {
        if (equals(it)) {
            found = true
        }
    }

    if (!found) add(data)

}
