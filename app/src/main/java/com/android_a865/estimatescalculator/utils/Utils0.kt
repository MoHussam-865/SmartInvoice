package com.android_a865.estimatescalculator.utils

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


fun String.double(): Double {

    return try {
        toDouble()
    } catch (e: Exception) {
        return 0.0
    }

}


fun Long.date(format: String = DATE_FORMATS[0]) : String {
    return SimpleDateFormat(format, Locale.getDefault()).format(this)
}


