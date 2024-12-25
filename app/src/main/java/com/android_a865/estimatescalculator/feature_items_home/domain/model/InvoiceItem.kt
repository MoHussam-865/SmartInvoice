package com.android_a865.estimatescalculator.feature_items_home.domain.model

import android.os.Parcelable
import com.android_a865.estimatescalculator.utils.toFormattedString
import kotlinx.parcelize.Parcelize
import kotlin.math.abs

@Parcelize
data class InvoiceItem(
    val id: Int,
    val name: String = "",
    val fullName: String,
    val price: Double = 0.0,
    var qty: Double = 0.0,
    var discount: Double = 0.0,
    val isFolder: Boolean = false
) : Parcelable {
    val total get() = finalPrice * qty
    val finalPrice get() = price * (1 - discount/100)

    val discountDetail get(): String {
        val sign = if (discount > 0.0) "-" else "+"
        return if (discount != 0.0) {
            "(${price.toFormattedString()}  $sign ${abs(discount).toFormattedString()}%)"
        } else ""
    }
    val details get(): String {
        if (discount != 0.0) {
            val sign = if (discount > 0.0) "-" else "+"
            return "$fullName  $discountDetail"
        }
        return fullName
    }

}