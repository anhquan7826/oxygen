package com.nhom1.oxygen.data.model.location

import com.google.gson.annotations.SerializedName

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
