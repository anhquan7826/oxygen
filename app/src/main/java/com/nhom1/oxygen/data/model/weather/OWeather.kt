package com.nhom1.oxygen.data.model.weather

import com.google.gson.annotations.SerializedName

data class OWeather(
    @SerializedName("last_updated_epoch") val lastUpdatedEpoch: Long,
    @SerializedName("last_updated") val lastUpdated: String,
    @SerializedName("temp_c") val tempC: Double,
    @SerializedName("temp_f") val tempF: Double,
    @SerializedName("is_day") val isDay: Int,
    val condition: OWeatherCondition,
    @SerializedName("wind_mph") val windMPH: Double,
    @SerializedName("wind_kph") val windKPH: Double,
    @SerializedName("wind_degree") val windDegree: Double,
    @SerializedName("wind_dir") val windDir: String,
    @SerializedName("pressure_mb") val pressureMB: Double,
    @SerializedName("pressure_in") val pressureIN: Double,
    val humidity: Double,
    val cloud: Double,
    @SerializedName("feelslike_c") val feelsLikeC: Double,
    @SerializedName("feelslike_f") val feelsLikeF: Double,
    @SerializedName("vision_km") val visionKM: Double,
    @SerializedName("vision_miles") val visionMiles: Double,
    val uv: Double,
    @SerializedName("gust_mph") val gustMPH: Double,
    @SerializedName("gust_kph") val gustKPH: Double,
    @SerializedName("air_quality") val airQuality: OAirQuality
)