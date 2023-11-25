package com.nhom1.oxygen.ui.profile.edit

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImage
import com.nhom1.oxygen.R
import com.nhom1.oxygen.common.composables.OAppBar
import com.nhom1.oxygen.common.composables.OChips
import com.nhom1.oxygen.common.composables.ODialog
import com.nhom1.oxygen.common.composables.ODropdownMenu
import com.nhom1.oxygen.common.composables.OOption
import com.nhom1.oxygen.common.composables.OTextField
import com.nhom1.oxygen.common.theme.OxygenTheme
import com.nhom1.oxygen.data.model.user.OUser
import com.nhom1.oxygen.utils.extensions.oBorder
import com.nhom1.oxygen.utils.fromJson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileActivity : ComponentActivity() {
    private lateinit var userData: OUser
    private lateinit var viewModel: EditProfileViewModel

    private lateinit var imagePickerLauncher: ActivityResultLauncher<PickVisualMediaRequest>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        viewModel = ViewModelProvider(this)[EditProfileViewModel::class.java]
        userData = fromJson(intent.extras!!.getString("userData")!!, OUser::class.java)!!
        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            // TODO: set avt
        }
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
        var showCancelDialog by remember {
            mutableStateOf(false)
        }
        Scaffold(
            topBar = {
                OAppBar(
                    title = stringResource(R.string.edit_profile),
                    leading = painterResource(id = R.drawable.cancel_colored),
                    onLeadingPressed = {
                        showCancelDialog = true
                    },
                    actions = listOf(
                        painterResource(id = R.drawable.save_colored)
                    ),
                    onActionPressed = listOf {
                        setResult(RESULT_OK, Intent().putExtra("edited", true))
                        finish()
                    }
                )
            },
            containerColor = Color.White,
            contentWindowInsets = ScaffoldDefaults.contentWindowInsets.add(
                WindowInsets(
                    left = 16.dp,
                    right = 16.dp
                )
            ),
            modifier = Modifier.statusBarsPadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(it)
                    .padding(top = 32.dp),
            ) {
                AvatarPicker(
                    modifier = Modifier
                        .padding(bottom = 32.dp)
                        .align(Alignment.CenterHorizontally)
                )
                NameField(
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                SexField(
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                AddressField(
                    modifier = Modifier.padding(bottom = 32.dp)
                )
                OOption(
                    leading = painterResource(id = R.drawable.medical_history_colored),
                    title = stringResource(R.string.medical_history_form),
                    modifier = Modifier.padding(
                        bottom = 16.dp
                    )
                ) {
                    // TODO: Go to medical history
                }
            }
        }
        if (showCancelDialog) {
            ODialog(
                title = stringResource(id = R.string.warning),
                content = stringResource(R.string.cancel_edit_content),
                cancelText = stringResource(id = R.string.back),
                confirmText = stringResource(id = R.string.cancel),
                onCancel = {
                    showCancelDialog = false
                }, onConfirm = {
                    // TODO: cancel
                    setResult(RESULT_OK, Intent().putExtra("edited", false))
                    finish()
                })
        }
    }

    @Composable
    fun AvatarPicker(modifier: Modifier = Modifier) {
        Box(
            modifier = modifier
                .clip(CircleShape)
                .size(128.dp)
                .oBorder()
                .clickable {
                    imagePickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
        ) {
            AsyncImage(
                userData.avt,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.3F)
                    .background(
                        color = Color.Black.copy(alpha = 0.5F)
                    )
                    .align(Alignment.BottomCenter)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.gallery_colored),
                    contentDescription = null,
                    modifier = Modifier
                        .size(22.dp)
                        .align(Alignment.Center)
                )
            }
        }
    }

    @Composable
    fun NameField(modifier: Modifier = Modifier) {
        Field(
            modifier = modifier,
            label = stringResource(R.string.full_name)
        ) {
            OTextField(
                initialValue = userData.name
            ) {
                // TODO: On name changes
            }
        }
    }

    @Composable
    fun SexField(modifier: Modifier = Modifier) {
        Field(modifier = modifier, label = stringResource(R.string.sex)) {
            OChips(
                entries = mapOf(
                    true to stringResource(id = R.string.male),
                    false to stringResource(id = R.string.female)
                ),
                initialValue = userData.profile?.sex
            ) {
                // TODO: On sex changes
            }
        }
    }

    @Composable
    fun AddressField(modifier: Modifier = Modifier) {
        Column(
            modifier = modifier
        ) {
            Row(
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Field(
                    label = stringResource(R.string.country),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .weight(1f)
                ) {
                    ODropdownMenu(
                        entries = mapOf(
                            0 to "Menu 0",
                            1 to "Menu 1",
                            2 to "Menu 2"
                        )
                    ) {

                    }
                }
                Field(
                    label = stringResource(R.string.province_city),
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(1f)
                ) {
                    ODropdownMenu(
                        entries = mapOf(
                            0 to "Menu 0",
                            1 to "Menu 1",
                            2 to "Menu 2"
                        )
                    ) {

                    }
                }
            }
            Row(
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Field(
                    label = stringResource(R.string.district),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .weight(1f)
                ) {
                    ODropdownMenu(
                        entries = mapOf(
                            0 to "Menu 0",
                            1 to "Menu 1",
                            2 to "Menu 2"
                        )
                    ) {

                    }
                }
                Field(
                    label = stringResource(R.string.ward),
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(1f)
                ) {
                    ODropdownMenu(
                        entries = mapOf(
                            0 to "Menu 0",
                            1 to "Menu 1",
                            2 to "Menu 2"
                        )
                    ) {

                    }
                }
            }
            Field(label = stringResource(id = R.string.address)) {
                OTextField(
                    initialValue = userData.profile?.address ?: ""
                ) {

                }
            }
        }
    }

    @Composable
    fun Field(modifier: Modifier = Modifier, label: String, content: @Composable () -> Unit) {
        Column(
            modifier = modifier
        ) {
            Text(
                text = label,
                fontSize = 12.sp,
                modifier = Modifier.padding(bottom = 8.dp, start = 8.dp)
            )
            content()
        }
    }
}