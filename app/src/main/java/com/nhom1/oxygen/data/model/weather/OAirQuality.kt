package com.nhom1.oxygen.data.model.weather

import com.google.gson.annotations.SerializedName

data class OAirQuality(
    val co: Double,
    val no2: Double,
    val o3: Double,
    val so2: Double,
    @SerializedName("pm2_5") val pm25: Double,
    val pm10: Double,
)