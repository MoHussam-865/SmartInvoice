package com.android_a865.estimatescalculator.utils



fun <T> MutableList<T>.addUnique(
    data: T,
    onFound: (T) -> T = { it },
    equals: (T) -> Boolean ) {

    var found = false

    map {
        if (equals( it )) {
            found = true
            onFound(it)
        } else it
    }

    if (!found) {
        add(data)
    }

}
