package com.nhom1.oxygen.ui.landing

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.snapping.SnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.nhom1.oxygen.R
import com.nhom1.oxygen.common.theme.OxygenTheme
import com.nhom1.oxygen.common.widgets.OButtonPrimary
import com.nhom1.oxygen.common.widgets.OButtonSecondary
import com.nhom1.oxygen.ui.home.HomeActivity
import com.nhom1.oxygen.utils.ConfigUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class LandingActivity : ComponentActivity() {
    private lateinit var viewModel: LandingViewModel
    private lateinit var coroutineScope: CoroutineScope

    @OptIn(ExperimentalFoundationApi::class)
    private lateinit var pagerState: PagerState

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        viewModel = ViewModelProvider(this)[LandingViewModel::class.java]
        setContent {
            coroutineScope = rememberCoroutineScope()
            pagerState = rememberPagerState()
            OxygenTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    LandingView()
                }
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun LandingView() {
        HorizontalPager(
            pageCount = 3,
            state = pagerState,
            userScrollEnabled = false
        ) {
            when (it) {
                0 -> LandingIntroduction()
                1 -> LandingLocationPermission()
                2 -> LandingNotificationPermission()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
    @Composable
    private fun LandingIntroduction() {
        Scaffold(
            modifier = Modifier.safeDrawingPadding(),
            contentWindowInsets = WindowInsets(
                left = 16.dp,
                right = 16.dp
            ),
            bottomBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            bottom = 16.dp
                        )
                ) {
                    OButtonPrimary(
                        modifier = Modifier.align(Alignment.Center),
                        text = stringResource(R.string.text_continue),
                        minWidth = 128.dp,
                    ) {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(1, 0F)
                        }
                    }
                }
            }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                Image(
                    painterResource(id = R.drawable.fresh_air),
                    contentDescription = null,
                    modifier = Modifier
                        .width(128.dp)
                        .padding(bottom = 32.dp)
                )
                Text(
                    text = stringResource(R.string.oxygen),
                    textAlign = TextAlign.Center,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 64.dp)
                )
                Text(
                    text = stringResource(R.string.landing_introduction),
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
    @Composable
    private fun LandingLocationPermission() {
        Scaffold(
            modifier = Modifier.safeDrawingPadding(),
            contentWindowInsets = WindowInsets(
                left = 16.dp,
                right = 16.dp,
                bottom = 16.dp
            ),
            bottomBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OButtonPrimary(
                        text = stringResource(R.string.grant_permission),
                        minWidth = 128.dp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        // TODO: Ask location permission
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(2, 0F)
                        }
                    }
                    OButtonSecondary(
                        text = stringResource(R.string.skip),
                        minWidth = 128.dp
                    ) {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(2, 0F)
                        }
                    }
                }
            }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                Image(
                    painterResource(id = R.drawable.location),
                    contentDescription = null,
                    modifier = Modifier
                        .width(128.dp)
                        .padding(bottom = 32.dp)
                )
                Text(
                    text = stringResource(R.string.get_local_info_permission),
                    textAlign = TextAlign.Center,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 64.dp)
                )
                Text(
                    text = stringResource(R.string.get_local_info_permission_description),
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
    @Composable
    private fun LandingNotificationPermission() {
        Scaffold(
            modifier = Modifier.safeDrawingPadding(),
            contentWindowInsets = WindowInsets(
                left = 16.dp,
                right = 16.dp,
                bottom = 16.dp
            ),
            bottomBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OButtonPrimary(
                        text = stringResource(R.string.grant_permission),
                        minWidth = 128.dp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        // TODO: Ask notification permission
                        goToHome()
                    }
                    OButtonSecondary(
                        text = stringResource(R.string.skip),
                        minWidth = 128.dp
                    ) {
                        goToHome()
                    }
                }
            }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                Image(
                    painterResource(id = R.drawable.notification),
                    contentDescription = null,
                    modifier = Modifier
                        .width(128.dp)
                        .padding(bottom = 32.dp)
                )
                Text(
                    text = stringResource(R.string.allow_notification),
                    textAlign = TextAlign.Center,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 64.dp)
                )
                Text(
                    text = stringResource(R.string.allow_notification_description),
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    private fun goToHome() {
//        ConfigUtil.firstLaunch = false
        startActivity(Intent(this@LandingActivity, HomeActivity::class.java))
        finish()
    }
}