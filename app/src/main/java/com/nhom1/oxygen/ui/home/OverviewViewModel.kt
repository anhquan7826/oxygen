package com.nhom1.oxygen.ui.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class OverviewViewModel : ViewModel() {
    data class OverviewState(
        val state: Int = 0
    )

    private val _overviewState = MutableStateFlow(OverviewState())
    val overviewState = _overviewState.asStateFlow()
}