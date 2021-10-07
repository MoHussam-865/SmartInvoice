package com.android_a865.estimatescalculator.utils

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.MutableStateFlow

val <T> T.exhaustive: T get() = this


inline fun <T> MutableStateFlow<T>.update(fun0: (T) -> T) {
    value = fun0(value)
}

inline fun <T> MutableLiveData<T>.update(fun0: (T?) -> T) {
    value = fun0(value)
}
