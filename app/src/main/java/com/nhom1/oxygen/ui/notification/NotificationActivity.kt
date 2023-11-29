@file:OptIn(ExperimentalMaterial3Api::class)

package com.nhom1.oxygen.ui.notification

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.nhom1.oxygen.R
import com.nhom1.oxygen.common.composables.OAppBar
import com.nhom1.oxygen.common.composables.OCard
import com.nhom1.oxygen.common.composables.OError
import com.nhom1.oxygen.common.composables.OLoading
import com.nhom1.oxygen.common.theme.OxygenTheme
import com.nhom1.oxygen.data.model.notification.ONotification
import com.nhom1.oxygen.utils.constants.LoadState
import com.nhom1.oxygen.utils.getTimeString
import com.nhom1.oxygen.utils.now
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationActivity : ComponentActivity() {
    private lateinit var viewModel: NotificationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        viewModel = ViewModelProvider(this)[NotificationViewModel::class.java]
        setContent {
            OxygenTheme {
                Surface(
                    color = Color.White
                ) {
                    NotificationView()
                }
            }
        }
    }

    @Composable
    fun NotificationView() {
        val state by viewModel.state.collectAsState()
        Scaffold(
            topBar = {
                OAppBar(
                    title = stringResource(R.string.notification),
                    leading = painterResource(id = R.drawable.arrow_back),
                    onLeadingPressed = {
                        setResult(RESULT_OK, Intent().putExtra("count", state.notifications.size))
                        finish()
                    },
                    actions = listOf(
                        painterResource(id = R.drawable.add),
                        painterResource(id = R.drawable.clear_all)
                    ),
                    onActionPressed = listOf(
                        {
                            viewModel.addNotification()
                        },
                        {
                            viewModel.clearAllNotifications()
                        }
                    )
                )
            },
            containerColor = Color.White,
            contentWindowInsets = ScaffoldDefaults.contentWindowInsets.add(
                WindowInsets(
                    left = 16.dp, right = 16.dp
                )
            ),
            modifier = Modifier
                .statusBarsPadding()
                .imePadding()
        ) {
            when (state.state) {
                LoadState.LOADING -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it)
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
                            .padding(it)
                    ) {
                        OError(
                            error = state.error,
                            modifier = Modifier.align(Alignment.Center)
                        ) {
                            viewModel.load()
                        }
                    }
                }

                else -> {
                    if (state.notifications.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(it)
                        ) {
                            Text(
                                text = stringResource(R.string.no_notification),
                                color = Color.Gray,
                                modifier = Modifier.align(Alignment.Center),
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .padding(it)
                                .padding(top = 32.dp, bottom = 16.dp)
                        ) {
                            items(
                                state.notifications,
                                key = { n -> n.id }
                            ) { notification ->
                                NotificationTile(
                                    notification = notification,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }


    @Composable
    fun NotificationTile(
        modifier: Modifier = Modifier,
        notification: ONotification,
    ) {
        val dismissState = rememberDismissState(
            confirmValueChange = { dismissValue ->
                if (dismissValue == DismissValue.Default) {
                    false
                } else {
                    viewModel.clearNotification(notification)
                    true
                }
            },
        )
        SwipeToDismiss(
            modifier = modifier,
            state = dismissState,
            directions = setOf(
                DismissDirection.EndToStart
            ),
            background = {
                val backgroundColor by animateColorAsState(
                    when (dismissState.targetValue) {
                        DismissValue.DismissedToStart -> Color.Red.copy(alpha = 0.8f)
                        else -> Color.White
                    }, label = ""
                )

                val iconScale by animateFloatAsState(
                    targetValue = if (dismissState.targetValue == DismissValue.DismissedToStart) 1.3f else 0.5f,
                    label = ""
                )

                Box(
                    Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp))
                        .background(color = backgroundColor)
                        .padding(end = 16.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Icon(
                        modifier = Modifier.scale(iconScale),
                        painter = painterResource(id = R.drawable.delete),
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            },
            dismissContent = {
                OCard(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = 12.dp
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = if (notification.type == ONotification.TYPE_SUGGESTION) painterResource(
                                id = R.drawable.information
                            ) else painterResource(
                                id = R.drawable.warning
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .size(32.dp)
                        )
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = if (notification.type == ONotification.TYPE_SUGGESTION) stringResource(
                                    id = R.string.suggestion
                                ) else stringResource(
                                    id = R.string.warning
                                ),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            Text(
                                text = notification.message
                            )
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            val date: String
                            val now = getTimeString(now(), "dd/MM/yyyy")
                            val fromNotification = getTimeString(notification.time, "dd/MM/YYYY")
                            date = if (now == fromNotification) {
                                stringResource(R.string.today)
                            } else {
                                fromNotification
                            }
                            Text(
                                text = date,
                                fontSize = 10.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Center,
                            )
                            Text(
                                text = getTimeString(notification.time),
                                fontSize = 10.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
            }
        )
    }
}