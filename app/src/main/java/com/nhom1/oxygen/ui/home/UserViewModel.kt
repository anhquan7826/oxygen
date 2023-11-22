package com.nhom1.oxygen.ui.home

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
class UserViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
    data class UserState(
        val state: LoadState,
        val userData: OUser? = null,
        val error: String? = null
    )

    private val _state = MutableStateFlow(UserState(LoadState.LOADING))
    val state = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        _state.update {
            UserState(LoadState.LOADING)
        }
        userRepository.getUserData().listen(
            onError = { exception ->
                _state.update {
                    UserState(
                        state = LoadState.ERROR,
                        error = exception.message
                    )
                }
            }
        ) { userData ->
            _state.update {
                UserState(
                    state = LoadState.LOADED,
                    userData = userData
                )
            }
        }
    }
}