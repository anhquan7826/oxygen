@file:OptIn(ExperimentalFoundationApi::class)

package com.nhom1.oxygen.ui.history

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.nhom1.oxygen.R
import com.nhom1.oxygen.common.composables.OAppBar
import com.nhom1.oxygen.common.composables.OBarChart
import com.nhom1.oxygen.common.composables.OBarChartData
import com.nhom1.oxygen.common.composables.OError
import com.nhom1.oxygen.common.composables.OLoading
import com.nhom1.oxygen.common.composables.OTab
import com.nhom1.oxygen.common.composables.OTabRow
import com.nhom1.oxygen.common.constants.OxygenColors
import com.nhom1.oxygen.common.constants.getAQIColor
import com.nhom1.oxygen.common.theme.OxygenTheme
import com.nhom1.oxygen.data.model.history.OHourlyHistory
import com.nhom1.oxygen.utils.constants.LoadState
import com.nhom1.oxygen.utils.getTimeString
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.ln

@AndroidEntryPoint
class HistoryActivity : ComponentActivity() {
    private lateinit var viewModel: HistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        viewModel = ViewModelProvider(this)[HistoryViewModel::class.java]
        setContent {
            OxygenTheme {
                Surface(
                    color = Color.White
                ) {
                    HistoryView()
                }
            }
        }
    }

    @Composable
    fun HistoryView() {
        val state by viewModel.state.collectAsState()
        Scaffold(
            topBar = {
                OAppBar(
                    title = stringResource(id = R.string.exposure_history),
                    leading = painterResource(id = R.drawable.arrow_back),
                    withShadow = state.history?.isEmpty() ?: true,
                    onLeadingPressed = {
                        finish()
                    }
                )
            },
            containerColor = Color.White,
            modifier = Modifier.statusBarsPadding()
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                when (state.state) {
                    LoadState.LOADING -> {
                        OLoading(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    LoadState.ERROR -> {
                        OError(
                            modifier = Modifier.align(Alignment.Center),
                            error = state.error
                        ) {
                            viewModel.load()
                        }
                    }

                    else -> {
                        if (state.history!!.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Text(
                                    text = stringResource(R.string.no_history),
                                    color = Color.Gray,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        } else {
                            val pagerState = rememberPagerState { state.history!!.size }
                            val coroutineScope = rememberCoroutineScope()
                            Column(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                OTabRow(
                                    selectedTabIndex = pagerState.currentPage,
                                    scrollable = true,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    state.history!!.mapIndexed { index, history ->
                                        OTab(
                                            title = getTimeString(history.time, "dd/MM/yyyy"),
                                            selected = index == pagerState.currentPage
                                        ) {
                                            coroutineScope.launch {
                                                pagerState.animateScrollToPage(index, 0F)
                                            }
                                        }
                                    }
                                }
                                HorizontalPager(state = pagerState) {
                                    val bounds = LatLngBounds.builder().let { builder ->
                                        for (h in state.history!![it].history) {
                                            builder.include(LatLng(h.latitude, h.longitude))
                                        }
                                        builder.build()
                                    }

                                    val camState = rememberCameraPositionState()
                                    LaunchedEffect(it) {
                                        camState.animate(
                                            update = CameraUpdateFactory.newLatLngBounds(
                                                bounds, 20
                                            ),
                                            durationMs = 500
                                        )
                                    }
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .verticalScroll(
                                                rememberScrollState()
                                            )
                                            .padding(horizontal = 16.dp)
                                    ) {
                                        var selectedPosition by remember {
                                            mutableStateOf<OHourlyHistory?>(null)
                                        }
                                        ExposureChart(
                                            history = state.history!![it].history,
                                            modifier = Modifier.padding(top = 32.dp, bottom = 32.dp)
                                        ) { history ->
                                            coroutineScope.launch {
                                                if (selectedPosition == history) {
                                                    selectedPosition = null
                                                    camState.animate(
                                                        update = CameraUpdateFactory.newLatLngBounds(
                                                            bounds, 20
                                                        ),
                                                        durationMs = 500
                                                    )
                                                } else {
                                                    selectedPosition = history
                                                    camState.animate(
                                                        update = CameraUpdateFactory.newLatLngZoom(
                                                            LatLng(
                                                                history.latitude,
                                                                history.longitude
                                                            ), 20F
                                                        ),
                                                        durationMs = 500
                                                    )
                                                }
                                            }
                                        }
                                        MovingHistory(
                                            modifier = Modifier.padding(bottom = 32.dp),
                                            history = state.history!![it].history,
                                            camState = camState,
                                            selectedPosition = selectedPosition
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun ExposureChart(
        modifier: Modifier = Modifier,
        history: List<OHourlyHistory>,
        onDataClick: (OHourlyHistory) -> Unit
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            Text(
                text = stringResource(R.string.today_exposure_chart),
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            OBarChart(
                data = OBarChartData(
                    maxYValue = 500.0,
                    barsData = history.map {
                        OBarChartData.OBarData(
                            label = "${getTimeString(it.time, "HH")}h",
                            value = it.aqi.toDouble(),
                            color = getAQIColor(it.aqi),
                            onClick = { onDataClick(it) }
                        )
                    }
                )
            )
        }
    }

    @Composable
    fun MovingHistory(
        modifier: Modifier = Modifier,
        history: List<OHourlyHistory>,
        camState: CameraPositionState,
        selectedPosition: OHourlyHistory? = null,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            Text(
                text = stringResource(R.string.moving_history),
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                val bounds by remember {
                    mutableStateOf(
                        calculateBoundingBox(
                            history.map {
                                LatLng(
                                    it.latitude,
                                    it.longitude
                                )
                            }
                        )
                    )
                }
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = camState,
                    properties = MapProperties(
                        isBuildingEnabled = true,
                        isIndoorEnabled = false,
                        isMyLocationEnabled = false,
                        isTrafficEnabled = false,
                        latLngBoundsForCameraTarget = if (bounds != null) LatLngBounds(
                            bounds!!.first,
                            bounds!!.second
                        ) else null,
                        maxZoomPreference = 15f,
                    )
                ) {
                    Polyline(
                        points = history.map { LatLng(it.latitude, it.longitude) },
                        clickable = false,
                        color = OxygenColors.mainColor,
                        width = 3f,
                        jointType = JointType.ROUND
                    )
                    if (selectedPosition != null) {
                        Marker(
                            state = MarkerState(
                                position = LatLng(
                                    selectedPosition.latitude,
                                    selectedPosition.longitude
                                )
                            ),
                            draggable = false,
                            title = "${
                                getTimeString(
                                    selectedPosition.time,
                                    "HH"
                                )
                            }h, ${selectedPosition.aqi}",

                            )
                    }
                }
            }
        }
    }

    private fun calculateBoundingBox(coordinates: List<LatLng>): Pair<LatLng, LatLng>? {
        if (coordinates.isEmpty()) {
            return null
        }

        var minLat = Double.MAX_VALUE
        var maxLat = Double.MIN_VALUE
        var minLng = Double.MAX_VALUE
        var maxLng = Double.MIN_VALUE

        for (coordinate in coordinates) {
            minLat = minOf(minLat, coordinate.latitude)
            maxLat = maxOf(maxLat, coordinate.latitude)
            minLng = minOf(minLng, coordinate.longitude)
            maxLng = maxOf(maxLng, coordinate.longitude)
        }

        val southwest = LatLng(minLat, minLng)
        val northeast = LatLng(maxLat, maxLng)

        return Pair(southwest, northeast)
    }

    private fun calculateCenter(southwest: LatLng, northeast: LatLng): LatLng {
        val centerLat = (southwest.latitude + northeast.latitude) / 2
        val centerLng = (southwest.longitude + northeast.longitude) / 2

        return LatLng(centerLat, centerLng)
    }

    private fun calculateZoomLevel(southwest: LatLng, northeast: LatLng): Float {
        val globeWidth = 256.0
        val zoomMax = 8.0

        val latDiff = Math.toRadians(northeast.latitude - southwest.latitude)
        val lngDiff = Math.toRadians(northeast.longitude - southwest.longitude)

        val latFraction = (cos(Math.toRadians(southwest.latitude)) +
                cos(Math.toRadians(northeast.latitude))) / 2

        val lngFraction = (lngDiff + 2 * Math.PI) / (2 * Math.PI)

        val latZoom = ln(globeWidth / latDiff / latFraction) / ln(2.0)
        val lngZoom = ln(globeWidth / lngDiff / lngFraction) / ln(2.0)

        val zoom = minOf(latZoom, lngZoom, zoomMax)

        return zoom.toFloat()
    }
}