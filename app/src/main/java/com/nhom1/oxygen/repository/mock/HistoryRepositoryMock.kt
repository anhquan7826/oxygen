package com.nhom1.oxygen.repository.mock

import com.nhom1.oxygen.repository.HistoryRepository
import io.reactivex.rxjava3.core.Completable

class HistoryRepositoryMock : HistoryRepository {
    override fun addLocationHistory(): Completable {
        return Completable.create {
            it.onComplete()
        }
    }
}