package com.nhom1.oxygen.repository

import io.reactivex.rxjava3.core.Single

interface PathologyRepository {
    fun analyzePathology(input: String): Single<List<String>>
}