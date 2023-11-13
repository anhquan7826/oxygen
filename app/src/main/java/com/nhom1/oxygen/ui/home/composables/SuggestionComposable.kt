@file:OptIn(ExperimentalMaterial3Api::class)

package com.nhom1.oxygen.ui.home.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nhom1.oxygen.R
import com.nhom1.oxygen.common.composables.OAppBar
import com.nhom1.oxygen.common.composables.OCard
import com.nhom1.oxygen.common.composables.OOverallStatusCompact
import com.nhom1.oxygen.common.composables.OTab
import com.nhom1.oxygen.common.composables.OTabRow
import com.nhom1.oxygen.ui.home.SuggestionViewModel
import com.nhom1.oxygen.utils.extensions.oShadow
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SuggestionComposable(viewModel: SuggestionViewModel) {
    var currentTab by remember {
        mutableIntStateOf(0)
    }
    val pagerState = rememberPagerState(0, 0f) { 2 }
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            OAppBar(
                title = stringResource(id = R.string.news_suggestion),
                leading = painterResource(id = R.drawable.news_colored),
                withShadow = false
            )
        },
        containerColor = Color.White,
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.exclude(WindowInsets.navigationBars)
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            OTabRow(
                selectedTabIndex = currentTab,
            ) {
                OTab(
                    title = stringResource(R.string.suggestion),
                    selected = currentTab == 0
                ) {
                    currentTab = 0
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(currentTab, 0f)
                    }
                }
                OTab(
                    title = stringResource(R.string.news),
                    selected = currentTab == 1
                ) {
                    currentTab = 1
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(currentTab, 0f)
                    }
                }
            }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f),
            ) { page ->
                LaunchedEffect(pagerState.currentPage) {
                    currentTab = pagerState.currentPage
                }
                if (page == 0) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                horizontal = 16.dp
                            )
                            .verticalScroll(rememberScrollState())
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(top = 32.dp)
                                .height(
                                    intrinsicSize = IntrinsicSize.Max
                                )
                        ) {
                            OOverallStatusCompact(
                                aqi = 100,
                                modifier = Modifier
                                    .padding(end = 4.dp)
                                    .fillMaxHeight()
                                    .weight(1f),
                                fillMaxHeight = true
                            )
                            WarningCard(
                                modifier = Modifier
                                    .padding(start = 4.dp)
                                    .fillMaxHeight()
                                    .weight(1f)
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 32.dp, bottom = 16.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.suggestions_colored),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(48.dp)
                                    .padding(end = 16.dp)
                            )
                            Text(
                                text = stringResource(R.string.take_care_of_your_health),
                                fontSize = 16.sp
                            )
                        }
                        repeat(4) {
                            SuggestionText(suggestion = "Hạn chế hoạt động ngoài trời, đặc biệt vào buổi trưa.")
                        }
                    }
                } else {
                    Column(
                        verticalArrangement = Arrangement.Top
                    ) {

                    }
                }
            }
        }
    }
}

@Composable
fun WarningCard(modifier: Modifier = Modifier) {
    OCard(modifier = modifier.oShadow()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.warning),
                contentDescription = null
            )
            Text(
                text = "Không khí hiện tại không tốt cho sức khỏe. Hãy cẩn thận.",
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun SuggestionText(suggestion: String) {
    OCard(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .oShadow()
    ) {
        Text(
            text = suggestion,
            fontSize = 16.sp
        )
    }
}