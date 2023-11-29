package com.nhom1.oxygen.ui.profile.edit

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImage
import com.nhom1.oxygen.R
import com.nhom1.oxygen.common.composables.OAppBar
import com.nhom1.oxygen.common.composables.OCard
import com.nhom1.oxygen.common.composables.OChips
import com.nhom1.oxygen.common.composables.ODatePickerDialog
import com.nhom1.oxygen.common.composables.ODialog
import com.nhom1.oxygen.common.composables.ODropdownMenu
import com.nhom1.oxygen.common.composables.OLoading
import com.nhom1.oxygen.common.composables.OOption
import com.nhom1.oxygen.common.composables.OTextField
import com.nhom1.oxygen.common.theme.OxygenTheme
import com.nhom1.oxygen.data.model.user.OUser
import com.nhom1.oxygen.ui.medical.DeclareMedicalHistoryActivity
import com.nhom1.oxygen.utils.extensions.oBorder
import com.nhom1.oxygen.utils.getTimeString
import com.nhom1.oxygen.utils.gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileActivity : ComponentActivity() {
    private lateinit var userData: OUser
    private lateinit var viewModel: EditProfileViewModel

    private var edited = false
    private val declareMedicalHistoryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            edited = it.data?.getBooleanExtra("edited", false) ?: false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        viewModel = ViewModelProvider(this)[EditProfileViewModel::class.java]
        userData = gson.fromJson(intent.extras!!.getString("userData")!!, OUser::class.java)
        viewModel.load(userData)
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
        val saveState by viewModel.saveState.collectAsState()
        LaunchedEffect(saveState) {
            if (saveState.saved == null && saveState.error != null) {
                Toast.makeText(
                    this@EditProfileActivity,
                    resources.getText(R.string.cannot_save).toString() + " (${saveState.error})",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (saveState.saved == true) {
                setResult(
                    RESULT_OK,
                    Intent().putExtra("edited", true)
                )
                finish()
            }
        }
        Scaffold(
            topBar = {
                OAppBar(title = stringResource(R.string.edit_profile),
                    leading = painterResource(id = R.drawable.cancel_colored),
                    onLeadingPressed = {
                        if (viewModel.hasModified()) {
                            showCancelDialog = true
                        } else {
                            setResult(RESULT_OK, Intent().putExtra("edited", edited))
                            finish()
                        }
                    },
                    actions = listOf(
                        painterResource(id = R.drawable.save_colored)
                    ),
                    onActionPressed = listOf {
                        if (viewModel.canSave()) {
                            viewModel.saveUserData(this)
                        } else {
                            Toast.makeText(
                                this,
                                resources.getText(R.string.please_fill_all_forms),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    trailing = {
                        if (saveState.saved == false) {
                            OLoading()
                        }
                    }
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
                        .align(Alignment.CenterHorizontally),
                    initialImage = Uri.parse(userData.avatar),
                    viewModel = viewModel
                )
                NameField(
                    modifier = Modifier.padding(bottom = 16.dp), viewModel = viewModel
                )
                DoBField(
                    modifier = Modifier.padding(bottom = 16.dp), viewModel = viewModel
                )
                SexField(
                    modifier = Modifier.padding(bottom = 16.dp), viewModel = viewModel
                )
                AddressField(
                    modifier = Modifier.padding(bottom = 32.dp), viewModel = viewModel
                )
                OOption(
                    leading = painterResource(id = R.drawable.medical_history_colored),
                    title = stringResource(R.string.medical_history_form),
                    modifier = Modifier.padding(
                        bottom = 16.dp
                    )
                ) {
                    declareMedicalHistoryLauncher.launch(
                        Intent(
                            this@EditProfileActivity,
                            DeclareMedicalHistoryActivity::class.java
                        ).putExtra("userData", gson.toJson(userData))
                    )
                }
            }
        }
        if (showCancelDialog) {
            ODialog(title = stringResource(id = R.string.warning),
                content = stringResource(R.string.cancel_edit_content),
                cancelText = stringResource(id = R.string.back),
                confirmText = stringResource(R.string.discard),
                onCancel = {
                    showCancelDialog = false
                },
                onConfirm = {
                    setResult(RESULT_OK, Intent().putExtra("edited", edited))
                    finish()
                })
        }
    }

    @Composable
    fun AvatarPicker(
        modifier: Modifier = Modifier,
        initialImage: Uri,
        viewModel: EditProfileViewModel
    ) {
        var image by rememberSaveable {
            mutableStateOf(initialImage)
        }
        val picker = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia()
        ) {
            if (it != null) {
                image = it
                viewModel.setAvt(it)
            }
        }
        Box(modifier = modifier
            .clip(CircleShape)
            .size(128.dp)
            .oBorder()
            .clickable {
                picker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }) {
            AsyncImage(
                image,
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
    fun NameField(modifier: Modifier = Modifier, viewModel: EditProfileViewModel) {
        var isError by rememberSaveable {
            mutableStateOf(false)
        }
        Field(
            modifier = modifier, label = stringResource(R.string.full_name)
        ) {
            OTextField(
                initialValue = userData.name,
                isError = isError,
                errorText = stringResource(R.string.name_cant_be_empty),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
            ) {
                viewModel.setName(it)
                isError = it.isEmpty()
            }
        }
    }

    @Composable
    fun DoBField(modifier: Modifier = Modifier, viewModel: EditProfileViewModel) {
        var dob by rememberSaveable {
            mutableStateOf(userData.profile?.dateOfBirth)
        }
        var showDatePicker by remember {
            mutableStateOf(false)
        }
        Field(label = stringResource(R.string.date_of_birth)) {
            OCard(
                modifier = modifier.fillMaxWidth(),
                onClick = {
                    showDatePicker = true
                }
            ) {
                Text(
                    text = dob?.let { getTimeString(it, "dd/MM/yyyy") } ?: ""
                )
            }
        }
        if (showDatePicker) {
            ODatePickerDialog(
                onDismiss = { showDatePicker = false },
                onDatePicked = {
                    showDatePicker = false
                    dob = it
                    viewModel.setDateOfBirth(it)
                }
            )
        }
    }

    @Composable
    fun SexField(modifier: Modifier = Modifier, viewModel: EditProfileViewModel) {
        Field(modifier = modifier, label = stringResource(R.string.sex)) {
            OChips(
                entries = mapOf(
                    true to stringResource(id = R.string.male),
                    false to stringResource(id = R.string.female)
                ), initialValue = userData.profile?.sex
            ) {
                viewModel.setSex(it)
            }
        }
    }

    @Composable
    fun AddressField(modifier: Modifier = Modifier, viewModel: EditProfileViewModel) {
        val state by viewModel.state.collectAsState()
        Column(
            modifier = modifier
        ) {
            Field(
                label = stringResource(R.string.province_city),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                ODropdownMenu(
                    entries = state.provinces,
                    value = state.currentProvince
                ) {
                    viewModel.setProvince(it)
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
                        entries = state.districts,
                        value = state.currentDistrict
                    ) {
                        viewModel.setDistrict(it)
                    }
                }
                Field(
                    label = stringResource(R.string.ward),
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(1f)
                ) {
                    ODropdownMenu(
                        entries = state.wards,
                        value = state.currentWard
                    ) {
                        viewModel.setWard(it)
                    }
                }
            }
            Field(label = stringResource(id = R.string.address)) {
                var isError by rememberSaveable {
                    mutableStateOf(false)
                }
                OTextField(
                    initialValue = userData.profile?.address ?: "",
                    errorText = stringResource(R.string.address_cannot_be_empty),
                    isError = isError,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                ) {
                    viewModel.setAddress(it)
                    isError = it.isEmpty()
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