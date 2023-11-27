package com.nhom1.oxygen.repository.mock

import com.nhom1.oxygen.repository.PathologyRepository
import io.reactivex.rxjava3.core.Single
import java.util.concurrent.TimeUnit

class PathologyRepositoryMock : PathologyRepository {
    override fun analyzePathology(input: String): Single<List<String>> {
        return Single.create<List<String>> {
            it.onSuccess(listOf(
                "Tiểu đường",
                "Viêm gan B",
                "AIDS",
                "Lậu"
            ))
        }.delay(1000, TimeUnit.MILLISECONDS)
    }
}