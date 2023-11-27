@file:OptIn(ExperimentalMaterial3Api::class)

package com.nhom1.oxygen.common.composables

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.nhom1.oxygen.R
import com.nhom1.oxygen.utils.now

@Composable
fun ODatePickerDialog(
    initialDate: Long = now(),
    onDismiss: () -> Unit,
    onDatePicked: (Long) -> Unit
) {
    val state = rememberDatePickerState(
        initialSelectedDateMillis = initialDate * 1000
    )
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    state.selectedDateMillis?.let {
                        onDatePicked(it / 1000)
                    }
                }
            ) {
                Text(stringResource(R.string.ok))
            }
        }
    ) {
        DatePicker(
            state = state,
            dateValidator = {
                it < now() * 1000
            },
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ODatePickerDialogPreview() {
    ODatePickerDialog(onDismiss = {}) {}
}