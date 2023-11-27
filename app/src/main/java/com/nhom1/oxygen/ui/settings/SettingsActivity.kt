package com.nhom1.oxygen.ui.settings

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.nhom1.oxygen.R
import com.nhom1.oxygen.common.composables.OAppBar
import com.nhom1.oxygen.common.composables.OCard
import com.nhom1.oxygen.common.composables.OSwitch
import com.nhom1.oxygen.common.theme.OxygenTheme
import com.nhom1.oxygen.repository.SettingRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsActivity : ComponentActivity() {
    private lateinit var viewModel: SettingsViewModel
    @Inject
    lateinit var settingRepository: SettingRepository

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
            contentWindowInsets = ScaffoldDefaults.contentWindowInsets.add(
                WindowInsets(
                    left = 16.dp, right = 16.dp
                )
            ),
            modifier = Modifier
                .statusBarsPadding()
                .imePadding()
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(it)
                    .padding(top = 32.dp, bottom = 16.dp)
            ) {
                SettingTile(
                    title = stringResource(R.string.temperature_unit),
                    initialValue = settingRepository.temperatureUnit,
                    enabledTitle = "Celsius",
                    disabledTitle = "Fahrenheit",
                    onCheck = { value ->
                        settingRepository.temperatureUnit = value
                    }
                )
                SettingTile(
                    title = stringResource(R.string.receive_notification),
                    initialValue = settingRepository.receiveNotification,
                    onCheck = { value ->
                        settingRepository.receiveNotification = value
                    }
                )
            }
        }
    }

    @Composable
    fun SettingTile(title: String, initialValue: Boolean, enabledTitle: String? = null, disabledTitle: String? = null, onCheck: (Boolean) -> Unit) {
        OCard(
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    modifier = Modifier.weight(1f)
                )
                OSwitch(initialValue = initialValue, onChange = onCheck, enabledTitle = enabledTitle, disabledTitle = disabledTitle)
            }
        }
    }
}