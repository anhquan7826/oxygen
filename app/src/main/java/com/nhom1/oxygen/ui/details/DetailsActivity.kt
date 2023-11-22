@file:OptIn(ExperimentalFoundationApi::class)

package com.nhom1.oxygen.ui.details

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImage
import com.nhom1.oxygen.R
import com.nhom1.oxygen.common.composables.AutoSizeText
import com.nhom1.oxygen.common.composables.OAppBar
import com.nhom1.oxygen.common.composables.OCard
import com.nhom1.oxygen.common.composables.OError
import com.nhom1.oxygen.common.composables.OLoading
import com.nhom1.oxygen.common.constants.getAQIColor
import com.nhom1.oxygen.common.theme.OxygenTheme
import com.nhom1.oxygen.data.model.location.OLocation
import com.nhom1.oxygen.data.model.weather.OAirQuality
import com.nhom1.oxygen.data.model.weather.OWeather
import com.nhom1.oxygen.utils.constants.LoadState
import com.nhom1.oxygen.utils.extensions.oShadow
import com.nhom1.oxygen.utils.fromJson
import com.nhom1.oxygen.utils.getTimeString
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsActivity : ComponentActivity() {
    private lateinit var viewModel: DetailsViewModel
    private var location: OLocation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        viewModel = ViewModelProvider(this)[DetailsViewModel::class.java]
        if (intent.extras != null && intent.extras!!.containsKey("location")) {
            location =
                fromJson(intent.extras!!.getString("location").toString(), OLocation::class.java)
        }
        setContent {
            OxygenTheme {
                Surface(
                    color = Color.White
                ) {
                    DetailsView()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (location != null) viewModel.load(location!!)
    }

    @Composable
    fun DetailsView() {
        val state by viewModel.state.collectAsState()
        Scaffold(
            topBar = {
                OAppBar(
                    title = stringResource(id = R.string.details),
                    leading = painterResource(id = R.drawable.arrow_back),
                    onLeadingPressed = {
                        finish()
                    },
                    actions = listOf(
                        painterResource(id = R.drawable.information)
                    ),
                    onActionPressed = listOf {
                        startActivity(Intent(this, DetailsHelperActivity::class.java))
                    },
                )
            },
            containerColor = Color.White,
            contentWindowInsets = WindowInsets(left = 16.dp, right = 16.dp),
            modifier = Modifier.systemBarsPadding()
        ) {
            if (location == null) {
                Box(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize()
                ) {
                    OError(
                        modifier = Modifier.align(Alignment.Center),
                        error = stringResource(R.string.invalid_location_data),
                        buttonLabel = stringResource(R.string.back)
                    ) {
                        finish()
                    }
                }
            } else {
                when (state.state) {
                    LoadState.LOADING -> {
                        Box(
                            modifier = Modifier
                                .padding(it)
                                .fillMaxSize()
                        ) {
                            OLoading(
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }

                    LoadState.ERROR -> {
                        Box(
                            modifier = Modifier
                                .padding(it)
                                .fillMaxSize()
                        ) {
                            OError(
                                modifier = Modifier.align(Alignment.Center),
                                error = state.error
                            ) {
                                viewModel.load(location!!)
                            }
                        }
                    }

                    else -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(it)
                                .padding(top = 32.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.location)
                            )
                            Text(
                                text = "${location!!.suburb}, ${location!!.district}, ${location!!.city}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = location!!.country,
                                fontSize = 16.sp,
                            )
                            AtmosphericIndex(
                                value = OAirQuality(
                                    state.weatherCurrent!!.airQuality.co,
                                    state.weatherCurrent!!.airQuality.no2,
                                    state.weatherCurrent!!.airQuality.o3,
                                    state.weatherCurrent!!.airQuality.so2,
                                    state.weatherCurrent!!.airQuality.pm25,
                                    state.weatherCurrent!!.airQuality.pm10,
                                    state.weatherCurrent!!.airQuality.aqi
                                ),
                                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
                            )
                            WeatherForecast()
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun AQIChart(modifier: Modifier = Modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            Text(
                text = stringResource(R.string.aqi_chart)
            )
        }
    }

    @Composable
    fun WeatherForecast(modifier: Modifier = Modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            Text(
                text = stringResource(R.string.aqi_chart),
                modifier = Modifier.padding(16.dp)
            )
            HorizontalPager(
                state = rememberPagerState { 3 }
            ) {

            }
        }
    }

    @Composable
    fun AtmosphericIndex(modifier: Modifier = Modifier, value: OAirQuality) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            Text(
                text = stringResource(R.string.atmospheric_index),
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row(
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                IndexCard(
                    type = "CO",
                    value = value.co,
                    unit = "µg/m3",
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp)
                )
                IndexCard(
                    type = "O3",
                    value = value.o3,
                    unit = "µg/m3",
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp)
                )
            }
            Row(
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                IndexCard(
                    type = "NO2",
                    value = value.no2,
                    unit = "µg/m3",
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp)
                )
                IndexCard(
                    type = "SO2",
                    value = value.so2,
                    unit = "µg/m3",
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp)
                )
            }
            Row {
                IndexCard(
                    type = "PM10",
                    autoSubscriptType = false,
                    value = value.pm10,
                    unit = "µg/m3",
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp)
                )
                IndexCard(
                    type = "PM2.5",
                    autoSubscriptType = false,
                    value = value.pm25,
                    unit = "µg/m3",
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp)
                )
            }
        }
    }

    @Composable
    fun IndexCard(
        modifier: Modifier = Modifier,
        type: String,
        autoSubscriptType: Boolean = true,
        value: Double,
        unit: String
    ) {
        val subscript = SpanStyle(
            baselineShift = BaselineShift.Subscript,
            fontSize = 12.sp,
        )
        val subscript2 = SpanStyle(
            baselineShift = BaselineShift.Subscript,
            fontSize = 10.sp,
        )

        OCard(
            modifier = modifier.oShadow()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (autoSubscriptType) {
                    Text(
                        text = buildAnnotatedString {
                            val parts = type.split(Regex("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)"))
                            for (part in parts) {
                                if (part.all { it.isDigit() }) {
                                    withStyle(subscript) {
                                        append(part)
                                    }
                                } else {
                                    append(part)
                                }
                            }
                        },
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Text(
                        text = type,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(1f)
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.weight(1f)
                ) {
                    AutoSizeText(
                        value.toString(),
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        maxTextSize = 32.sp
                    )
                    Text(
                        text = buildAnnotatedString {
                            val parts = unit.split(Regex("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)"))
                            for (part in parts) {
                                if (part.all { it.isDigit() }) {
                                    withStyle(subscript2) {
                                        append(part)
                                    }
                                } else {
                                    append(part)
                                }
                            }
                        },
                        fontSize = 14.sp,
                    )
                }
            }
        }
    }

    @Composable
    fun ForecastDay(time: String, now: OWeather, next24h: List<OWeather>, celsius: Boolean = true) {
        OCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Row {
                    Column {
                        Text(text = time)
                        Text(text = if (celsius) "${now.tempC}°C" else "${now.tempF}°F")
                    }
                    AsyncImage(now.condition.icon, contentDescription = null)
                }
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState())
                ) {
                    for (forecast in next24h) {
                        OCard(
                            modifier = Modifier.width(128.dp)
                        ) {
                            Column {
                                Text(
                                    text = getTimeString(forecast.time)
                                )
                                if (now.chanceOfRain != null) Text(
                                    text = "${now.chanceOfRain}%"
                                )
                                Text(text = if (celsius) "${forecast.tempC}°C" else "${forecast.tempF}°F")
                                Box(
                                    modifier = Modifier.background(
                                        color = getAQIColor(forecast.airQuality.aqi)
                                    )
                                ) {
                                    Text(text = forecast.airQuality.aqi.toString())
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}