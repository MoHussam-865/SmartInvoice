package com.android_a865.estimatescalculator.core.data.local.entity

import android.os.Parcelable
import com.android_a865.estimatescalculator.core.utils.Path
import com.android_a865.estimatescalculator.core.utils.PathUtil
import com.android_a865.estimatescalculator.core.utils.Selectable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Item(
    var id: Int = 0,
    override var name: String,
    override var path: Path,
    var price: Double = 0.0,

    override var isFolder: Boolean = false,
    override var isSelected: Boolean = false
) : Parcelable, PathUtil, Selectable<Item> {

    override fun copy(b: Boolean) = copy(isSelected = b)
    val nameAsFolder get() = path.open(name).path
}