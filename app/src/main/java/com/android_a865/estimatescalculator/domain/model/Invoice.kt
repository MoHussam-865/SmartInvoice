package com.android_a865.estimatescalculator.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Invoice(
        val date: Date,
        val items: List<InvoiceItem>
): Parcelable
