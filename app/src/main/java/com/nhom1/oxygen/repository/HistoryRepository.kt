package com.nhom1.oxygen.repository

import com.nhom1.oxygen.data.model.history.OHistory
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface HistoryRepository {
    fun addLocationHistory(): Completable

    fun get7dHistory(): Single<List<OHistory>>
}