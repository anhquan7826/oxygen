package com.nhom1.oxygen.common.composables

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nhom1.oxygen.common.theme.oxygenColor

@Composable
fun OLoading(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        color = oxygenColor,
        modifier = modifier
    )
}