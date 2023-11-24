package com.nhom1.oxygen.ui.home

import androidx.lifecycle.ViewModel
import com.nhom1.oxygen.data.model.history.OHistory
import com.nhom1.oxygen.data.model.user.OUser
import com.nhom1.oxygen.repository.HistoryRepository
import com.nhom1.oxygen.repository.UserRepository
import com.nhom1.oxygen.utils.constants.LoadState
import com.nhom1.oxygen.utils.listen
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val historyRepository: HistoryRepository
) : ViewModel() {
    data class UserState(
        val state: LoadState,
        val userData: OUser? = null,
        val history: OHistory? = null,
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
        Single.zip(
            userRepository.getUserData(),
            historyRepository.getTodayHistory()
        ) { userData, history ->
            UserState(
                state = LoadState.LOADED,
                userData = userData,
                history = history,
            )
        }.listen(
            onError = { exception ->
                _state.update {
                    UserState(
                        state = LoadState.ERROR,
                        error = exception.message
                    )
                }
            }
        ) { result ->
            _state.update { result }
        }
    }

    fun logout() {
        userRepository.signOut()
    }
}