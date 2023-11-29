@file:OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)

package com.nhom1.oxygen.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.nhom1.oxygen.R
import com.nhom1.oxygen.common.composables.AutoSizeText
import com.nhom1.oxygen.common.theme.OxygenTheme
import com.nhom1.oxygen.ui.home.composables.OverviewComposable
import com.nhom1.oxygen.ui.home.composables.SearchComposable
import com.nhom1.oxygen.ui.home.composables.SuggestionComposable
import com.nhom1.oxygen.ui.home.composables.UserComposable
import com.nhom1.oxygen.utils.extensions.oShadow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {
    inner class HomePageController(
        val pagerState: PagerState,
        private val keyboardController: SoftwareKeyboardController? = null
    ) {
        private val _currentPage = MutableStateFlow(0)
        val currentPage = _currentPage.asStateFlow()

        fun goto(page: Int) {
            keyboardController?.hide()
            _currentPage.update { page }
            coroutineScope.launch {
                pagerState.animateScrollToPage(page, 0F)
            }
        }
    }

    private val items = mapOf(
        0 to "overview", 1 to "search", 2 to "suggestion", 3 to "user"
    )
    private lateinit var homeController: HomePageController

    private lateinit var overviewViewModel: OverviewViewModel
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var suggestionViewModel: SuggestionViewModel
    private lateinit var userViewModel: UserViewModel

    private lateinit var coroutineScope: CoroutineScope

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        overviewViewModel = ViewModelProvider(this)[OverviewViewModel::class.java]
        searchViewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        suggestionViewModel = ViewModelProvider(this)[SuggestionViewModel::class.java]
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        setContent {
            coroutineScope = rememberCoroutineScope()
            homeController = HomePageController(
                rememberPagerState { items.size },
                LocalSoftwareKeyboardController.current
            )
            OxygenTheme {
                Surface {
                    HomeView()
                }
            }
        }
    }

    private fun reloadData() {
        overviewViewModel.load()
        suggestionViewModel.load()
        userViewModel.load()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (intent.hasExtra("refresh")) {
            if (intent.getBooleanExtra("refresh", false)) {
                reloadData()
            }
        }
        if (intent.hasExtra("goToSuggestion")) {
            if (intent.getBooleanExtra("goToSuggestion", false)) {
                homeController.goto(2)
            }
        }
    }

    @Composable
    private fun HomeView() {
        val page by homeController.currentPage.collectAsState()
        Scaffold(
            bottomBar = {
                NavigationBar(
                    containerColor = Color.White,
                    modifier = Modifier.oShadow(),
                ) {
                    for (item in items) {
                        NavigationBarItem(
                            alwaysShowLabel = false,
                            selected = page == item.key,
                            onClick = {
                                homeController.goto(item.key)
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = Color.White
                            ),
                            icon = {
                                Icon(
                                    if (page == item.key) when (item.value) {
                                        "overview" -> painterResource(id = R.drawable.house)
                                        "search" -> painterResource(id = R.drawable.search_location)
                                        "suggestion" -> painterResource(id = R.drawable.news_colored)
                                        "user" -> painterResource(id = R.drawable.user_colored)
                                        else -> painterResource(id = 0)
                                    }
                                    else when (item.value) {
                                        "overview" -> painterResource(id = R.drawable.house_outlined)
                                        "search" -> painterResource(id = R.drawable.search_location_monochrome)
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
            },
            containerColor = Color.White,
            modifier = Modifier.imePadding(),
        ) {
            HorizontalPager(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                state = homeController.pagerState,
                userScrollEnabled = false,
            ) { currentPage ->
                when (currentPage) {
                    0 -> OverviewComposable(overviewViewModel, homeController)
                    1 -> SearchComposable(searchViewModel)
                    2 -> SuggestionComposable(suggestionViewModel)
                    3 -> UserComposable(userViewModel)
                }
            }
        }
    }
}