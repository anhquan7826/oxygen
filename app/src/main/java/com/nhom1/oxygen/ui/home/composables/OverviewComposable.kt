package com.nhom1.oxygen.ui.home.composables

import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
import com.nhom1.oxygen.utils.constants.LoadState.ERROR
import com.nhom1.oxygen.utils.constants.LoadState.LOADING
import com.nhom1.oxygen.utils.extensions.oBorder
import com.nhom1.oxygen.utils.extensions.toPrettyString
import com.nhom1.oxygen.utils.getTimeString
import com.nhom1.oxygen.utils.toJson

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OverviewComposable(viewModel: OverviewViewModel, homeController: HomeActivity.HomePageController) {
    val state by viewModel.overviewState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    Scaffold(
        topBar = {
            OAppBar(
                title = stringResource(id = R.string.oxygen),
                leading = painterResource(id = R.drawable.fresh_air_blue),
                actions = listOf(
                    painterResource(id = R.drawable.bell)
                ),
                onActionPressed = listOf {
                    // TODO: Open notification
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
                        .padding(padding)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    Box(modifier = Modifier.height(32.dp))
                    OOverallStatus(
                        place = "${state.location!!.district}, ${state.location!!.province}",
                        country = state.location!!.country,
                        aqi = state.weatherCurrent!!.airQuality.aqi,
                        modifier = Modifier.padding(
                            bottom = 16.dp
                        )
                    )
                    // TODO: Suggestion
                    SuggestBox(suggestion = "Nên hạn chế hoạt động ngoài trời, đặc biệt vào buổi trưa. Sử dụng máy lọc không khí trong nhà và giữ cửa sổ kín để tránh khói và bụi. Nếu cần phải ra ngoài, đeo khẩu trang N95 để bảo vệ đường hô hấp.") {
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
                    Row {
                        TempBox(value = 23.0, modifier = Modifier.weight(1f), celsius = viewModel.tempUnit)
                        Box(Modifier.width(16.dp))
                        HumidityBox(value = 60.0, modifier = Modifier.weight(1f))
                    }
                    WeatherForecastToday(forecasts = state.weather24h!!, celsius = viewModel.tempUnit)
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
    OCard(modifier = Modifier
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
fun TempBox(modifier: Modifier = Modifier, value: Double, celsius: Boolean = true) {
    OCard(
        modifier = modifier
            .padding(bottom = 16.dp)
            .oBorder()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.temperature),
                contentDescription = null,
                modifier = Modifier.size(72.dp)
            )
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(R.string.temperature)
                )
                Text(
                    text = value.toPrettyString() + if (celsius) "°C" else "°F",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}

@Composable
fun HumidityBox(modifier: Modifier = Modifier, value: Double) {
    OCard(
        modifier = modifier
            .padding(bottom = 16.dp)
            .oBorder()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.humidity),
                contentDescription = null,
                modifier = Modifier.size(72.dp)
            )
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(R.string.humidity)
                )
                Text(
                    text = value.toPrettyString() + "%",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 16.dp)
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
        repeat(forecasts.size) { index ->
            val forecast = forecasts[index]
            OCard(
                contentPadding = 4.dp,
                modifier = Modifier
                    .padding(end = if (forecast == forecasts.last()) 0.dp else 4.dp)
                    .size(
                        width = 100.dp,
                        height = 300.dp,
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
                            modifier = Modifier.padding(horizontal = 8.dp)
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