package com.nhom1.oxygen.data.model.location

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

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
    @SerializedName("suburb") val district: String,
    @SerializedName("quarter") val ward: String,
    val street: String,
    @SerializedName("lat") val latitude: Double,
    @SerializedName("lon") val longitude: Double,
)
