package com.nhom1.oxygen.repository.mock

import com.nhom1.oxygen.data.model.history.OHistory
import com.nhom1.oxygen.data.model.history.OHourlyHistory
import com.nhom1.oxygen.repository.HistoryRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class HistoryRepositoryMock : HistoryRepository {
    override fun addLocationHistory(latitude: Double, longitude: Double, aqi: Int): Completable {
        return Completable.create {
            it.onComplete()
        }
    }

    override fun getTodayHistory(): Single<OHistory> {
        return get7dHistory().map {
            it.maxByOrNull { e -> e.time }!!
        }
    }

    override fun get7dHistory(): Single<List<OHistory>> {
        return Single.create {
            it.onSuccess(List(7) { i ->
                OHistory(
                    time = 1700672400L + i * 86400,
                    history = MutableList(24) { j ->
                        OHourlyHistory(
                            latitude = 21.03119444 + Random.nextDouble(-1.0, 1.0),
                            longitude = 105.76388889 + Random.nextDouble(-1.0, 1.0),
                            aqi = Random.nextInt(501),
                            time = 1700672400L + i * 86400 + j * 3600
                        )
                    }.apply { removeAt(3); removeAt(12); removeAt(13) }
                )
            }.sortedByDescending { e -> e.time })
        }.delay(1000, TimeUnit.MILLISECONDS)
    }
}