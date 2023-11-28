package com.nhom1.oxygen.ui.settings

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.nhom1.oxygen.R
import com.nhom1.oxygen.common.composables.OAppBar
import com.nhom1.oxygen.common.composables.OCard
import com.nhom1.oxygen.common.composables.OSwitch
import com.nhom1.oxygen.common.composables.OTextSwitch
import com.nhom1.oxygen.common.theme.OxygenTheme
import com.nhom1.oxygen.repository.SettingRepository
import com.nhom1.oxygen.utils.constants.OLanguage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsActivity : ComponentActivity() {
    @Inject
    lateinit var settingRepository: SettingRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
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
                LanguageTile()
                TempUnitTile()
                NotificationTile()
            }
        }
    }

    @Composable
    fun LanguageTile() {
        OCard(
            modifier = Modifier.padding(bottom = 8.dp).fillMaxWidth()
        ) {
            Column {
                Text(
                    text = stringResource(R.string.language),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OTextSwitch(
                    values = listOf(
                        OLanguage.UNSPECIFIED, OLanguage.VIETNAMESE, OLanguage.ENGLISH
                    ),
                    initialValue = settingRepository.language,
                    title = {
                        when (it) {
                            OLanguage.VIETNAMESE -> resources.getString(R.string.vietnamese)
                            OLanguage.ENGLISH -> resources.getString(R.string.english)
                            else -> resources.getString(R.string.system)
                        }
                    }
                ) {
                    settingRepository.language = it
                    Toast.makeText(
                        this@SettingsActivity,
                        resources.getString(R.string.language_will_change_after_app_restart),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    @Composable
    fun TempUnitTile() {
        OCard(
            modifier = Modifier.padding(bottom = 8.dp).fillMaxWidth()
        ) {
            Column {
                Text(
                    text = stringResource(R.string.temperature_unit),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OTextSwitch(
                    values = listOf(
                        true, false
                    ),
                    initialValue = settingRepository.temperatureUnit,
                    title = {
                        if (it) "Celsius" else "Fahrenheit"
                    }
                ) {
                    settingRepository.temperatureUnit = it
                }
            }
        }
    }

    @Composable
    fun NotificationTile() {
        OCard(
            modifier = Modifier.padding(bottom = 8.dp).fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.receive_notification),
                    modifier = Modifier.weight(1f)
                )
                OSwitch(
                    initialValue = settingRepository.receiveNotification,
                ) {
                    settingRepository.receiveNotification = it
                }
            }
        }
    }
}