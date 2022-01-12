package com.android_a865.estimatescalculator.utils

import android.util.Log
import com.android_a865.estimatescalculator.feature_main.domain.model.InvoiceItem


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
