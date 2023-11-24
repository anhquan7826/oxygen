package com.nhom1.oxygen.ui.home.composables

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.nhom1.oxygen.R
import com.nhom1.oxygen.common.composables.OAppBar
import com.nhom1.oxygen.common.composables.OError
import com.nhom1.oxygen.common.composables.OLoading
import com.nhom1.oxygen.common.composables.OOption
import com.nhom1.oxygen.ui.history.HistoryActivity
import com.nhom1.oxygen.ui.home.UserViewModel
import com.nhom1.oxygen.utils.constants.LoadState
import com.nhom1.oxygen.utils.extensions.oShadow

@Composable
fun UserComposable(viewModel: UserViewModel) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    Scaffold(
        topBar = {
            OAppBar(
                title = stringResource(id = R.string.profile),
                leading = painterResource(id = R.drawable.user_colored)
            )
        },
        containerColor = Color.White,
        contentWindowInsets = WindowInsets(
            left = 16.dp,
            right = 16.dp,
        )
    ) {
        when (state.state) {
            LoadState.LOADING -> {
                Box(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize()
                ) {
                    OLoading(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            LoadState.ERROR -> {
                Box(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize()
                ) {
                    OError(error = state.error) {
                        viewModel.load()
                    }
                }
            }

            else -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(it)
                    .padding(top = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AsyncImage(
                    model = state.userData!!.avt,
                    contentDescription = null,
                    modifier = Modifier
                        .clip(CircleShape)
                        .oShadow()
                        .size(128.dp),
                )
                Text(
                    text = "Xin chào",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(
                        top = 24.dp,
                        bottom = 8.dp,
                    )
                )
                Text(
                    text = state.userData!!.name,
                    fontSize = 32.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(
                        bottom = 32.dp
                    )
                )
                OOption(
                    leading = painterResource(id = R.drawable.history_colored),
                    title = stringResource(R.string.exposure_history),
                    modifier = Modifier.padding(
                        bottom = 16.dp
                    )
                ) {
                    context.startActivity(Intent(context, HistoryActivity::class.java))
                }
                OOption(
                    leading = painterResource(id = R.drawable.resume_colored),
                    title = stringResource(R.string.personal_info),
                    modifier = Modifier.padding(
                        bottom = 16.dp
                    )
                ) {
                    // TODO: Go to user profile
                }
                OOption(
                    leading = painterResource(id = R.drawable.medical_history_colored),
                    title = stringResource(R.string.medical_history_form),
                    modifier = Modifier.padding(
                        bottom = 16.dp
                    )
                ) {
                    // TODO: Go to history
                }
                OOption(
                    leading = painterResource(id = R.drawable.settings_colored),
                    title = stringResource(R.string.settings),
                    modifier = Modifier.padding(
                        bottom = 16.dp
                    )
                ) {
                    // TODO: Go to settings
                }
                OOption(
                    leading = painterResource(id = R.drawable.logout_colored),
                    title = stringResource(R.string.sign_out),
                    titleColor = Color.Red,
                    modifier = Modifier.padding(
                        bottom = 16.dp
                    )
                ) {
                    // TODO: Go to logout
                }
            }
        }
    }
}