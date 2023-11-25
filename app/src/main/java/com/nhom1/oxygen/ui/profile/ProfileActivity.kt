package com.nhom1.oxygen.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImage
import com.nhom1.oxygen.R
import com.nhom1.oxygen.common.composables.OAppBar
import com.nhom1.oxygen.common.composables.OCard
import com.nhom1.oxygen.common.composables.OError
import com.nhom1.oxygen.common.composables.OLoading
import com.nhom1.oxygen.common.constants.getBMIColor
import com.nhom1.oxygen.common.theme.OxygenTheme
import com.nhom1.oxygen.ui.profile.edit.EditProfileActivity
import com.nhom1.oxygen.utils.constants.LoadState
import com.nhom1.oxygen.utils.extensions.oBorder
import com.nhom1.oxygen.utils.extensions.toPrettyString
import com.nhom1.oxygen.utils.toJson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileActivity : ComponentActivity() {
    private lateinit var viewModel: ProfileViewModel
    private lateinit var editLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        editLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            val edited = it.data?.getBooleanExtra("edited", false) ?: false
            if (edited) viewModel.load()
        }
        setContent {
            OxygenTheme {
                Surface(
                    color = Color.White
                ) {
                    ProfileView()
                }
            }
        }
    }

    @Composable
    fun ProfileView() {
        val state by viewModel.state.collectAsState()
        Scaffold(
            topBar = {
                OAppBar(
                    title = stringResource(id = R.string.personal_info),
                    leading = painterResource(id = R.drawable.arrow_back),
                    onLeadingPressed = {
                        finish()
                    },
                    actions = listOf(
                        painterResource(id = R.drawable.edit_colored)
                    ),
                    onActionPressed = listOf {
                        if (state.state == LoadState.LOADED) editLauncher.launch(
                            Intent(
                                this,
                                EditProfileActivity::class.java
                            ).putExtra("userData", toJson(state.userData!!))
                        )
                    }
                )
            },
            containerColor = Color.White,
            contentWindowInsets = WindowInsets(left = 16.dp, right = 16.dp),
            modifier = Modifier.systemBarsPadding()
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
                        OError(
                            modifier = Modifier.align(Alignment.Center),
                            error = state.error
                        ) {
                            viewModel.load()
                        }
                    }
                }

                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it)
                            .verticalScroll(rememberScrollState())
                            .padding(top = 32.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = state.userData!!.name,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                Text(
                                    text = state.userData!!.profile?.sex?.let { sex ->
                                        if (sex) stringResource(R.string.male) else stringResource(R.string.female)
                                    } ?: stringResource(R.string.sex_not_set),
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                Text(
                                    text = state.userData!!.profile?.dateOfBirth
                                        ?: stringResource(R.string.dob_not_set)
                                )
                            }
                            AsyncImage(
                                state.userData!!.avt,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(96.dp)
                                    .clip(CircleShape)
                            )
                        }
                        Divider(
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                        InfoField(
                            title = stringResource(id = R.string.address),
                            content = state.userData!!.profile?.fullAddress
                                ?: stringResource(id = R.string.not_set)
                        )
                        Divider(
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                        Row(
                            modifier = Modifier
                                .padding(bottom = 24.dp)
                                .height(
                                    intrinsicSize = IntrinsicSize.Max
                                )
                        ) {
                            InfoField(
                                title = stringResource(R.string.height),
                                content = state.userData!!.profile?.height?.toPrettyString()
                                    ?: stringResource(R.string.not_set),
                                modifier = Modifier.weight(1f)
                            )
                            Divider(
                                color = DividerDefaults.color,
                                modifier = Modifier
                                    .padding(horizontal = 4.dp)
                                    .fillMaxHeight()
                                    .width(1.dp)
                            )
                            InfoField(
                                title = stringResource(R.string.weight),
                                content = state.userData!!.profile?.weight?.toPrettyString()
                                    ?: stringResource(R.string.not_set),
                                modifier = Modifier.weight(1f)
                            )
                            Divider(
                                color = DividerDefaults.color,
                                modifier = Modifier
                                    .padding(horizontal = 4.dp)
                                    .fillMaxHeight()
                                    .width(1.dp)
                            )
                            InfoField(
                                title = stringResource(R.string.bmi_index),
                                content = state.userData!!.profile?.bmi?.toPrettyString()
                                    ?: stringResource(R.string.not_set),
                                contentColor = if (state.userData!!.profile?.bmi == null) Color.Black else getBMIColor(
                                    state.userData!!.profile?.bmi!!
                                ),
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Text(
                            text = stringResource(R.string.medical_history),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        state.userData!!.diseases?.let { diseases ->
                            if (diseases.isEmpty()) {
                                Text(
                                    text = stringResource(R.string.no_medical_history),
                                    fontWeight = FontWeight.SemiBold
                                )
                            } else {
                                for (disease in diseases) {
                                    OCard(
                                        modifier = Modifier
                                            .padding(bottom = 8.dp)
                                            .fillMaxWidth()
                                            .oBorder()
                                    ) {
                                        Text(
                                            text = disease.name
                                        )
                                    }
                                }
                            }
                        } ?: Text(text = stringResource(R.string.medical_history_not_set))
                    }
                }
            }
        }
    }

    @Composable
    fun InfoField(
        modifier: Modifier = Modifier,
        title: String,
        content: String,
        contentColor: Color = Color.Black
    ) {
        Column(
            modifier = modifier
        ) {
            Text(
                text = title,
                fontSize = 12.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = content,
                color = contentColor,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}