package com.nhom1.oxygen.repository.impl

import android.content.Context
import com.nhom1.oxygen.data.model.location.OLocation
import com.nhom1.oxygen.data.service.OxygenService
import com.nhom1.oxygen.repository.LocationRepository
import com.nhom1.oxygen.utils.constants.SPKeys
import io.reactivex.rxjava3.core.Single

class LocationRepositoryImpl(
    private val context: Context,
    private val service: OxygenService
) : LocationRepository {
    private val sharedPreferences =
        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    override fun getCurrentLocation(): Single<OLocation> {
        val currentLat = sharedPreferences.getFloat(SPKeys.CURRENT_LAT, 0F).toDouble()
        val currentLon = sharedPreferences.getFloat(SPKeys.CURRENT_LON, 0F).toDouble()
        return service.geocoding.getLocation(
            latitude = currentLat,
            longitude = currentLon
        )
    }

    override fun findLocation(query: String): Single<List<OLocation>> {
        return service.geocoding.searchLocation(query)
    }
}