package com.nhom1.oxygen.data.model.weather

import com.google.gson.annotations.SerializedName

data class OForecast(
    val timeEpoch: Long,
    val time: String,
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
    @SerializedName("precip_mm") val precipitationMM: Double,
    @SerializedName("precip_in") val precipitationIN: Double,
    val humidity: Double,
    val cloud: Double,
    @SerializedName("feelslike_c") val feelsLikeC: Double,
    @SerializedName("feelslike_f") val feelsLikeF: Double,
    @SerializedName("chance_of_rain") val chanceOfRain: Double,
    @SerializedName("air_quality") val airQuality: OAirQuality
)
