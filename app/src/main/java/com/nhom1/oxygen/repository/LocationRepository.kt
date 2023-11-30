package com.nhom1.oxygen.repository

import com.nhom1.oxygen.data.model.divisions.ODistrict
import com.nhom1.oxygen.data.model.divisions.OProvince
import com.nhom1.oxygen.data.model.divisions.OWard
import com.nhom1.oxygen.data.model.location.OLocation
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface LocationRepository {
    fun getCurrentLocation(): Single<OLocation>

    fun findLocation(query: String): Single<List<OLocation>>

    fun getProvinces(): Single<List<OProvince>>

    fun getDistricts(provinceId: String): Single<List<ODistrict>>

    fun getWards(districtId: String): Single<List<OWard>>

    fun getSearchedLocation(): Single<List<OLocation>>

    fun addSearchedLocation(location: OLocation): Completable
}