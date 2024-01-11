package com.android_a865.estimatescalculator.feature_main.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class InvoiceItem(
    val id: Int,
    val name: String = "",
    val price: Double = 0.0,
    var qty: Double = 0.0,
    var discount: Double = 0.0,
    val isFolder: Boolean = false
) : Parcelable {
    val total get() = price * qty * (1 - discount/100)
}