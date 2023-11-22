package com.nhom1.oxygen.repository

import io.reactivex.rxjava3.core.Completable

interface HistoryRepository {
    fun addLocationHistory(): Completable
}