package com.nhom1.oxygen.repository.impl

import android.content.Context
import com.nhom1.oxygen.data.model.history.OHistory
import com.nhom1.oxygen.data.model.history.OHourlyHistory
import com.nhom1.oxygen.data.model.location.OLocation
import com.nhom1.oxygen.data.service.OxygenService
import com.nhom1.oxygen.repository.HistoryRepository
import com.nhom1.oxygen.utils.constants.SPKeys
import com.nhom1.oxygen.utils.fromJson
import com.nhom1.oxygen.utils.listen
import com.nhom1.oxygen.utils.now
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class HistoryRepositoryImpl(
    private val context: Context,
    private val service: OxygenService
) : HistoryRepository {
    private val sharedPreferences =
        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    override fun addLocationHistory(): Completable {
        return Completable.create { completableEmitter ->
            val location = fromJson<OLocation>(sharedPreferences.getString(SPKeys.CURRENT_LOCATION, "")!!, OLocation::class.java)!!
            service.weather.getCurrent(location.latitude, location.longitude).listen { weather ->
                service.history.addHistory(
                    OHourlyHistory(
                        latitude = location.latitude,
                        longitude = location.longitude,
                        time = now(),
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