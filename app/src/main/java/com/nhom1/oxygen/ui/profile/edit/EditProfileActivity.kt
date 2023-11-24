package com.nhom1.oxygen.ui.profile.edit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.nhom1.oxygen.R
import com.nhom1.oxygen.common.composables.OAppBar
import com.nhom1.oxygen.common.theme.OxygenTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileActivity : ComponentActivity() {
    private lateinit var viewModel: EditProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        viewModel = ViewModelProvider(this)[EditProfileViewModel::class.java]
        setContent {
            OxygenTheme {
                Surface(
                    color = Color.White
                ) {
                    EditProfileView()
                }
            }
        }
    }

    @Composable
    fun EditProfileView() {
        Scaffold(
            topBar = {
                OAppBar(
                    title = stringResource(R.string.edit_profile),
                    leading = painterResource(id = R.drawable.cancel_colored),
                    onLeadingPressed = {
                        // TODO: On cancel
                    },
                    actions = listOf(
                        painterResource(id = R.drawable.save_colored)
                    ),
                    onActionPressed = listOf {
                        // TODO: on save
                        finish()
                    }
                )
            },
            containerColor = Color.White,
            modifier = Modifier.statusBarsPadding()
        ) {
            Column(
                modifier = Modifier.padding(it)
            ) {

            }
        }
    }
}