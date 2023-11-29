package com.nhom1.oxygen.repository.impl

import com.nhom1.oxygen.data.model.location.OLocation
import com.nhom1.oxygen.data.model.weather.OWeather
import com.nhom1.oxygen.data.service.OxygenService
import com.nhom1.oxygen.repository.WeatherRepository
import com.nhom1.oxygen.utils.debugLog
import com.nhom1.oxygen.utils.now
import io.reactivex.rxjava3.core.Single

class WeatherRepositoryImpl(private val service: OxygenService) : WeatherRepository {
    data class CachedWeatherInfo(
        val time: Long,
        val location: OLocation,
        val weather: OWeather? = null,
        val weathers: List<OWeather>? = null
    )

    private val cacheInterval = 900
    private val cacheDistance = 100

    private lateinit var cachedWeatherCurrent: CachedWeatherInfo
    private lateinit var cachedWeather24h: CachedWeatherInfo
    private lateinit var cachedWeather7d: CachedWeatherInfo

    override fun getCurrentWeatherInfo(location: OLocation): Single<OWeather> {
        return when {
            !this::cachedWeatherCurrent.isInitialized
                    || (now() - cachedWeatherCurrent.time > cacheInterval)
                    || (location.distance(cachedWeatherCurrent.location) > cacheDistance) -> {
                debugLog("${this::class.simpleName}: getCurrentWeatherInfo: new!")
                service.weather.getCurrent(location.latitude, location.longitude).doOnSuccess {
                    cachedWeatherCurrent = CachedWeatherInfo(
                        time = now(),
                        location = location,
                        weather = it
                    )
                }
            }

            else -> {
                debugLog("${this::class.simpleName}: getCurrentWeatherInfo: cached!")
                return Single.create {
                    it.onSuccess(cachedWeatherCurrent.weather!!)
                }
            }
        }
    }

    override fun getWeatherInfoIn24h(location: OLocation): Single<List<OWeather>> {
        return when {
            !this::cachedWeather24h.isInitialized
                    || (now() - cachedWeather24h.time > cacheInterval)
                    || (location.distance(cachedWeather24h.location) > cacheDistance) -> {
                debugLog("${this::class.simpleName}: get24hWeatherInfo: new!")
                service.weather.get24h(location.latitude, location.longitude).doOnSuccess {
                    cachedWeather24h = CachedWeatherInfo(
                        time = now(),
                        location = location,
                        weathers = it
                    )
                }
            }

            else -> {
                debugLog("${this::class.simpleName}: get24WeatherInfo: cached!")
                return Single.create {
                    it.onSuccess(cachedWeather24h.weathers!!)
                }
            }
        }
    }

    override fun getWeatherInfoIn7d(location: OLocation): Single<List<OWeather>> {
        return when {
            !this::cachedWeather7d.isInitialized
                    || (now() - cachedWeather7d.time > cacheInterval)
                    || (location.distance(cachedWeather7d.location) > cacheDistance) -> {
                debugLog("${this::class.simpleName}: get7dWeatherInfo: new!")
                service.weather.get7d(location.latitude, location.longitude).doOnSuccess {
                    cachedWeather7d = CachedWeatherInfo(
                        time = now(),
                        location = location,
                        weathers = it
                    )
                }
            }

            else -> {
                debugLog("${this::class.simpleName}: get7dWeatherInfo: cached!")
                return Single.create {
                    it.onSuccess(cachedWeather7d.weathers!!)
                }
            }
        }
    }
}