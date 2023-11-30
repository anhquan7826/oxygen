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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.CameraUpdateFactory.newLatLngBounds
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
import com.google.maps.android.compose.rememberMarkerState
import com.nhom1.oxygen.R
import com.nhom1.oxygen.common.composables.OAppBar
import com.nhom1.oxygen.common.composables.OBarChart
import com.nhom1.oxygen.common.composables.OBarChartData
import com.nhom1.oxygen.common.composables.OError
import com.nhom1.oxygen.common.composables.OLoading
import com.nhom1.oxygen.common.composables.OTab
import com.nhom1.oxygen.common.composables.OTabRow
import com.nhom1.oxygen.common.constants.OxygenColors
import com.nhom1.oxygen.common.theme.OxygenTheme
import com.nhom1.oxygen.data.model.history.OHourlyHistory
import com.nhom1.oxygen.utils.bitmapDescriptor
import com.nhom1.oxygen.utils.constants.LoadState
import com.nhom1.oxygen.utils.debugLog
import com.nhom1.oxygen.utils.getAQIColor
import com.nhom1.oxygen.utils.getTimeString
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
                                HorizontalPager(
                                    state = pagerState,
                                    key = { state.history!![it].time }
                                ) {
                                    val bounds = rememberSaveable(pagerState.currentPage) {
                                        LatLngBounds.builder().let { builder ->
                                            for (h in state.history!![pagerState.currentPage].history.filterNotNull()) {
                                                builder.include(LatLng(h.latitude, h.longitude))
                                            }
                                            builder.build()
                                        }
                                    }

                                    val camState =
                                        rememberCameraPositionState(key = pagerState.currentPage.toString())
                                    if (!pagerState.isScrollInProgress && it == pagerState.currentPage) {
                                        LaunchedEffect(true) {
                                            camState.animate(
                                                update = newLatLngBounds(
                                                    bounds, 20
                                                ),
                                                durationMs = 1000
                                            )
                                        }
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
                                            history = state.history!![pagerState.currentPage].history,
                                            modifier = Modifier.padding(top = 32.dp, bottom = 32.dp)
                                        ) { history ->
                                            coroutineScope.launch {
                                                if (selectedPosition == history) {
                                                    selectedPosition = null
                                                    camState.animate(
                                                        update = newLatLngBounds(
                                                            bounds, 20
                                                        ),
                                                        durationMs = 1000
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
                                                        durationMs = 1000
                                                    )
                                                }
                                            }
                                        }
                                        MovingHistory(
                                            modifier = Modifier.padding(bottom = 32.dp),
                                            history = state.history!![pagerState.currentPage].history.filterNotNull(),
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
        history: List<OHourlyHistory?>,
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
                    barsData = history.mapIndexed { index, h ->
                        OBarChartData.OBarData(
                            label = "${index}h",
                            value = h?.aqi?.toDouble() ?: -1.0,
                            color = h?.let { getAQIColor(h.aqi) } ?: Color.Gray,
                            onClick = { h?.let { onDataClick(h) } }
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
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = camState,
                    properties = MapProperties(
                        isBuildingEnabled = true,
                        isIndoorEnabled = false,
                        isMyLocationEnabled = false,
                        isTrafficEnabled = false,
                        maxZoomPreference = 15f,
                    ),
                ) {
                    Polyline(
                        points = history.map { LatLng(it.latitude, it.longitude) },
                        clickable = false,
                        color = OxygenColors.mainColor,
                        width = 3f,
                        jointType = JointType.ROUND
                    )
                    for (h in history) {
                        Marker(
                            state = rememberMarkerState(position = LatLng(h.latitude, h.longitude)),
                            draggable = false,
                            icon = bitmapDescriptor(
                                this@HistoryActivity, R.drawable.radio_button_unchecked
                            ),
                            anchor = Offset(0.5f, 0.5f)
                        )
                    }
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
}