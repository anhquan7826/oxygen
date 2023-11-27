package com.nhom1.oxygen.ui.medical

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.nhom1.oxygen.R
import com.nhom1.oxygen.common.composables.OAppBar
import com.nhom1.oxygen.common.composables.OButtonPrimary
import com.nhom1.oxygen.common.composables.OButtonSecondary
import com.nhom1.oxygen.common.composables.OCard
import com.nhom1.oxygen.common.composables.ODialog
import com.nhom1.oxygen.common.composables.OLoading
import com.nhom1.oxygen.common.composables.OTextField
import com.nhom1.oxygen.common.constants.getBMIColor
import com.nhom1.oxygen.common.theme.OxygenTheme
import com.nhom1.oxygen.data.model.user.OUser
import com.nhom1.oxygen.utils.constants.LoadState
import com.nhom1.oxygen.utils.extensions.toPrettyString
import com.nhom1.oxygen.utils.fromJson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeclareMedicalHistoryActivity : ComponentActivity() {
    private lateinit var viewModel: DeclareMedicalHistoryViewModel
    private lateinit var userData: OUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        viewModel = ViewModelProvider(this)[DeclareMedicalHistoryViewModel::class.java]
        userData = fromJson(intent.getStringExtra("userData")!!, OUser::class.java)!!
        setContent {
            OxygenTheme {
                Surface(
                    color = Color.White
                ) {
                    DeclareMedicalHistoryView()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.load(userData)
    }

    @Composable
    fun DeclareMedicalHistoryView() {
        var showCancelDialog by remember {
            mutableStateOf(false)
        }
        val state by viewModel.state.collectAsState()
        val saveState by viewModel.saveState.collectAsState()
        LaunchedEffect(saveState) {
            if (saveState.saved == null && saveState.error != null) {
                Toast.makeText(
                    this@DeclareMedicalHistoryActivity,
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
                OAppBar(
                    title = stringResource(R.string.medical_history_form),
                    leading = painterResource(id = R.drawable.cancel_colored),
                    onLeadingPressed = {
                        if (viewModel.hasModified()) {
                            showCancelDialog = true
                        } else {
                            setResult(RESULT_OK, Intent().putExtra("edited", false))
                            finish()
                        }
                    },
                    actions = listOf(
                        painterResource(id = R.drawable.save_colored)
                    ),
                    onActionPressed = listOf {
                        if (viewModel.canSave()) {
                            viewModel.save()
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
                    left = 16.dp,
                    right = 16.dp
                )
            ),
            modifier = Modifier.statusBarsPadding()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(it)
                    .padding(top = 32.dp, bottom = 16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 4.dp)
                    ) {
                        Field(label = stringResource(id = R.string.height)) {
                            OTextField(
                                trailing = {
                                    Text("cm")
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Done
                                ),
                            ) { value ->
                                viewModel.setHeight(value.toDoubleOrNull() ?: -1.0)
                            }
                        }
                        Field(label = stringResource(id = R.string.weight)) {
                            OTextField(
                                trailing = {
                                    Text("kg")
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Done
                                ),
                            ) { value ->
                                viewModel.setWeight(value.toDoubleOrNull() ?: -1.0)
                            }
                        }
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 4.dp)
                    ) {
                        Text(
                            text = state.bmi?.toPrettyString()
                                ?: stringResource(R.string.unidentified),
                            color = if (state.bmi == null) Color.Black else getBMIColor(state.bmi!!),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 24.dp)
                        )
                        Text(
                            text = buildAnnotatedString {
                                append("kg/m")
                                withStyle(
                                    SpanStyle(
                                        baselineShift = BaselineShift.Superscript,
                                        fontSize = 8.sp,
                                    )
                                ) {
                                    append("2")
                                }
                            },
                            fontSize = 10.sp,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Text(
                            text = stringResource(id = R.string.bmi_index)
                        )
                    }
                }
                Field(
                    label = stringResource(id = R.string.medical_history),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    OTextField(
                        maxLines = Int.MAX_VALUE,
                        modifier = Modifier.height(250.dp)
                    ) { value ->
                        viewModel.setPathology(value)
                    }
                }
                if (state.state == LoadState.LOADING) {
                    OButtonSecondary(
                        text = stringResource(R.string.analyzing),
                        leading = {
                            OLoading()
                        },
                        minWidth = 200.dp
                    ) {}
                } else {
                    OButtonPrimary(
                        text = stringResource(R.string.analyze_pathology),
                        minWidth = 200.dp
                    ) {
                        viewModel.analyzePathology()
                    }
                }
                if (state.diseases.isNotEmpty()) {
                    Field(
                        label = stringResource(R.string.detected_diseases),
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        for (disease in state.diseases) {
                            DiseaseTile(disease = disease) {
                                viewModel.deleteDisease(disease)
                            }
                        }
                    }
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
                    setResult(RESULT_OK, Intent().putExtra("edited", false))
                    finish()
                })
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

    @Composable
    fun DiseaseTile(disease: String, onDelete: () -> Unit) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            OCard(modifier = Modifier.weight(1f)) {
                Text(disease)
            }
            IconButton(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(32.dp),
                onClick = onDelete
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.close),
                    contentDescription = null,
                )
            }
        }
    }
}