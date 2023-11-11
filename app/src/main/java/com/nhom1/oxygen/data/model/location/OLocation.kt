package com.nhom1.oxygen.data.model.location

import com.google.gson.annotations.SerializedName

data class OLocation(
    val name: String,
    val region: String,
    val country: String,
    @SerializedName("lat") val latitude: Double,
    @SerializedName("lon") val longitude: Double,
    @SerializedName("tz_id") val tzId: String,
    @SerializedName("localtime_epoch") val localtimeEpoch: Long,
    val localtime: String,
)
