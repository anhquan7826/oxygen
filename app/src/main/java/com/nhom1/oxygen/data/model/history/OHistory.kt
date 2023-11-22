package com.nhom1.oxygen.data.model.history

import com.google.gson.annotations.SerializedName

data class OHistory(
    val id: Int,
    @SerializedName("lat") val latitude: Double,
    @SerializedName("lon") val longitude: Double,
    val aqi: Double,
    val time: Long,
)
