package com.nhom1.oxygen.ui.home.composables

import androidx.compose.runtime.Composable
import com.nhom1.oxygen.common.composables.OExposure

@Composable
fun UserComposable() {
    OExposure(aqis = (0..23).map { 100 })
}