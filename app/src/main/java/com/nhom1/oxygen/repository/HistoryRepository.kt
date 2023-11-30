package com.nhom1.oxygen.repository

import com.nhom1.oxygen.data.model.history.OHistory
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface HistoryRepository {
    fun addLocationHistory(latitude: Double, longitude: Double, aqi: Int): Completable

    fun getTodayHistory(): Single<OHistory>

    fun get7dHistory(): Single<List<OHistory>>
}