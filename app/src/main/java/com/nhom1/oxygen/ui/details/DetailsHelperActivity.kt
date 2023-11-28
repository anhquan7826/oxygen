package com.nhom1.oxygen.ui.details

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.nhom1.oxygen.R
import com.nhom1.oxygen.common.composables.OAppBar
import com.nhom1.oxygen.common.theme.OxygenTheme

class DetailsHelperActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            OxygenTheme {
                Surface(
                    color = Color.White
                ) {
                    DetailsHelperView()
                }
            }
        }
    }

    @Composable
    fun DetailsHelperView() {
        Scaffold(
            topBar = {
                OAppBar(
                    title = stringResource(R.string.information),
                    leading = painterResource(id = R.drawable.arrow_back),
                    onLeadingPressed = {
                        finish()
                    }
                )
            },
            containerColor = Color.White,
            contentWindowInsets = WindowInsets(left = 16.dp, right = 16.dp),
            modifier = Modifier.systemBarsPadding()
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(it)
                    .padding(top = 32.dp)
            ) {
                InfoTitle(title = stringResource(R.string.aqi_index_title))
                InfoContent(content = stringResource(R.string.aqi_index_info))
                InfoTitle(title = stringResource(R.string.no2_index_title))
                InfoContent(content = stringResource(R.string.no2_index_info))
                InfoTitle(title = stringResource(R.string.pm_index_title))
                InfoContent(content = stringResource(R.string.pm_index_info))
            }
        }
    }

    @Composable
    fun InfoTitle(title: String) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }

    @Composable
    fun InfoContent(content: String) {
        Text(
            text = content,
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(bottom = 32.dp)
        )
    }
}