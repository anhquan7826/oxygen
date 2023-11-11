package com.nhom1.oxygen.ui.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {
    private val _overviewState = MutableStateFlow(OverviewState())
    val overviewState = _overviewState.asStateFlow()
}


data class OverviewState(
    val state: Int = 0
)