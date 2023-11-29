package com.nhom1.oxygen.repository.impl

import com.nhom1.oxygen.data.service.OxygenService
import com.nhom1.oxygen.repository.PathologyRepository
import io.reactivex.rxjava3.core.Single

class PathologyRepositoryImpl(
    private val service: OxygenService
) : PathologyRepository{
    override fun analyzePathology(input: String): Single<List<String>> {
        return service.analyzer.analyzeDiseases(input).map { it.analysis }
    }
}