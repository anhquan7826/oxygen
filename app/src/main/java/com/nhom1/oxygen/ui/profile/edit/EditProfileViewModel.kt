package com.nhom1.oxygen.ui.profile.edit

import androidx.lifecycle.ViewModel
import com.nhom1.oxygen.data.model.user.OUser
import com.nhom1.oxygen.utils.constants.LoadState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EditProfileViewModel : ViewModel() {
    data class EditProfileState(
        val state: LoadState = LoadState.LOADING,
        val userData: OUser? = null
    )

    private lateinit var initialUserData: OUser

    private val _state = MutableStateFlow(EditProfileState())
    val state = _state.asStateFlow()

    fun load(initialUserData: OUser) {
        this.initialUserData = initialUserData
        _state.update { EditProfileState() }
    }
}