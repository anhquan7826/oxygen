package com.nhom1.oxygen.repository.impl

import android.annotation.SuppressLint
import android.content.Context
import com.nhom1.oxygen.data.database.OxygenDatabase
import com.nhom1.oxygen.data.model.divisions.ODistrict
import com.nhom1.oxygen.data.model.divisions.OProvince
import com.nhom1.oxygen.data.model.divisions.OWard
import com.nhom1.oxygen.data.model.location.OLocation
import com.nhom1.oxygen.data.service.OxygenService
import com.nhom1.oxygen.repository.LocationRepository
import com.nhom1.oxygen.utils.constants.SPKeys
import com.nhom1.oxygen.utils.fromJson
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class LocationRepositoryImpl(
    private val context: Context,
    private val service: OxygenService,
    private val database: OxygenDatabase
) : LocationRepository {
    private val sharedPreferences =
        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    @SuppressLint("MissingPermission")
    override fun getCurrentLocation(): Single<OLocation> {
        return Single.create { emitter ->
            emitter.onSuccess(
                fromJson(
                    sharedPreferences.getString(SPKeys.CURRENT_LOCATION, "")!!,
                    OLocation::class.java
                )!!
            )
        }
    }

    override fun getLocationFromCoordinate(latitude: Double, longitude: Double): Single<OLocation> {
        return service.geocoding.getLocation(latitude, longitude)
    }

    override fun findLocation(query: String): Single<List<OLocation>> {
        return service.geocoding.searchLocation(query)
    }

    override fun getProvinces(): Single<List<OProvince>> {
        return service.division.getProvinces()
    }

    override fun getDistricts(provinceId: String): Single<List<ODistrict>> {
        return service.division.getDistricts(provinceId)
    }

    override fun getWards(districtId: String): Single<List<OWard>> {
        return service.division.getWards(districtId)
    }

    override fun getSearchedLocation(): Single<List<OLocation>> {
        return database.searchedLocationDao().getSearchedLocation()
    }

    override fun addSearchedLocation(location: OLocation): Completable {
        return database.searchedLocationDao().addSearchedLocation(location)
    }
}