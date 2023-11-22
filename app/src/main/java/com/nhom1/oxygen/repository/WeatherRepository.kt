package com.nhom1.oxygen.repository

import com.nhom1.oxygen.data.model.location.OLocation
import com.nhom1.oxygen.data.model.weather.OWeather
import io.reactivex.rxjava3.core.Single

interface WeatherRepository {
    fun getCurrentWeatherInfo(location: OLocation): Single<OWeather>

    fun getWeatherInfoIn24h(location: OLocation): Single<List<OWeather>>

    fun getWeatherInfoIn7d(location: OLocation): Single<List<OWeather>>
}