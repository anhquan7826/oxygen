package com.nhom1.oxygen.ui.settings

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.nhom1.oxygen.R
import com.nhom1.oxygen.common.composables.OAppBar
import com.nhom1.oxygen.common.theme.OxygenTheme

class SettingsActivity : ComponentActivity() {
    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        viewModel = ViewModelProvider(this)[SettingsViewModel::class.java]
        setContent {
            OxygenTheme {
                Surface(
                    color = Color.White
                ) {
                    SettingsView()
                }
            }
        }
    }

    @Composable
    fun SettingsView() {
        Scaffold(
            topBar = {
                OAppBar(
                    title = stringResource(R.string.settings),
                    leading = painterResource(id = R.drawable.arrow_back),
                    onLeadingPressed = {
                        finish()
                    })
            },
            containerColor = Color.White,
            modifier = Modifier.statusBarsPadding()
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(it)
                    .padding(top = 32.dp, bottom = 16.dp)
            ) {

            }
        }
    }
}