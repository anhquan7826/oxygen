@file:OptIn(
    ExperimentalMaterial3Api::class
)

package com.nhom1.oxygen.ui.home.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nhom1.oxygen.R
import com.nhom1.oxygen.common.composables.OAppBar
import com.nhom1.oxygen.common.composables.OCard
import com.nhom1.oxygen.common.composables.OOverallStatus
import com.nhom1.oxygen.common.theme.oxygenColor
import com.nhom1.oxygen.data.model.weather.OAirQuality
import com.nhom1.oxygen.data.model.weather.OForecast
import com.nhom1.oxygen.data.model.weather.OWeatherCondition
import com.nhom1.oxygen.ui.home.HomeViewModel
import com.nhom1.oxygen.utils.extensions.toPrettyString
import com.nhom1.oxygen.utils.getTimeString

@Composable
fun OverviewComposable(viewModel: HomeViewModel) {
    val state by viewModel.overviewState.collectAsState()
    Scaffold(
        topBar = {
            OAppBar(
                title = stringResource(id = R.string.oxygen),
                leading = painterResource(id = R.drawable.fresh_air_blue),
                actions = listOf(
                    painterResource(id = R.drawable.bell)
                )
            )
        },
        contentWindowInsets = WindowInsets(
            left = 16.dp,
            right = 16.dp,
            bottom = 80.dp
        ),
        containerColor = Color.White
    ) { padding ->
        if (false /* state.state == 0 */) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    color = oxygenColor,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else {
            Column(
                Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Box(modifier = Modifier.height(32.dp))
                OOverallStatus(
                    place = "alo", country = "alo", aqi = 500,
                    modifier = Modifier.padding(
                        bottom = 16.dp
                    )
                )
                SuggestBox(suggestion = "Nên hạn chế hoạt động ngoài trời, đặc biệt vào buổi trưa. Sử dụng máy lọc không khí trong nhà và giữ cửa sổ kín để tránh khói và bụi. Nếu cần phải ra ngoài, đeo khẩu trang N95 để bảo vệ đường hô hấp.")
                Row {
                    TempBox(value = 23.0, modifier = Modifier.weight(1f))
                    Box(Modifier.width(16.dp))
                    HumidityBox(value = 60.0, modifier = Modifier.weight(1f))
                }
                WeatherForecastToday(forecasts = forecasts)
            }
        }
    }
}

