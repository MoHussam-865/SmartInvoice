package com.android_a865.estimatescalculator.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*


fun String.double(default: Double = 0.0): Double {

    return try {
        this.toDouble()
    } catch (e: Exception) {
        default
    }

}


fun Long.date(format: String = DATE_FORMATS[0]) : String {
    return SimpleDateFormat(format, Locale.getDefault()).format(this)
}
