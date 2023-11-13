@file:OptIn(ExperimentalMaterial3Api::class)

package com.nhom1.oxygen.ui.home.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nhom1.oxygen.R
import com.nhom1.oxygen.common.composables.OAppBar
import com.nhom1.oxygen.common.composables.OTextField
import com.nhom1.oxygen.ui.home.SearchViewModel
import com.nhom1.oxygen.utils.extensions.oBorder
import com.nhom1.oxygen.utils.extensions.oClip

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchComposable(viewModel: SearchViewModel) {
    val state by viewModel.state.collectAsState()
    Scaffold(
        topBar = {
            OAppBar(
                title = stringResource(id = R.string.search),
                leading = painterResource(id = R.drawable.search_location),
            )
        },
        contentWindowInsets = WindowInsets(
            left = 16.dp,
            right = 16.dp,
        ),
        containerColor = Color.White
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            OTextField(
                modifier = Modifier.padding(top = 32.dp, bottom = 16.dp),
                placeholder = {
                    Text(
                        text = stringResource(R.string.search_hint)
                    )
                },
                leading = {
                    Icon(
                        Icons.Rounded.Search,
                        contentDescription = null,
                    )
                },
                onValueChange = viewModel::getSearchResult
            )
            when {
                state.searchValue.isEmpty() && state.result.isEmpty() -> {
                    Box(
                        modifier = Modifier.weight(1f).fillMaxSize()
                    ) {
                        Text(
                            stringResource(R.string.empty_search),
                            fontSize = 16.sp,
                            color = Color(0xFF8D8D8D),
                            modifier = Modifier.align(Alignment.Center),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                state.searchValue.isNotEmpty() && state.result.isEmpty() -> {
                    Box(
                        modifier = Modifier.weight(1f).fillMaxSize()
                    ) {
                        Text(
                            stringResource(R.string.search_location_not_found),
                            fontSize = 16.sp,
                            color = Color(0xFF8D8D8D),
                            modifier = Modifier.align(Alignment.Center),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                else -> {
                    LazyColumn {
                        items(5) {
                            SearchResult(100, "Hà Nội", "Hà Nội, Việt Nam")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchResult(aqi: Int, result: String, location: String) {
    val colors = listOf(
        Color(0x4000E400),
        Color(0x40FFFF00),
        Color(0x40E06D2D),
        Color(0x40C51919),
        Color(0x406C07D1),
        Color(0x407E0023)
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(bottom = 16.dp)
            .oClip()
            .clickable {

            }
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    color = when {
                        (aqi in 0..50) -> colors[0]
                        (aqi in 51..100) -> colors[1]
                        (aqi in 101..150) -> colors[2]
                        (aqi in 201..300) -> colors[4]
                        (aqi in 151..200) -> colors[3]
                        else -> colors[5]
                    },
                    shape = RoundedCornerShape(12.dp)
                )
                .oBorder()
        ) {
            Text(
                text = aqi.toString(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Text(
            text = result,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
        )
        Text(
            text = location,
            fontWeight = FontWeight.Light
        )
        Icon(
            painter = painterResource(id = R.drawable.chevron_right),
            contentDescription = null
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SearchResultPreview() {
    SearchResult(100, "Hà Nội", "Hà Nội, Việt Nam")
}