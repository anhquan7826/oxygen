package com.nhom1.oxygen.data.model.weather

import com.google.gson.annotations.SerializedName

data class OWeather(
    val time: Long,
    @SerializedName("temp_c") val tempC: Double,
    @SerializedName("temp_f") val tempF: Double,
    val condition: OWeatherCondition,
    val chanceOfRain: Int? = null,
    val precipitationIn: Double? = null,
    val precipitationMm: Double? = null,
    @SerializedName("wind_mph") val windMPH: Double,
    @SerializedName("wind_kph") val windKPH: Double,
    @SerializedName("wind_degree") val windDegree: Double,
    @SerializedName("wind_dir") val windDir: String,
    val humidity: Double,
    @SerializedName("air_quality") val airQuality: OAirQuality,
)