package com.nhom1.oxygen.repository.impl

import android.content.Context
import com.nhom1.oxygen.data.service.OxygenService
import com.nhom1.oxygen.repository.HistoryRepository
import com.nhom1.oxygen.utils.constants.SPKeys
import com.nhom1.oxygen.utils.listen
import io.reactivex.rxjava3.core.Completable
import java.time.Instant

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
                    OxygenService.History.AddHistoryRequest(
                        lat = currentLat,
                        lon = currentLon,
                        time = Instant.now().epochSecond,
                        aqi = weather.airQuality.aqi
                    )
                ).listen {
                    completableEmitter.onComplete()
                }
            }
        }
    }
}