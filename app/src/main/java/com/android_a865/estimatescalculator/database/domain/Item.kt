package com.android_a865.estimatescalculator.database.domain

import android.os.Parcelable
import com.android_a865.estimatescalculator.utils.Path
import com.android_a865.estimatescalculator.utils.PathUtil
import com.android_a865.estimatescalculator.utils.Selectable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Item(
        var id: Int = 0,
        override var name: String,
        override var path: Path,
        var value: Double = 0.0,
        var value2: Double = 0.0,
        var qty: Double = 0.0,
        override var isFolder: Boolean = false,

        override var isSelected: Boolean = false
): Parcelable, PathUtil, Selectable<Item> {
        override fun changeTo(b: Boolean) = copy(isSelected = b)
}