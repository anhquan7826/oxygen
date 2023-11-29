package com.nhom1.oxygen.ui.home.composables

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.nhom1.oxygen.R
import com.nhom1.oxygen.common.composables.OAppBar
import com.nhom1.oxygen.common.composables.OButtonPrimary
import com.nhom1.oxygen.common.composables.OCard
import com.nhom1.oxygen.common.composables.OError
import com.nhom1.oxygen.common.composables.OLoading
import com.nhom1.oxygen.common.composables.OOverallStatus
import com.nhom1.oxygen.data.model.weather.OWeather
import com.nhom1.oxygen.ui.details.DetailsActivity
import com.nhom1.oxygen.ui.home.HomeActivity
import com.nhom1.oxygen.ui.home.OverviewViewModel
import com.nhom1.oxygen.ui.notification.NotificationActivity
import com.nhom1.oxygen.utils.constants.LoadState.ERROR
import com.nhom1.oxygen.utils.constants.LoadState.LOADING
import com.nhom1.oxygen.utils.extensions.oBorder
import com.nhom1.oxygen.utils.extensions.toPrettyString
import com.nhom1.oxygen.utils.getTimeString
import com.nhom1.oxygen.utils.toJson

@Composable
fun OverviewComposable(
    viewModel: OverviewViewModel,
    homeController: HomeActivity.HomePageController
) {
    val state by viewModel.overviewState.collectAsState()
    val context = LocalContext.current
    val notificationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.data?.hasExtra("count") == true) {
            viewModel.setNotificationCount(it.data!!.getIntExtra("count", 0))
        }
    }
    Scaffold(
        topBar = {
            OAppBar(
                title = stringResource(id = R.string.oxygen),
                leading = painterResource(id = R.drawable.fresh_air_blue),
                actions = listOf(
                    if (state.notifications == 0)
                        painterResource(id = R.drawable.bell)
                    else
                        painterResource(id = R.drawable.bell_available)
                ),
                onActionPressed = listOf {
                    notificationLauncher.launch(Intent(context, NotificationActivity::class.java))
                }
            )
        },
        contentWindowInsets = WindowInsets(
            left = 16.dp,
            right = 16.dp,
        ),
        containerColor = Color.White
    ) { padding ->
        when (state.state) {
            LOADING -> {
                Box(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                ) {
                    OLoading(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            ERROR -> {
                Box(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                ) {
                    OError(
                        error = state.error,
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        viewModel.load()
                    }
                }
            }

            else -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(padding)
                        .padding(top = 32.dp)
                        .fillMaxSize()
                ) {
                    OOverallStatus(
                        place = "${state.location!!.district}, ${state.location!!.province}",
                        country = state.location!!.country,
                        aqi = state.weatherCurrent!!.airQuality.aqi,
                        modifier = Modifier.padding(
                            bottom = 16.dp
                        )
                    )
                    SuggestBox(suggestion = state.suggestion!!) {
                        homeController.goto(2)
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(
                            top = 16.dp,
                            bottom = 16.dp
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.information),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .size(32.dp)
                        )
                        Text(
                            text = stringResource(R.string.weather_info),
                            fontSize = 16.sp,
                        )
                    }
                    WeatherBox(weather = state.weatherCurrent!!, celsius = viewModel.tempUnit)
                    WeatherForecastToday(
                        forecasts = state.weather24h!!,
                        celsius = viewModel.tempUnit
                    )
                    OButtonPrimary(
                        text = stringResource(R.string.details),
                        modifier = Modifier.padding(bottom = 32.dp)
                    ) {
                        context.startActivity(
                            Intent(context, DetailsActivity::class.java)
                                .putExtra(
                                    "location",
                                    toJson(state.location!!)
                                )
                                .putExtra("weatherCurrent", toJson(state.weatherCurrent!!))
                                .putExtra(
                                    "weather24h",
                                    state.weather24h!!.map { e -> toJson(e) }.toTypedArray()
                                )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SuggestBox(suggestion: String, onClick: () -> Unit) {
    OCard(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .oBorder(),
        onClick = onClick
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.daily_suggestion),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = stringResource(R.string.see_more),
                    fontSize = 12.sp,
                    color = Color(0xFF7A7A7A)
                )
                Icon(
                    painter = painterResource(id = R.drawable.chevron_right),
                    contentDescription = null
                )
            }
            Text(
                text = suggestion,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Composable
fun WeatherBox(modifier: Modifier = Modifier, weather: OWeather, celsius: Boolean = true) {
    OCard(
        modifier = modifier
            .padding(bottom = 16.dp)
            .oBorder()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = if (celsius) "${weather.tempC.toPrettyString()}°C" else "${weather.tempF.toPrettyString()}°F",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Text(
                    text = weather.condition.text,
                    textAlign = TextAlign.Center
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                AsyncImage(
                    weather.condition.icon,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(16.dp)
                        .size(72.dp)
                )
                if (weather.chanceOfRain != null) Text(
                    text = "${stringResource(R.string.chance_of_rain)}:\n${weather.chanceOfRain}%",
                    fontSize = 12.sp,
                    softWrap = true,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun WeatherForecastToday(
    modifier: Modifier = Modifier,
    forecasts: List<OWeather>,
    celsius: Boolean = true
) {
    Row(
        modifier = modifier
            .padding(bottom = 16.dp)
            .horizontalScroll(rememberScrollState())
    ) {
        forecasts.forEach { forecast ->
            OCard(
                contentPadding = 4.dp,
                modifier = Modifier
                    .padding(end = if (forecast == forecasts.last()) 0.dp else 4.dp)
                    .size(
                        width = 100.dp,
                        height = 150.dp,
                    )
                    .oBorder()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = getTimeString(forecast.time),
                        fontWeight = FontWeight.SemiBold,
                    )
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.weight(1f)
                    ) {
                        AsyncImage(
                            forecast.condition.icon,
                            contentDescription = null,
                            modifier = Modifier.size(42.dp)
                        )
                        if (forecast.chanceOfRain != null)
                            Text(
                                text = "${forecast.chanceOfRain}%",
                                fontSize = 12.sp
                            )
                    }
                    Text(
                        text = forecast.tempC.toPrettyString() + if (celsius) "°C" else "°F",
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}