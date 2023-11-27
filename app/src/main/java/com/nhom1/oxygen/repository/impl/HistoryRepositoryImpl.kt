package com.nhom1.oxygen.repository.impl

import android.content.Context
import com.nhom1.oxygen.data.model.history.OHistory
import com.nhom1.oxygen.data.model.history.OHourlyHistory
import com.nhom1.oxygen.data.service.OxygenService
import com.nhom1.oxygen.repository.HistoryRepository
import com.nhom1.oxygen.utils.constants.SPKeys
import com.nhom1.oxygen.utils.listen
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

class HistoryRepositoryImpl(
    private val context: Context,
    private val service: OxygenService
) : HistoryRepository {
    private val sharedPreferences =
        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    override fun addLocationHistory(): Completable {
        return Completable.create { completableEmitter ->
            val currentLat = sharedPreferences.getFloat(SPKeys.CURRENT_LAT, 0F).toDouble()
            val currentLon = sharedPreferences.getFloat(SPKeys.CURRENT_LON, 0F).toDouble()
            service.weather.getCurrent(currentLat, currentLon).listen { weather ->
                service.history.addHistory(
                    OHourlyHistory(
                        latitude = currentLat,
                        longitude = currentLon,
                        time = Instant.now().epochSecond,
                        aqi = weather.airQuality.aqi
                    )
                ).listen {
                    completableEmitter.onComplete()
                }
            }
        }
    }

    override fun getTodayHistory(): Single<OHistory> {
        return service.history.getHistoryToday().map {
            OHistory(
                time = LocalDateTime.now().toEpochSecond(ZoneOffset.of("GMT+7")),
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