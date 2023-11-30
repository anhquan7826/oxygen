package com.nhom1.oxygen.repository.impl

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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
import com.nhom1.oxygen.utils.listen
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class LocationRepositoryImpl(
    private val context: Context,
    private val service: OxygenService,
    private val database: OxygenDatabase
) : LocationRepository {
    private val sharedPreferences = context.getSharedPreferences(
        context.packageName, Context.MODE_PRIVATE
    )

    private val cacheDistance = 1000

    private lateinit var cachedLocation: OLocation

    init {
        if (sharedPreferences.contains(SPKeys.Cache.CACHE_CURRENT_LOCATION)) {
            cachedLocation = gson.fromJson(
                sharedPreferences.getString(SPKeys.Cache.CACHE_CURRENT_LOCATION, "")!!,
                OLocation::class.java
            )
        }
    }

    @SuppressLint("MissingPermission")
    override fun getCurrentLocation(): Single<OLocation> {
        return Single.create { emitter ->
            LocationServices
                .getFusedLocationProviderClient(context)
                .lastLocation
                .addOnSuccessListener {
                    try {
                        getLocationFromCoordinate(it.latitude, it.longitude).listen(
                            onError = { e -> emitter.onError(e) }
                        ) { location ->
                            emitter.onSuccess(location)
                        }
                    } catch (e: Exception) {
                        emitter.onError(e)
                    }
                }
                .addOnFailureListener {
                    emitter.onError(it)
                }
        }
    }

    private fun getLocationFromCoordinate(latitude: Double, longitude: Double): Single<OLocation> {
        return when {
            !this::cachedLocation.isInitialized ||
                    CoordinateUtil.distance(
                        Pair(cachedLocation.latitude, cachedLocation.longitude),
                        Pair(latitude, longitude)
                    ) > cacheDistance -> {
                debugLog("${this::class.simpleName}: getLocationFromCoordinate: new!")
                service.geocoding.getLocation(latitude, longitude).doOnSuccess {
                    cachedLocation = it
                    sharedPreferences.edit().putString(
                        SPKeys.Cache.CACHE_CURRENT_LOCATION, gson.toJson(it)
                    ).apply()
                }
            }

            else -> {
                Single.create {
                    debugLog("${this::class.simpleName}: getLocationFromCoordinate: cached!")
                    it.onSuccess(cachedLocation)
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