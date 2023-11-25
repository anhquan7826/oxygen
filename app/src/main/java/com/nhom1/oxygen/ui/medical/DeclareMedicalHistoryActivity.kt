package com.nhom1.oxygen.ui.medical

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.nhom1.oxygen.R
import com.nhom1.oxygen.common.composables.OAppBar
import com.nhom1.oxygen.common.composables.ODialog
import com.nhom1.oxygen.common.theme.OxygenTheme

class DeclareMedicalHistoryActivity : ComponentActivity() {
    private lateinit var viewModel: DeclareMedicalHistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        viewModel = ViewModelProvider(this)[DeclareMedicalHistoryViewModel::class.java]
        setContent {
            OxygenTheme {
                Surface(
                    color = Color.White
                ) {
                    DeclareMedicalHistoryView()
                }
            }
        }
    }

    @Composable
    fun DeclareMedicalHistoryView() {
        var showCancelDialog by remember {
            mutableStateOf(false)
        }
        Scaffold(
            topBar = {
                OAppBar(
                    title = stringResource(R.string.edit_profile),
                    leading = painterResource(id = R.drawable.cancel_colored),
                    onLeadingPressed = {
                        showCancelDialog = true
                    },
                    actions = listOf(
                        painterResource(id = R.drawable.save_colored)
                    ),
                    onActionPressed = listOf {
                        // TODO: save
                        finish()
                    }
                )
            },
            containerColor = Color.White,
            contentWindowInsets = ScaffoldDefaults.contentWindowInsets.add(
                WindowInsets(
                    left = 16.dp,
                    right = 16.dp
                )
            ),
            modifier = Modifier.statusBarsPadding()
        ) {
            Column(
                modifier = Modifier.padding(it)
            ) {

            }
        }
        if (showCancelDialog) {
            ODialog(
                title = stringResource(id = R.string.warning),
                content = stringResource(R.string.cancel_edit_content),
                cancelText = stringResource(id = R.string.back),
                confirmText = stringResource(id = R.string.cancel),
                onCancel = {
                    showCancelDialog = false
                }, onConfirm = {
                    // TODO: cancel
                    finish()
                })
        }
    }
}