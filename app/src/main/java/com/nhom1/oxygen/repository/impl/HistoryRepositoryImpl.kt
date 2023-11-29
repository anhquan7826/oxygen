package com.nhom1.oxygen.repository.impl

import com.nhom1.oxygen.data.model.history.OHistory
import com.nhom1.oxygen.data.model.history.OHourlyHistory
import com.nhom1.oxygen.data.service.OxygenService
import com.nhom1.oxygen.repository.HistoryRepository
import com.nhom1.oxygen.repository.LocationRepository
import com.nhom1.oxygen.repository.WeatherRepository
import com.nhom1.oxygen.utils.listen
import com.nhom1.oxygen.utils.now
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class HistoryRepositoryImpl(
    private val service: OxygenService,
    private val locationRepository: LocationRepository,
    private val weatherRepository: WeatherRepository
) : HistoryRepository {
    override fun addLocationHistory(): Completable {
        return Completable.create { emitter ->
            locationRepository.getCurrentLocation().listen(
                onError = { emitter.onError(it) }
            ) { location ->
                weatherRepository.getCurrentWeatherInfo(location).listen(
                    onError = { emitter.onError(it) }
                ) { weather ->
                    service.history.addHistory(
                        OHourlyHistory(
                            latitude = location.latitude,
                            longitude = location.longitude,
                            time = now(),
                            aqi = weather.airQuality.aqi
                        )
                    ).listen(
                        onError = { emitter.onError(it) }
                    ) {
                        emitter.onComplete()
                    }
                }
            }

        }
    }

    override fun getTodayHistory(): Single<OHistory> {
        return service.history.getHistoryToday().map {
            OHistory(
                time = now(),
                history = it
            )
        }
    }

    override fun get7dHistory(): Single<List<OHistory>> {
        return service.history.getHistory7d().map {
            it.sortedByDescending { e -> e.time }
        }
    }
}