package com.nhom1.oxygen.repository.mock

import com.nhom1.oxygen.data.model.location.OLocation
import com.nhom1.oxygen.data.model.weather.OAirQuality
import com.nhom1.oxygen.data.model.weather.OWeather
import com.nhom1.oxygen.data.model.weather.OWeatherCondition
import com.nhom1.oxygen.repository.WeatherRepository
import io.reactivex.rxjava3.core.Single

class WeatherRepositoryMock : WeatherRepository {
    private fun sampleWeather(time: Int) = OWeather(
        time = 1700302339L + time,
        tempC = 28.0,
        tempF = 65.0,
        humidity = 50.0,
        windDegree = 0.0,
        windMPH = 0.0,
        windKPH = 0.0,
        windDir = "NE",
        condition = OWeatherCondition(
            text = "Partly cloudy",
            icon = "//cdn.weatherapi.com/weather/64x64/night/116.png",
            code = 1003
        ),
        airQuality = OAirQuality(
            co = 547.4, no2 = 7.8, o3 = 95.8, so2 = 30.5, pm25 = 73.9, pm10 = 76.8, aqi = 59
        )
    )

    override fun getCurrentWeatherInfo(location: OLocation): Single<OWeather> {
        return Single.create {
            it.onSuccess(
                sampleWeather(0)
            )
        }
    }

    override fun getWeatherInfoIn24h(location: OLocation): Single<List<OWeather>> {
        return Single.create {
            it.onSuccess(List(24) { index ->
                sampleWeather(3600 * index)
            })
        }
    }

    override fun getWeatherInfoIn7d(location: OLocation): Single<List<OWeather>> {
        return Single.create {
            it.onSuccess(List(7) { index ->
                sampleWeather(3600 * 24 * index)
            })
        }
    }
}