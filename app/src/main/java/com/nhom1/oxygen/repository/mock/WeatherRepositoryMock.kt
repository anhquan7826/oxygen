package com.nhom1.oxygen.repository.mock

import com.nhom1.oxygen.data.model.location.OLocation
import com.nhom1.oxygen.data.model.weather.OAirQuality
import com.nhom1.oxygen.data.model.weather.OWeather
import com.nhom1.oxygen.data.model.weather.OWeatherCondition
import com.nhom1.oxygen.repository.WeatherRepository
import io.reactivex.rxjava3.core.Single
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class WeatherRepositoryMock : WeatherRepository {
    private fun sampleWeather(time: Int) = OWeather(
        time = 1700586000L + time,
        tempC = 28.0,
        tempF = 65.0,
        humidity = 50.0,
        windDegree = 0.0,
        windMPH = 0.0,
        windKPH = 0.0,
        windDir = "NE",
        chanceOfRain = 60,
        condition = OWeatherCondition(
            text = "Partly cloudy",
            icon = "https://cdn.weatherapi.com/weather/64x64/day/176.png",
            code = 1003
        ),
        airQuality = OAirQuality(
            co = 547.4, no2 = 7.8, o3 = 95.8, so2 = 30.5, pm2_5 = 73.9, pm10 = 76.8, aqi = Random.nextInt(501)
        )
    )

    override fun getCurrentWeatherInfo(location: OLocation): Single<OWeather> {
        return Single.create {
            it.onSuccess(
                sampleWeather(0)
            )
        }.delay(1000, TimeUnit.MILLISECONDS)
    }

    override fun getWeatherInfoIn24h(location: OLocation): Single<List<OWeather>> {
        return Single.create {
            it.onSuccess(List(24) { index ->
                sampleWeather(3600 * index)
            })
        }.delay(1000, TimeUnit.MILLISECONDS)
    }

    override fun getWeatherInfoIn7d(location: OLocation): Single<List<OWeather>> {
        return Single.create {
            it.onSuccess(List(7) { index ->
                sampleWeather(3600 * 24 * index)
            })
        }.delay(1000, TimeUnit.MILLISECONDS)
    }
}