@Composable
fun SuggestBox(suggestion: String) {
    OCard(modifier = Modifier
        .padding(bottom = 16.dp)
        .clickable { }) {
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
                    Icons.Rounded.KeyboardArrowRight,
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
    OCard(modifier = modifier.padding(bottom = 16.dp)) {
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
    OCard(modifier = modifier.padding(bottom = 16.dp)) {
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
                    text = stringResource(R.string.temperature)
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
    forecasts: List<OForecast>,
    celsius: Boolean = true
) {
    LazyRow(
        modifier = modifier
            .padding(bottom = 16.dp)
    ) {
        items(forecasts.size) { index ->
            val forecast = forecasts[index]
            OCard(
                contentPadding = 4.dp,
                withShadow = false,
                modifier = Modifier
                    .size(
                        width = 100.dp,
                        height = 300.dp,
                    )
                    .padding(end = if (forecast == forecasts.last()) 0.dp else 4.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = getTimeString(forecast.timeEpoch),
                        fontWeight = FontWeight.SemiBold,
                    )
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.weight(1f)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.cloudy_rainy_day),
                            contentDescription = null,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        Text(
                            text = forecast.chanceOfRain.toPrettyString() + "%",
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

val forecasts = listOf(
    OForecast(
        timeEpoch = 1699686000L,
        time = "",
        airQuality = OAirQuality(
            co = 0.0,
            no2 = 0.0,
            o3 = 0.0,
            pm10 = 0.0,
            pm25 = 0.0,
            so2 = 0.0
        ),
        chanceOfRain = 0.0,
        cloud = 0.0,
        condition = OWeatherCondition(
            text = "",
            code = 0,
            icon = ""
        ),
        feelsLikeC = 0.0,
        feelsLikeF = 0.0,
        humidity = 0.0,
        isDay = 0,
        precipitationIN = 0.0,
        precipitationMM = 0.0,
        pressureIN = 0.0,
        pressureMB = 0.0,
        tempC = 0.0,
        tempF = 0.0,
        windDegree = 0.0,
        windDir = "",
        windKPH = 0.0,
        windMPH = 0.0
    ),
    OForecast(
        timeEpoch = 1699689600,
        time = "",
        airQuality = OAirQuality(
            co = 0.0,
            no2 = 0.0,
            o3 = 0.0,
            pm10 = 0.0,
            pm25 = 0.0,
            so2 = 0.0
        ),
        chanceOfRain = 0.0,
        cloud = 0.0,
        condition = OWeatherCondition(
            text = "",
            code = 0,
            icon = ""
        ),
        feelsLikeC = 0.0,
        feelsLikeF = 0.0,
        humidity = 0.0,
        isDay = 0,
        precipitationIN = 0.0,
        precipitationMM = 0.0,
        pressureIN = 0.0,
        pressureMB = 0.0,
        tempC = 0.0,
        tempF = 0.0,
        windDegree = 0.0,
        windDir = "",
        windKPH = 0.0,
        windMPH = 0.0
    ),
    OForecast(
        timeEpoch = 1699693200,
        time = "",
        airQuality = OAirQuality(
            co = 0.0,
            no2 = 0.0,
            o3 = 0.0,
            pm10 = 0.0,
            pm25 = 0.0,
            so2 = 0.0
        ),
        chanceOfRain = 0.0,
        cloud = 0.0,
        condition = OWeatherCondition(
            text = "",
            code = 0,
            icon = ""
        ),
        feelsLikeC = 0.0,
        feelsLikeF = 0.0,
        humidity = 0.0,
        isDay = 0,
        precipitationIN = 0.0,
        precipitationMM = 0.0,
        pressureIN = 0.0,
        pressureMB = 0.0,
        tempC = 0.0,
        tempF = 0.0,
        windDegree = 0.0,
        windDir = "",
        windKPH = 0.0,
        windMPH = 0.0
    ),
    OForecast(
        timeEpoch = 1699696800,
        time = "",
        airQuality = OAirQuality(
            co = 0.0,
            no2 = 0.0,
            o3 = 0.0,
            pm10 = 0.0,
            pm25 = 0.0,
            so2 = 0.0
        ),
        chanceOfRain = 0.0,
        cloud = 0.0,
        condition = OWeatherCondition(
            text = "",
            code = 0,
            icon = ""
        ),
        feelsLikeC = 0.0,
        feelsLikeF = 0.0,
        humidity = 0.0,
        isDay = 0,
        precipitationIN = 0.0,
        precipitationMM = 0.0,
        pressureIN = 0.0,
        pressureMB = 0.0,
        tempC = 0.0,
        tempF = 0.0,
        windDegree = 0.0,
        windDir = "",
        windKPH = 0.0,
        windMPH = 0.0
    ),
    OForecast(
        timeEpoch = 1699700400,
        time = "",
        airQuality = OAirQuality(
            co = 0.0,
            no2 = 0.0,
            o3 = 0.0,
            pm10 = 0.0,
            pm25 = 0.0,
            so2 = 0.0
        ),
        chanceOfRain = 0.0,
        cloud = 0.0,
        condition = OWeatherCondition(
            text = "",
            code = 0,
            icon = ""
        ),
        feelsLikeC = 0.0,
        feelsLikeF = 0.0,
        humidity = 0.0,
        isDay = 0,
        precipitationIN = 0.0,
        precipitationMM = 0.0,
        pressureIN = 0.0,
        pressureMB = 0.0,
        tempC = 0.0,
        tempF = 0.0,
        windDegree = 0.0,
        windDir = "",
        windKPH = 0.0,
        windMPH = 0.0
    )
)

@Preview
@Composable
fun WeatherForecastTodayPreview() {
    WeatherForecastToday(
        forecasts = forecasts
    )
}