@file:OptIn(ExperimentalMaterial3Api::class)

package com.nhom1.oxygen.ui.home.composables

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nhom1.oxygen.R
import com.nhom1.oxygen.common.composables.OAppBar
import com.nhom1.oxygen.common.composables.OCard
import com.nhom1.oxygen.common.composables.OTextField
import com.nhom1.oxygen.ui.details.DetailsActivity
import com.nhom1.oxygen.ui.home.SearchViewModel
import com.nhom1.oxygen.utils.extensions.oClip
import com.nhom1.oxygen.utils.toJson

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchComposable(viewModel: SearchViewModel) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
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
        containerColor = Color.White,
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
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                onValueChange = viewModel::onQueryChanged,
            )
            when {
                state.searchValue.isEmpty() && state.searchHistory.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
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

                state.searchValue.isEmpty() && state.searchHistory.isNotEmpty() -> {
                    Column {
                        Text(
                            text = stringResource(R.string.recent_search),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(bottom = 8.dp, start = 8.dp)
                        )
                        LazyColumn {
                            for (result in state.searchHistory) {
                                item {
                                    SearchResult(
                                        result.name ?: result.district,
                                        "${result.province}, ${result.country}"
                                    ) {
                                        context.startActivity(
                                            Intent(context, DetailsActivity::class.java).putExtra(
                                                "location", toJson(result)
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                state.searchValue.isNotEmpty() && state.result.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
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
                        for (result in state.result) {
                            item {
                                SearchResult(
                                    result.name ?: result.district,
                                    "${result.province}, ${result.country}"
                                ) {
                                    viewModel.saveLocation(result)
                                    context.startActivity(
                                        Intent(context, DetailsActivity::class.java).putExtra(
                                            "location", toJson(result)
                                        )
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
fun SearchResult(result: String, location: String, onClick: () -> Unit) {
    OCard(
        modifier = Modifier
            .padding(bottom = 8.dp)
            .oClip(),
        contentPadding = 8.dp,
        onClick = onClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = result,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .weight(1f)
            )
            Text(
                text = location,
                fontWeight = FontWeight.Light,
                fontSize = 12.sp
            )
            Icon(
                painter = painterResource(id = R.drawable.chevron_right),
                contentDescription = null
            )
        }
    }
}