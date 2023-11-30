package com.nhom1.oxygen.repository.impl

import com.nhom1.oxygen.data.model.history.OHistory
import com.nhom1.oxygen.data.model.history.OHourlyHistory
import com.nhom1.oxygen.data.service.OxygenService
import com.nhom1.oxygen.repository.HistoryRepository
import com.nhom1.oxygen.utils.getHour
import com.nhom1.oxygen.utils.now
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class HistoryRepositoryImpl(
    private val service: OxygenService,
) : HistoryRepository {
    override fun addLocationHistory(latitude: Double, longitude: Double, aqi: Int): Completable {
        return service.history.addHistory(
            OHourlyHistory(
                latitude = latitude,
                longitude = longitude,
                time = now(),
                aqi = aqi
            )
        )
    }

    override fun getTodayHistory(): Single<OHistory> {
        return service.history.getHistoryToday().map {
            standardizeHistory(
                OHistory(
                    time = now(),
                    history = it
                )
            )
        }
    }

    override fun get7dHistory(): Single<List<OHistory>> {
        return service.history.getHistory7d().map {
            it.sortedByDescending { e -> e.time }.map { history ->
                standardizeHistory(history)
            }
        }
    }

    private fun standardizeHistory(history: OHistory): OHistory {
        val hourlyHistory = history.history.filterNotNull().sortedBy { e -> e.time }
        val newHourlyHistory = mutableListOf<OHourlyHistory?>()
        var j = 0
        for (i in 0..23) {
            if (j >= hourlyHistory.size) break
            if (getHour(hourlyHistory[j].time) == i) {
                newHourlyHistory.add(hourlyHistory[j])
                j++
            } else if (getHour(hourlyHistory[j].time) > i) {
                newHourlyHistory.add(null)
            } else {
                newHourlyHistory.add(null)
                j++
            }
        }
        return OHistory(
            time = history.time,
            history = newHourlyHistory
        )
    }
}