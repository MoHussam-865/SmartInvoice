package com.android_a865.estimatescalculator.feature_main.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Invoice(
    val id: Int = 0,
    val date: Long = Calendar.getInstance().timeInMillis,
    val items: List<InvoiceItem>,
) : Parcelable {
    val total get() = items.sumOf { it.total }
}
