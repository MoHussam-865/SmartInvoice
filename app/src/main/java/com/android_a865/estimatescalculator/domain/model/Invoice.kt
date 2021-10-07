package com.android_a865.estimatescalculator.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Invoice(
    val id: Int = 0,
    val date: Long = Calendar.getInstance().timeInMillis,
    val items: List<InvoiceItem>,
) : Parcelable

val Invoice.total get() = items.sumOf { it.total }
