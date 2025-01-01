package com.android_a865.estimatescalculator.core.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android_a865.estimatescalculator.core.domain.repository.ToSend
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "Clients")
data class Client(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val org: String? = null,
    val title: String? = null,
    val phone1: String? = null,
    val phone2: String? = null,
    val email: String? = null,
    val address: String? = null
): Parcelable, ToSend