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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.nhom1.oxygen.R
import com.nhom1.oxygen.common.composables.OAppBar
import com.nhom1.oxygen.common.composables.OBarChart
import com.nhom1.oxygen.common.composables.OBarChartData
import com.nhom1.oxygen.common.composables.OError
import com.nhom1.oxygen.common.composables.OLoading
import com.nhom1.oxygen.common.composables.OTab
import com.nhom1.oxygen.common.composables.OTabRow
import com.nhom1.oxygen.common.constants.getAQIColor
import com.nhom1.oxygen.common.theme.OxygenTheme
import com.nhom1.oxygen.data.model.history.OHourlyHistory
import com.nhom1.oxygen.utils.constants.LoadState
import com.nhom1.oxygen.utils.getTimeString
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.osmdroid.api.IMapController
import org.osmdroid.tileprovider.MapTileProviderBasic
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polyline

@AndroidEntryPoint
class HistoryActivity : ComponentActivity() {
    private lateinit var viewModel: HistoryViewModel
    private lateinit var mapController: IMapController

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
                    withShadow = false,
                    onLeadingPressed = {
                        finish()
                    }
                )
            },
            containerColor = Color.White,
            modifier = Modifier.statusBarsPadding()
        ) { padding ->
            Box(
                modifier = Modifier.padding(padding)
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
                        val pagerState = rememberPagerState { state.history!!.size }
                        val coroutineScope = rememberCoroutineScope()
                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            OTabRow(
                                selectedTabIndex = pagerState.currentPage,
                                scrollable = true,
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
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .verticalScroll(
                                            rememberScrollState()
                                        )
                                        .padding(horizontal = 16.dp)
                                ) {
                                    ExposureChart(
                                        history = state.history!![it].history,
                                        modifier = Modifier.padding(top = 32.dp, bottom = 32.dp)
                                    )
                                    MovingHistory(
                                        modifier = Modifier.padding(bottom = 32.dp),
                                        history = state.history!![it].history
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
    fun ExposureChart(modifier: Modifier = Modifier, history: List<OHourlyHistory>) {
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
                            color = getAQIColor(it.aqi)
                        ) {
                            mapController.apply {
                                animateTo(GeoPoint(it.latitude, it.longitude))
                                setZoom(17.0)
                            }
                        }
                    }
                )
            )
        }
    }

    @Composable
    fun MovingHistory(modifier: Modifier = Modifier, history: List<OHourlyHistory>) {
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
                AndroidView(
                    factory = {
                        val line = Polyline().apply {
                            setPoints(
                                history.map { h ->
                                    GeoPoint(h.latitude, h.longitude)
                                }
                            )
                            this.outlinePaint.apply {
                                strokeWidth = 2f
                                strokeJoin = android.graphics.Paint.Join.ROUND
                                this.color = Color.Blue.toArgb()
                            }
                        }
                        MapView(
                            it,
                            MapTileProviderBasic(it)
                        ).apply {
                            setTileSource(TileSourceFactory.MAPNIK)
                            zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
                            zoomController.setZoomInEnabled(true)
                            zoomController.setZoomOutEnabled(true)
                            mapController = controller
                            overlays.add(line)
                            zoomToBoundingBox(line.bounds, true)
                            minZoomLevel = 5.0
                            maxZoomLevel = 20.0
                        }
                    }
                )
            }
        }
    }
}