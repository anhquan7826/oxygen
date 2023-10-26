package com.nhom1.oxygen.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.nhom1.oxygen.R
import com.nhom1.oxygen.common.theme.OxygenTheme
import com.nhom1.oxygen.ui.home.composables.MapComposable
import com.nhom1.oxygen.ui.home.composables.OverviewComposable
import com.nhom1.oxygen.ui.home.composables.SearchComposable
import com.nhom1.oxygen.ui.home.composables.UserComposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration

class HomeActivity : ComponentActivity() {
    private lateinit var viewModel: HomeViewModel
    private lateinit var coroutineScope: CoroutineScope

    private val items = mapOf(
        0 to "overview",
        1 to "search",
        2 to "map",
        3 to "user"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        Configuration.getInstance().load(this, getSharedPreferences("osmdroid", MODE_PRIVATE))
        setContent {
            coroutineScope = rememberCoroutineScope()
            OxygenTheme {
                Surface {
                    HomeView()
                }
            }
        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
    @Composable
    private fun HomeView() {
        val pagerState = rememberPagerState()
        var currentPage by rememberSaveable {
            mutableStateOf(0)
        }
        Scaffold(
            bottomBar = {
                NavigationBar(
                    containerColor = Color.White,
                ) {
                    for (item in items) {
                        NavigationBarItem(
                            alwaysShowLabel = true,
                            selected = currentPage == item.key,
                            onClick = {
                                coroutineScope.launch {
                                    currentPage = item.key
                                    pagerState.animateScrollToPage(currentPage, 0F)
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = Color.White
                            ),
                            icon = {
                                Icon(
                                    if (currentPage == item.key)
                                        when (item.value) {
                                            "overview" -> painterResource(id = R.drawable.house)
                                            "search" -> painterResource(id = R.drawable.search_location)
                                            "map" -> painterResource(id = R.drawable.place_colored)
                                            "user" -> painterResource(id = R.drawable.user_colored)
                                            else -> painterResource(id = 0)
                                        }
                                    else
                                        when (item.value) {
                                            "overview" -> painterResource(id = R.drawable.house_outlined)
                                            "search" -> painterResource(id = R.drawable.search_location_monochrome)
                                            "map" -> painterResource(id = R.drawable.place_outlined)
                                            "user" -> painterResource(id = R.drawable.user_outlined)
                                            else -> painterResource(id = 0)
                                        },
                                    contentDescription = null,
                                    tint = Color.Unspecified,
                                    modifier = Modifier.width(24.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = when (item.value) {
                                        "overview" -> stringResource(R.string.overview)
                                        "search" -> stringResource(R.string.search)
                                        "map" -> stringResource(R.string.map)
                                        "user" -> "username"
                                        else -> ""
                                    }
                                )
                            }
                        )
                    }
                }
            },
            contentWindowInsets = WindowInsets.safeContent
        ) {
            HorizontalPager (
                pageCount = items.size,
                state = pagerState,
                userScrollEnabled = false,
                modifier = Modifier
                    .fillMaxSize()
                    .safeDrawingPadding()
            ) {
                when (currentPage) {
                    0 -> OverviewComposable()
                    1 -> SearchComposable()
                    2 -> MapComposable()
                    3 -> UserComposable()
                }
            }
        }
    }
}