package com.nhom1.oxygen.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImage
import com.nhom1.oxygen.R
import com.nhom1.oxygen.common.composables.OButton
import com.nhom1.oxygen.common.composables.OLoading
import com.nhom1.oxygen.common.theme.OxygenTheme
import com.nhom1.oxygen.repository.UserRepository
import com.nhom1.oxygen.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {
    @Inject
    lateinit var userRepository: UserRepository

    private lateinit var viewModel: LoginViewModel
    private lateinit var signInLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        signInLauncher =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult(),
            ) {
                viewModel.onSignInResult(this, it)
            }

        setContent {
            OxygenTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    LoginView()
                }
            }
        }
    }

    @Preview(showSystemUi = true)
    @Composable
    fun LoginView() {
        val scope = rememberCoroutineScope()
        val loginState by viewModel.loginState.collectAsState()
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.fresh_air),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 150.dp)
                    .size(128.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(
                    top = 64.dp
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.login_colored),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(32.dp)
                )
                Text(
                    text = stringResource(R.string.login_to_oxygen),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
            if (loginState.state == 0 || loginState.state == 3) {
                OButton(
                    text = stringResource(R.string.login_with_google),
                    leading = painterResource(id = R.drawable.google),
                    maxWidth = 256.dp,
                    modifier = Modifier.padding(top = 64.dp)
                ) {
                    signInLauncher.launch(viewModel.getSignInIntent(this@LoginActivity))
                }
            }
            if (loginState.state == 1) {
                OLoading(
                    modifier = Modifier
                        .padding(top = 64.dp)
                        .size(64.dp)
                )
            }
            if (loginState.state == 2) {
                AsyncImage(
                    model = loginState.profilePicture!!,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 64.dp)
                        .size(64.dp)
                        .clip(CircleShape)
                )
                Text(
                    text = "Chào mừng ${loginState.username!!}",
                    modifier = Modifier.padding(top = 16.dp)
                )
                LaunchedEffect(true) {
                    scope.launch {
                        delay(1000)
                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                        finish()
                    }
                }
            }
        }
    }
}