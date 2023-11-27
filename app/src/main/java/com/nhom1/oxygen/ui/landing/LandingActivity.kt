@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.nhom1.oxygen.ui.landing

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.nhom1.oxygen.MainService
import com.nhom1.oxygen.R
import com.nhom1.oxygen.common.composables.OButtonPrimary
import com.nhom1.oxygen.common.theme.OxygenTheme
import com.nhom1.oxygen.repository.UserRepository
import com.nhom1.oxygen.ui.home.HomeActivity
import com.nhom1.oxygen.ui.login.LoginActivity
import com.nhom1.oxygen.utils.ConfigUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LandingActivity : ComponentActivity() {
    @Inject
    lateinit var userRepository: UserRepository

    private lateinit var coroutineScope: CoroutineScope

    private lateinit var pagerState: PagerState

    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) toNextPage()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            coroutineScope = rememberCoroutineScope()
            pagerState = rememberPagerState() { 3 }
            OxygenTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    LandingView()
                }
            }
        }
    }

    @Composable
    private fun LandingView() {
        HorizontalPager(
            modifier = Modifier,
            state = pagerState,
            userScrollEnabled = false,
        ) {
            when (it) {
                0 -> LandingIntroduction()
                1 -> LandingLocationPermission()
                2 -> LandingNotificationPermission()
            }
        }
    }

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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                Image(
                    painterResource(id = R.drawable.fresh_air),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(
                            top = 150.dp,
                            bottom = 32.dp
                        )
                        .width(128.dp)
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
                        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                }
            }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                Image(
                    painterResource(id = R.drawable.location),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(
                            top = 150.dp,
                            bottom = 32.dp
                        )
                        .width(128.dp)
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
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        } else {
                            toNextPage()
                        }
                    }
                }
            }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                Image(
                    painterResource(id = R.drawable.notification),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(
                            top = 150.dp,
                            bottom = 32.dp
                        )
                        .width(128.dp)
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

    private fun toNextPage() {
        if (pagerState.currentPage < 2) {
            coroutineScope.launch {
                pagerState.animateScrollToPage(pagerState.currentPage + 1, 0F)
            }
        } else {
            ConfigUtil.firstLaunch = false
            MainService.startService(this)
            if (!userRepository.isSignedIn()) {
                startActivity(Intent(this@LandingActivity, LoginActivity::class.java))
            } else {
                startActivity(Intent(this@LandingActivity, HomeActivity::class.java))
            }
            finish()
        }
    }
}