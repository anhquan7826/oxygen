package com.nhom1.oxygen.data.model.divisions

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity
data class OProvince(
    @SerializedName("codename") val id: String,
    val name: String,
)