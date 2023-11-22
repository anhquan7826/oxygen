@file:OptIn(ExperimentalFoundationApi::class)

package com.nhom1.oxygen.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.nhom1.oxygen.MainService
import com.nhom1.oxygen.R
import com.nhom1.oxygen.common.composables.AutoSizeText
import com.nhom1.oxygen.common.theme.OxygenTheme
import com.nhom1.oxygen.ui.home.composables.MapComposable
import com.nhom1.oxygen.ui.home.composables.OverviewComposable
import com.nhom1.oxygen.ui.home.composables.SearchComposable
import com.nhom1.oxygen.ui.home.composables.SuggestionComposable
import com.nhom1.oxygen.ui.home.composables.UserComposable
import com.nhom1.oxygen.utils.extensions.oShadow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {
    private lateinit var overviewViewModel: OverviewViewModel
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var suggestionViewModel: SuggestionViewModel
    private lateinit var userViewModel: UserViewModel

    private lateinit var coroutineScope: CoroutineScope

    private val items = mapOf(
        0 to "overview", 1 to "search", 2 to "map", 3 to "suggestion", 4 to "user"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        overviewViewModel = ViewModelProvider(this)[OverviewViewModel::class.java]
        searchViewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        suggestionViewModel = ViewModelProvider(this)[SuggestionViewModel::class.java]
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
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

    override fun onStart() {
        super.onStart()
        startForegroundService(Intent(this, MainService::class.java))
    }

    @Composable
    private fun HomeView() {
        val pagerState = rememberPagerState { items.size }
        var currentPage by remember {
            mutableIntStateOf(0)
        }
        Scaffold(
            bottomBar = {
                NavigationBar(
                    containerColor = Color.White,
                    modifier = Modifier.oShadow(),
                ) {
                    for (item in items) {
                        NavigationBarItem(
                            alwaysShowLabel = false,
                            selected = currentPage == item.key,
                            onClick = {
                                currentPage = item.key
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(currentPage, 0F)
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = Color.White
                            ),
                            icon = {
                                Icon(
                                    if (currentPage == item.key) when (item.value) {
                                        "overview" -> painterResource(id = R.drawable.house)
                                        "search" -> painterResource(id = R.drawable.search_location)
                                        "map" -> painterResource(id = R.drawable.place_colored)
                                        "suggestion" -> painterResource(id = R.drawable.news_colored)
                                        "user" -> painterResource(id = R.drawable.user_colored)
                                        else -> painterResource(id = 0)
                                    }
                                    else when (item.value) {
                                        "overview" -> painterResource(id = R.drawable.house_outlined)
                                        "search" -> painterResource(id = R.drawable.search_location_monochrome)
                                        "map" -> painterResource(id = R.drawable.place_outlined)
                                        "suggestion" -> painterResource(id = R.drawable.news_outlined)
                                        "user" -> painterResource(id = R.drawable.user_outlined)
                                        else -> painterResource(id = 0)
                                    },
                                    contentDescription = null,
                                    tint = Color.Unspecified,
                                    modifier = Modifier.width(24.dp)
                                )
                            },
                            label = {
                                AutoSizeText(
                                    text = when (item.value) {
                                        "overview" -> stringResource(R.string.overview)
                                        "search" -> stringResource(R.string.search)
                                        "map" -> stringResource(R.string.map)
                                        "suggestion" -> stringResource(R.string.news_suggestion)
                                        "user" -> stringResource(R.string.profile)
                                        else -> ""
                                    },
                                    textAlign = TextAlign.Center,
                                    maxTextSize = 12.sp,
                                )
                            }
                        )
                    }
                }
            }, containerColor = Color.White
        ) {
            HorizontalPager(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                state = pagerState,
                userScrollEnabled = false,
            ) { currentPage ->
                when (currentPage) {
                    0 -> OverviewComposable(overviewViewModel)
                    1 -> SearchComposable(searchViewModel)
                    2 -> MapComposable()
                    3 -> SuggestionComposable(suggestionViewModel)
                    4 -> UserComposable(userViewModel)
                }
            }
        }
    }
}