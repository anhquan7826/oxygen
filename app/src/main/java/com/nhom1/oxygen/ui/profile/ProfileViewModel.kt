package com.nhom1.oxygen.ui.profile

import androidx.lifecycle.ViewModel
import com.nhom1.oxygen.data.model.user.OUser
import com.nhom1.oxygen.repository.UserRepository
import com.nhom1.oxygen.utils.constants.LoadState
import com.nhom1.oxygen.utils.listen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    data class ProfileState(
        val state: LoadState = LoadState.LOADING,
        val userData: OUser? = null,
        val error: String? = null,
    )

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        _state.update { ProfileState() }
        userRepository.getUserData().listen(
            onError = { exception ->
                _state.update {
                    ProfileState(
                        state = LoadState.ERROR,
                        error = exception.message
                    )
                }
            }
        ) { result ->
            _state.update {
                ProfileState(
                    state = LoadState.LOADED,
                    userData = result
                )
            }
        }
    }
}