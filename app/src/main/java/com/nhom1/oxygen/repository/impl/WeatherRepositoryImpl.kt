package com.nhom1.oxygen.repository.impl

import com.nhom1.oxygen.data.model.location.OLocation
import com.nhom1.oxygen.data.model.weather.OWeather
import com.nhom1.oxygen.data.service.OxygenService
import com.nhom1.oxygen.repository.WeatherRepository
import io.reactivex.rxjava3.core.Single

class WeatherRepositoryImpl(private val service: OxygenService) : WeatherRepository {
    override fun getCurrentWeatherInfo(location: OLocation): Single<OWeather> {
        return service.weather.getCurrent(location.latitude, location.longitude)
    }

    override fun getWeatherInfoIn24h(location: OLocation): Single<List<OWeather>> {
        return service.weather.get24h(location.latitude, location.longitude)
    }

    override fun getWeatherInfoIn7d(location: OLocation): Single<List<OWeather>> {
        return service.weather.get7d(location.latitude, location.longitude)
    }
}