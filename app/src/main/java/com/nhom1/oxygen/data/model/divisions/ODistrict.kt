package com.nhom1.oxygen.data.model.divisions

import com.google.gson.annotations.SerializedName

data class ODistrict(
    @SerializedName("codename") val id: String,
    val name: String,
)
