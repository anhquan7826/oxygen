package com.nhom1.oxygen.ui.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SearchViewModel : ViewModel() {
    data class SearchState(
        val searchValue: String,
        val result: List<String>
    )

    private val _state = MutableStateFlow(SearchState("", listOf()))
    val state = _state.asStateFlow()

    fun getSearchResult(value: String) {
        _state.update {
            SearchState(value, listOf())
        }
    }
}