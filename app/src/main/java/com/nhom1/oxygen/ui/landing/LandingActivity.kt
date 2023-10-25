package com.nhom1.oxygen.ui.landing

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.nhom1.oxygen.R
import kotlinx.coroutines.launch
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

class LandingActivity : ComponentActivity() {
    private lateinit var viewModel: LandingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        viewModel = ViewModelProvider(this)[LandingViewModel::class.java]
        setContent {
            LandingView()
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun LandingView() {
        val scope = rememberCoroutineScope()
        val pagerState = rememberPagerState()
        HorizontalPager(
            pageCount = 3,
            state = pagerState
        ) {
            when (it) {
                0 -> LandingIntroduction {
                    scope.launch {
                        pagerState.animateScrollToPage(1, 0F)
                    }
                }
                1 -> LandingLocationPermission()
                2 -> LandingNotificationPermission()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun LandingIntroduction(onContinue: () -> Unit) {
        Scaffold(
            modifier = Modifier.safeDrawingPadding(),
            bottomBar = {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        modifier = Modifier.align(Alignment.Center),
                        onClick = onContinue
                    ) {
                        Text(text = stringResource(R.string.text_continue))
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

    @Composable
    private fun LandingLocationPermission() {
        Text(text = "localtop", modifier = Modifier.fillMaxSize())
    }

    @Composable
    private fun LandingNotificationPermission() {
        Text(text = "noti", modifier = Modifier.fillMaxSize())
    }
}