package com.nhom1.oxygen.data.model.location

import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import com.nhom1.oxygen.utils.CoordinateUtil

@Entity(
    tableName = "searched_location",
    primaryKeys = [
        "latitude",
        "longitude"
    ]
)
data class OLocation(
    val name: String? = null,
    val country: String,
    @SerializedName("country_code") val countryCode: String,
    val province: String,
    val district: String,
    val ward: String,
    @SerializedName("lat") val latitude: Double,
    @SerializedName("lon") val longitude: Double,
) {
    fun distance(other: OLocation): Double {
        return CoordinateUtil.distance(
            Pair(latitude, longitude),
            Pair(other.latitude, other.longitude)
        )
    }
}
