package com.nhom1.oxygen.repository

import com.nhom1.oxygen.data.model.location.OLocation
import io.reactivex.rxjava3.core.Single

interface LocationRepository {
    fun getCurrentLocation(): Single<OLocation>

    fun findLocation(query: String): Single<List<OLocation>>
}