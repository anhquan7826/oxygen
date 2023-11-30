package com.nhom1.oxygen.repository

import com.nhom1.oxygen.data.model.location.OLocation
import com.nhom1.oxygen.data.model.weather.OWeather
import io.reactivex.rxjava3.core.Single

interface WeatherRepository {
    fun getCurrentWeatherInfo(latitude: Double, longitude: Double): Single<OWeather>

    fun getWeatherInfoIn24h(latitude: Double, longitude: Double): Single<List<OWeather>>

    fun getWeatherInfoIn7d(latitude: Double, longitude: Double): Single<List<OWeather>>
}