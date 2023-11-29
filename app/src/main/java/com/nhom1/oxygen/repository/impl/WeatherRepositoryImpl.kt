package com.nhom1.oxygen.repository.impl

import com.nhom1.oxygen.data.model.location.OLocation
import com.nhom1.oxygen.data.model.weather.OWeather
import com.nhom1.oxygen.data.service.OxygenService
import com.nhom1.oxygen.repository.WeatherRepository
import com.nhom1.oxygen.utils.now
import io.reactivex.rxjava3.core.Single

class WeatherRepositoryImpl(private val service: OxygenService) : WeatherRepository {
    data class CachedWeatherInfo(
        val time: Long,
        val location: OLocation,
        val weather: OWeather? = null,
        val weathers: List<OWeather>? = null
    )

    private lateinit var cachedWeatherCurrent: CachedWeatherInfo
    private lateinit var cachedWeather24h: CachedWeatherInfo
    private lateinit var cachedWeather7d: CachedWeatherInfo

    override fun getCurrentWeatherInfo(location: OLocation): Single<OWeather> {
        return when {
            !this::cachedWeatherCurrent.isInitialized
                    || (now() - cachedWeatherCurrent.time > 900)
                    || (location.distance(cachedWeatherCurrent.location) > 100) -> {
                service.weather.getCurrent(location.latitude, location.longitude).map {
                    cachedWeatherCurrent = CachedWeatherInfo(
                        time = now(),
                        location = location,
                        weather = it
                    )
                    it
                }
            }

            else -> {
                return Single.create {
                    it.onSuccess(cachedWeatherCurrent.weather!!)
                }
            }
        }
    }

    override fun getWeatherInfoIn24h(location: OLocation): Single<List<OWeather>> {
        return when {
            !this::cachedWeather24h.isInitialized
                    || (now() - cachedWeather24h.time > 900)
                    || (location.distance(cachedWeather24h.location) > 100) -> {
                service.weather.get24h(location.latitude, location.longitude).map {
                    cachedWeather24h = CachedWeatherInfo(
                        time = now(),
                        location = location,
                        weathers = it
                    )
                    it
                }
            }

            else -> {
                return Single.create {
                    it.onSuccess(cachedWeather24h.weathers!!)
                }
            }
        }
    }

    override fun getWeatherInfoIn7d(location: OLocation): Single<List<OWeather>> {
        return when {
            !this::cachedWeather7d.isInitialized
                    || (now() - cachedWeather7d.time > 900)
                    || (location.distance(cachedWeather7d.location) > 100) -> {
                service.weather.get7d(location.latitude, location.longitude).map {
                    cachedWeather7d = CachedWeatherInfo(
                        time = now(),
                        location = location,
                        weathers = it
                    )
                    it
                }
            }

            else -> {
                return Single.create {
                    it.onSuccess(cachedWeather7d.weathers!!)
                }
            }
        }
    }
}