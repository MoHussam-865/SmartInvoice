package com.android_a865.estimatescalculator.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

fun execute() {

    System.currentTimeMillis().apply {

        DATE_FORMATS.forEach {
            Log.d("Test Some Utils", this.date(it))
        }

    }

}

val String.double get(): Double {

    return try {
        this.toDouble()
    } catch (e: Exception) {
        0.0
    }

}


fun Long.date(format: String = DATE_FORMATS[0]) : String {
    return SimpleDateFormat(format, Locale.getDefault()).format(System.currentTimeMillis())
}
