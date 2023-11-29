package com.nhom1.oxygen.repository.impl

import android.content.Context
import com.nhom1.oxygen.data.database.OxygenDatabase
import com.nhom1.oxygen.data.model.divisions.ODistrict
import com.nhom1.oxygen.data.model.divisions.OProvince
import com.nhom1.oxygen.data.model.divisions.OWard
import com.nhom1.oxygen.data.model.location.OLocation
import com.nhom1.oxygen.data.service.OxygenService
import com.nhom1.oxygen.repository.LocationRepository
import com.nhom1.oxygen.utils.CoordinateUtil
import com.nhom1.oxygen.utils.constants.SPKeys
import com.nhom1.oxygen.utils.debugLog
import com.nhom1.oxygen.utils.gson
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class LocationRepositoryImpl(
    private val context: Context,
    private val service: OxygenService,
    private val database: OxygenDatabase
) : LocationRepository {
    private val sharedPreferences =
        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    data class CachedLocationInfo(
        val latitude: Double,
        val longitude: Double,
        val info: OLocation
    )

    private val cacheDistance = 100

    private lateinit var cachedLocation: CachedLocationInfo

    override fun getCurrentLocation(): Single<OLocation> {
        return Single.create { emitter ->
            while (true) {
                if (sharedPreferences.contains(SPKeys.CURRENT_LOCATION)) break
            }
            try {
                val location = gson.fromJson(
                    sharedPreferences.getString(SPKeys.CURRENT_LOCATION, "").toString(),
                    OLocation::class.java
                )
                debugLog("${this::class.simpleName}: Got location: $location")
                emitter.onSuccess(location)
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
    }

    override fun getLocationFromCoordinate(latitude: Double, longitude: Double): Single<OLocation> {
        return when {
            !this::cachedLocation.isInitialized ||
                    CoordinateUtil.distance(
                        Pair(cachedLocation.latitude, cachedLocation.longitude),
                        Pair(latitude, longitude)
                    ) > cacheDistance -> {
                debugLog("${this::class.simpleName}: getLocationFromCoordinate: new!")
                service.geocoding.getLocation(latitude, longitude).map {
                    cachedLocation = CachedLocationInfo(latitude, longitude, it)
                    it
                }
            }

            else -> {
                Single.create {
                    debugLog("${this::class.simpleName}: getLocationFromCoordinate: cached!")
                    it.onSuccess(cachedLocation.info)
                }
            }
        }
    }

    override fun findLocation(query: String): Single<List<OLocation>> {
        return service.geocoding.searchLocation(query)
    }

    private lateinit var cachedProvinces: List<OProvince>
    override fun getProvinces(): Single<List<OProvince>> {
        return when {
            !this::cachedProvinces.isInitialized -> {
                service.division.getProvinces().map {
                    cachedProvinces = it
                    it
                }
            }

            else -> {
                Single.create { it.onSuccess(cachedProvinces) }
            }
        }
    }

    private val cachedDistrict = mutableMapOf<String, List<ODistrict>>()

    override fun getDistricts(provinceId: String): Single<List<ODistrict>> {
        return when {
            cachedDistrict.containsKey(provinceId) -> {
                Single.create {
                    it.onSuccess(cachedDistrict[provinceId]!!)
                }
            }

            else -> {
                service.division.getDistricts(provinceId).map {
                    cachedDistrict[provinceId] = it
                    it
                }
            }
        }
    }

    private val cachedWard = mutableMapOf<String, List<OWard>>()
    override fun getWards(districtId: String): Single<List<OWard>> {
        return when {
            cachedDistrict.containsKey(districtId) -> {
                Single.create {
                    it.onSuccess(cachedWard[districtId]!!)
                }
            }

            else -> {
                service.division.getWards(districtId).map {
                    cachedWard[districtId] = it
                    it
                }
            }
        }
    }

    override fun getSearchedLocation(): Single<List<OLocation>> {
        return database.searchedLocationDao().getSearchedLocation()
    }

    override fun addSearchedLocation(location: OLocation): Completable {
        return database.searchedLocationDao().addSearchedLocation(location)
    }
}