package com.nhom1.oxygen.data.model.user

import kotlin.math.pow

data class OUserProfile(
    val sex: Boolean? = null,
    val dateOfBirth: Long? = null,
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
                "${address!!}, ${ward!!}, ${district!!}, ${province!!}"
            } catch (_: Exception) {
                null
            }
        }

    val bmi: Double?
        get() {
            return try {
                weight!! / height!!.pow(2.0)
            } catch (_: Exception) {
                null
            }
        }
}
