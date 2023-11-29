package com.nhom1.oxygen.ui.history

import androidx.lifecycle.ViewModel
import com.nhom1.oxygen.data.model.history.OHistory
import com.nhom1.oxygen.repository.HistoryRepository
import com.nhom1.oxygen.utils.constants.LoadState
import com.nhom1.oxygen.utils.debugLog
import com.nhom1.oxygen.utils.listen
import com.nhom1.oxygen.utils.toMap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val historyRepository: HistoryRepository,
) : ViewModel() {
    data class HistoryState(
        val state: LoadState = LoadState.LOADING,
        val history: List<OHistory>? = null,
        val error: String? = null,
    )

    private val _state = MutableStateFlow(HistoryState())
    val state = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        _state.update {
            HistoryState()
        }
        historyRepository.get7dHistory().listen(
            onError = { exception ->
                _state.update {
                    HistoryState(
                        state = LoadState.ERROR,
                        error = exception.message
                    )
                }
            }
        ) { result ->
            _state.update {
                HistoryState(
                    state = LoadState.LOADED,
                    history = result
                )
            }
        }
    }
}