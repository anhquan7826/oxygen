package com.nhom1.oxygen.data.model.history

import com.google.gson.annotations.SerializedName

data class OHourlyHistory(
    @SerializedName("lat") val latitude: Double,
    @SerializedName("lon") val longitude: Double,
    val aqi: Int,
    val time: Long,
)
