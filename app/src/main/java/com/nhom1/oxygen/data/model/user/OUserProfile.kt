package com.nhom1.oxygen.data.model.user

import kotlin.math.pow

data class OUserProfile(
    val sex: Boolean? = null,
    val dateOfBirth: String? = null,
    val country: String? = null,
    val province: String? = null,
    val district: String? = null,
    val ward: String? = null,
    val address: String? = null,
    val height: Double? = null,
    val weight: Double? = null
) {
    val fullAddress: String?
        get() {
            return try {
                "${address!!}, ${ward!!}, ${district!!}, ${province!!}, ${country!!}"
            } catch (_: Exception) {
                null
            }
        }

    val bmi: Double?
        get() {
            return try {
                weight!! / (height!! / 100).pow(2.0)
            } catch (_: Exception) {
                null
            }
        }
}
