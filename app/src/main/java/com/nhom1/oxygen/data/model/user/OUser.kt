package com.nhom1.oxygen.data.model.user

data class OUser(
    val id: Int,
    val email: String,
    val name: String,
    val uid: String,
    val diseases: List<ODisease>,
    val profile: OUserProfile
)
