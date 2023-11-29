package com.nhom1.oxygen.data.model.weather

data class OAirQuality(
    val co: Double,
    val no2: Double,
    val o3: Double,
    val so2: Double,
    val pm2_5: Double,
    val pm10: Double,
    val aqi: Int
)