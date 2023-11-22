package com.nhom1.oxygen.data.model.user

data class OUserProfile(
    val sex: Boolean,
    val dateOfBirth: String,
    val country: String,
    val province: String,
    val district: String,
    val ward: String,
    val address: String,
    val height: Double,
    val weight: Double
)
