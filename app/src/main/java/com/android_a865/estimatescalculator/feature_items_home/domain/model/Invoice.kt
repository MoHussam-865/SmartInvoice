package com.android_a865.estimatescalculator.feature_items_home.domain.model

import android.os.Parcelable
import com.android_a865.estimatescalculator.core.data.local.entity.Client
import com.android_a865.estimatescalculator.core.enu.InvoiceTypes
import kotlinx.parcelize.Parcelize
import java.util.Calendar

@Parcelize
data class Invoice(
    val id: Int = 0,
    val type: InvoiceTypes,
    val date: Long = Calendar.getInstance().timeInMillis,
    val client: Client? = null,
    val items: List<InvoiceItem>,
) : Parcelable {
    val total get() = items.sumOf { it.total }
}
