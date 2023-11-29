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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImage
import com.nhom1.oxygen.R
import com.nhom1.oxygen.common.composables.AutoSizeText
import com.nhom1.oxygen.common.composables.OAppBar
import com.nhom1.oxygen.common.composables.OBarChart
import com.nhom1.oxygen.common.composables.OBarChartData
import com.nhom1.oxygen.common.composables.OCard
import com.nhom1.oxygen.common.composables.OError
import com.nhom1.oxygen.common.composables.OLoading
import com.nhom1.oxygen.common.composables.OPageIndicator
import com.nhom1.oxygen.common.composables.PieChart
import com.nhom1.oxygen.common.theme.OxygenTheme
import com.nhom1.oxygen.data.model.location.OLocation
import com.nhom1.oxygen.data.model.weather.OAirQuality
import com.nhom1.oxygen.data.model.weather.OWeather
import com.nhom1.oxygen.utils.constants.LoadState
import com.nhom1.oxygen.utils.extensions.oBorder
import com.nhom1.oxygen.utils.extensions.toPrettyString
import com.nhom1.oxygen.utils.getAQIColor
import com.nhom1.oxygen.utils.getAQILevel
import com.nhom1.oxygen.utils.getHour
import com.nhom1.oxygen.utils.getTimeString
import com.nhom1.oxygen.utils.gson
import com.nhom1.oxygen.utils.now
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsActivity : ComponentActivity() {
    private lateinit var viewModel: DetailsViewModel
    private var location: OLocation? = null
    private var weatherCurrent: OWeather? = null
    private var weather24h: List<OWeather>? = null
    private var weather7d: List<OWeather>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        viewModel = ViewModelProvider(this)[DetailsViewModel::class.java]
        if (intent.extras != null) {
            if (intent.extras!!.containsKey("location")) {
                location = gson.fromJson(
                    intent.extras!!.getString("location").toString(),
                    OLocation::class.java
                )
            }
            if (intent.extras!!.containsKey("weatherCurrent")) {
                weatherCurrent = gson.fromJson(
                    intent.extras!!.getString("weatherCurrent").toString(),
                    OWeather::class.java
                )
            }
            if (intent.extras!!.containsKey("weather24h")) {
                weather24h = intent.extras!!.getStringArray("weather24h")?.map { e ->
                    gson.fromJson(e.toString(), OWeather::class.java)
                }
            }
            if (intent.extras!!.containsKey("weather7d")) {
                weather7d = intent.extras!!.getStringArray("weather7d")?.map { e ->
                    gson.fromJson(e.toString(), OWeather::class.java)
                }
            }
        }
        if (location != null) viewModel.load(location!!, weatherCurrent, weather24h, weather7d)
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
                                text = "${location!!.ward}, ${location!!.district}, ${location!!.province}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = location!!.country,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(bottom = 32.dp)
                            )
                            AQIChart(
                                modifier = Modifier.padding(bottom = 32.dp),
                                next24h = state.weather24h!!,
                                next7d = state.weather7d!!
                            )
                            AtmosphericIndex(
                                value = OAirQuality(
                                    state.weatherCurrent!!.airQuality.co,
                                    state.weatherCurrent!!.airQuality.no2,
                                    state.weatherCurrent!!.airQuality.o3,
                                    state.weatherCurrent!!.airQuality.so2,
                                    state.weatherCurrent!!.airQuality.pm2_5,
                                    state.weatherCurrent!!.airQuality.pm10,
                                    state.weatherCurrent!!.airQuality.aqi
                                ),
                                modifier = Modifier.padding(bottom = 32.dp)
                            )
                            WeatherForecast(
                                current = state.weatherCurrent!!,
                                next24h = state.weather24h!!,
                                next7d = state.weather7d!!,
                                celsius = viewModel.tempUnit,
                                modifier = Modifier.padding(bottom = 32.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun AQIChart(modifier: Modifier = Modifier, next24h: List<OWeather>, next7d: List<OWeather>) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            Text(
                text = stringResource(R.string.aqi_chart),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            val pagerState = rememberPagerState { 2 }
            val coroutineScope = rememberCoroutineScope()
            OPageIndicator(
                indicators = listOf(
                    stringResource(id = R.string.today),
                    stringResource(id = R.string.next_week)
                ),
                currentIndex = pagerState.currentPage,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .width(300.dp)
            ) {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(it, 0F)
                }
            }
            HorizontalPager(state = pagerState) {
                when (it) {
                    0 -> {
                        OBarChart(
                            data = OBarChartData(
                                barsData = next24h.map { weather ->
                                    OBarChartData.OBarData(
                                        label = "${getTimeString(weather.time, "HH")}h",
                                        value = weather.airQuality.aqi.toDouble(),
                                        color = getAQIColor(weather.airQuality.aqi).copy(
                                            alpha = if (getHour(now()) >= getHour(
                                                    weather.time
                                                )
                                            ) 1f else 0.5f
                                        ),
                                    ) {

                                    }
                                },
                                maxYValue = 500.0,
                            )
                        )
                    }

                    1 -> {
                        OBarChart(
                            data = OBarChartData(
                                barsData = next7d.map { weather ->
                                    OBarChartData.OBarData(
                                        label = getTimeString(weather.time, "dd/MM"),
                                        value = weather.airQuality.aqi.toDouble(),
                                        color = getAQIColor(weather.airQuality.aqi),
                                    ) {

                                    }
                                },
                                maxYValue = 500.0,
                            )
                        )
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun WeatherForecast(
        modifier: Modifier = Modifier,
        current: OWeather,
        next24h: List<OWeather>,
        next7d: List<OWeather>,
        celsius: Boolean = true,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            Text(
                text = stringResource(R.string.weather_forecast),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            val pagerState = rememberPagerState { 2 }
            val coroutineScope = rememberCoroutineScope()
            OPageIndicator(
                indicators = listOf(
                    stringResource(id = R.string.today),
                    stringResource(R.string.next_week)
                ),
                currentIndex = pagerState.currentPage,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .width(300.dp)
            ) {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(it, 0F)
                }
            }
            HorizontalPager(
                state = pagerState
            ) {
                when (it) {
                    0 -> ForecastDay(
                        time = getTimeString(
                            now(),
                            "dd/MM/yyyy - HH:mm"
                        ),
                        now = current,
                        next24h = next24h.filter { e -> getHour(e.time) > getHour(now()) },
                        celsius = celsius
                    )

                    1 -> ForecastWeek(forecasts = next7d, celsius = celsius)
                }
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
                modifier = Modifier.padding(bottom = 16.dp)
            )
            OCard(
                backgroundColor = getAQIColor(value.aqi),
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
                    .height(150.dp)
                    .oBorder()
            ) {
                Row {
                    val color = if (getAQILevel(value.aqi) > 1) Color.White else Color.Black
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                    ) {
                        Text(
                            text = "AQI",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = color,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                    ) {
                        PieChart(
                            value = value.aqi / 500.0,
                            dark = color == Color.Black,
                            modifier = Modifier.align(Alignment.Center)
                        )
                        Text(
                            text = value.aqi.toString(),
                            fontSize = 32.sp,
                            color = color,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
            Row(
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                IndexCard(
                    type = "CO",
                    value = value.co,
                    unit = "µg/m3",
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                        .padding(end = 4.dp)
                )
                IndexCard(
                    type = "O3",
                    value = value.o3,
                    unit = "µg/m3",
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
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
                        .height(100.dp)
                        .padding(end = 4.dp)
                )
                IndexCard(
                    type = "SO2",
                    value = value.so2,
                    unit = "µg/m3",
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
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
                        .height(100.dp)
                        .padding(end = 4.dp)
                )
                IndexCard(
                    type = "PM2.5",
                    autoSubscriptType = false,
                    value = value.pm2_5,
                    unit = "µg/m3",
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
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
            modifier = modifier.oBorder()
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
                        value.toPrettyString(),
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
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .oBorder()
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .weight(3f)
                        .padding(bottom = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.weight(2f)
                    ) {
                        Text(
                            text = time,
                            fontWeight = FontWeight.Light
                        )
                        Column(
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = if (celsius) "${now.tempC.toPrettyString()}°C" else "${now.tempF.toPrettyString()}°F",
                                fontSize = 48.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            if (now.chanceOfRain != null) Text(
                                text = "${stringResource(R.string.chance_of_rain)}: ${now.chanceOfRain}%",
                                fontSize = 12.sp,
                                softWrap = true,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                    ) {
                        AsyncImage(
                            now.condition.icon,
                            contentDescription = null,
                            modifier = Modifier
                                .size(72.dp)
                                .align(Alignment.Center)
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .weight(5f)
                        .horizontalScroll(rememberScrollState())
                ) {
                    for (forecast in next24h) {
                        OCard(
                            backgroundColor = Color.White,
                            contentPadding = 4.dp,
                            modifier = Modifier
                                .width(72.dp)
                                .padding(end = if (forecast == next24h.last()) 0.dp else 4.dp)
                                .fillMaxHeight()
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = getTimeString(forecast.time),
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.weight(1f)
                                )
                                AsyncImage(
                                    forecast.condition.icon,
                                    contentDescription = null,
                                    modifier = Modifier.size(38.dp)
                                )
                                if (forecast.chanceOfRain != null) Text(
                                    text = "${forecast.chanceOfRain}%",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Light,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                                Text(
                                    text = if (celsius) "${forecast.tempC.toPrettyString()}°C" else "${forecast.tempF.toPrettyString()}°F",
                                    modifier = Modifier.padding(top = 12.dp)
                                )
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                ) {
                                    Text(
                                        text = forecast.airQuality.aqi.toString(),
                                        textAlign = TextAlign.Center,
                                        color = if (getAQILevel(forecast.airQuality.aqi) > 1) Color.White else Color.Black,
                                        fontSize = 12.sp,
                                        modifier = Modifier
                                            .align(Alignment.BottomCenter)
                                            .padding(vertical = 2.dp)
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(
                                                color = getAQIColor(forecast.airQuality.aqi)
                                            )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun ForecastWeek(forecasts: List<OWeather>, celsius: Boolean = true) {
        OCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .oBorder()
        ) {
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState())
            ) {
                for (forecast in forecasts) {
                    OCard(
                        backgroundColor = Color.White,
                        modifier = Modifier
                            .padding(end = if (forecast == forecasts.last()) 0.dp else 8.dp)
                            .width(200.dp)
                            .fillMaxHeight()
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = getTimeString(forecast.time, "dd/MM/yyyy"),
                                fontWeight = FontWeight.SemiBold
                            )
                            Box(
                                modifier = Modifier
                                    .padding(bottom = 8.dp)
                                    .weight(1f)
                                    .fillMaxSize()
                            ) {
                                AsyncImage(
                                    forecast.condition.icon,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .size(54.dp)
                                )
                            }
                            Text(
                                text = stringResource(id = R.string.temperature),
                                fontWeight = FontWeight.Light,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            Text(
                                text = if (celsius) "${forecast.tempC.toPrettyString()}°C" else "${forecast.tempF.toPrettyString()}°F",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            if (forecast.chanceOfRain != null) {
                                Text(
                                    text = stringResource(id = R.string.chance_of_rain),
                                    fontWeight = FontWeight.Light,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                                Text(
                                    text = "${forecast.chanceOfRain}%",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }

                            Text(
                                text = stringResource(id = R.string.aqi_index_title),
                                fontWeight = FontWeight.Light,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            Text(
                                text = forecast.airQuality.aqi.toString(),
                                textAlign = TextAlign.Center,
                                color = if (getAQILevel(forecast.airQuality.aqi) > 1) Color.White else Color.Black,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        color = getAQIColor(forecast.airQuality.aqi)
                                    )
                                    .padding(vertical = 2.dp, horizontal = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}