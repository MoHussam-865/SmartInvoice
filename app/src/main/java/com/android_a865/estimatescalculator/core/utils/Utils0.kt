package com.android_a865.estimatescalculator.core.utils

import android.content.Intent
import android.os.Build
import android.os.Parcelable
import java.text.SimpleDateFormat
import java.util.Locale


fun Long.date(format: String = DATE_FORMATS[0]) : String {
    return SimpleDateFormat(format, Locale.getDefault()).format(this)
}

inline fun <reified T : Parcelable?> getObject(
    intent: Intent, key: String
): T? {

    return if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU) {
        intent.getParcelableExtra(key, T::class.java)
    } else {
        intent.getParcelableExtra<T>(key)
    }

}


