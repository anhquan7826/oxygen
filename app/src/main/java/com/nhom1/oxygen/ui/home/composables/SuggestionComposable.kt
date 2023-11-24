@file:OptIn(ExperimentalMaterial3Api::class)

package com.nhom1.oxygen.ui.home.composables

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.nhom1.oxygen.R
import com.nhom1.oxygen.common.composables.OAppBar
import com.nhom1.oxygen.common.composables.OCard
import com.nhom1.oxygen.common.composables.OError
import com.nhom1.oxygen.common.composables.OLoading
import com.nhom1.oxygen.common.composables.OOverallStatusCompact
import com.nhom1.oxygen.common.composables.OTab
import com.nhom1.oxygen.common.composables.OTabRow
import com.nhom1.oxygen.data.model.article.OArticle
import com.nhom1.oxygen.data.model.weather.OWeather
import com.nhom1.oxygen.ui.home.SuggestionViewModel
import com.nhom1.oxygen.utils.constants.LoadState
import com.nhom1.oxygen.utils.extensions.oBorder
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SuggestionComposable(viewModel: SuggestionViewModel) {
    var currentTab by remember {
        mutableIntStateOf(0)
    }
    val pagerState = rememberPagerState(0, 0f) { 2 }
    val coroutineScope = rememberCoroutineScope()
    val state by viewModel.state.collectAsState()
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
            modifier = Modifier.padding(it)
        ) {
            when (state.state) {
                LoadState.LOADING -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {
                        OLoading(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }

                LoadState.ERROR -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {
                        OError(
                            modifier = Modifier.align(Alignment.Center)
                        ) {

                        }
                    }
                }

                else -> {
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
                            SuggestionPage(
                                weather = state.weather!!
                            )
                        } else {
                            ArticlePage(state.articles!!)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ArticlePage(articles: List<OArticle>) {
    val context = LocalContext.current
    LazyColumn(
        verticalArrangement = Arrangement.Top,
        contentPadding = PaddingValues(top = 32.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        items(articles) {
            ArticleTile(article = it) {
                context.startActivity(Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(it.url)
                })
            }
        }
    }
}

@Composable
fun SuggestionPage(weather: OWeather) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
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
                aqi = weather.airQuality.aqi,
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
}

@Composable
fun WarningCard(modifier: Modifier = Modifier) {
    OCard(modifier = modifier.oBorder()) {
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
            .oBorder()
    ) {
        Text(
            text = suggestion,
            fontSize = 16.sp
        )
    }
}

@Composable
fun ArticleTile(article: OArticle, onClick: () -> Unit) {
    OCard(
        contentPadding = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .oBorder()
            .clickable {
                onClick.invoke()
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = article.image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Column(
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(
                    text = article.title,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = article.preview,
                    fontSize = 12.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                val url by remember {
                    mutableStateOf(extractDomain(article.url))
                }
                url?.let { Text(text = it, fontSize = 10.sp, color = Color.Gray) }
            }
        }
    }
}

fun extractDomain(url: String): String? {
    val regex = Regex("(?<=://)(www\\.)?([\\w.-]+)")
    val matchResult = regex.find(url)
    val domainWithWww = matchResult?.groups?.get(2)?.value
    return domainWithWww?.removePrefix("www.")
}
