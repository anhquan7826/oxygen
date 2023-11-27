@file:OptIn(ExperimentalMaterial3Api::class)

package com.nhom1.oxygen.common.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nhom1.oxygen.common.constants.OxygenColors

@Composable
fun OTextField(
    modifier: Modifier = Modifier,
    initialValue: String = "",
    placeholder: @Composable (() -> Unit)? = null,
    leading: @Composable (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    errorText: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    maxLines: Int = 1,
    onValueChange: (String) -> Unit
) {
    var value by rememberSaveable {
        mutableStateOf(initialValue)
    }
    TextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        onValueChange = {
            value = it
            onValueChange.invoke(it)
        },
        isError = isError,
        shape = RoundedCornerShape(12.dp),
        placeholder = placeholder,
        leadingIcon = leading,
        trailingIcon = trailing,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        supportingText = {
            if (isError && errorText != null) {
                Text(text = errorText, fontSize = 10.sp)
            }
        },
        maxLines = maxLines,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = OxygenColors.containerColor,
            unfocusedContainerColor = OxygenColors.containerColor,
            focusedIndicatorColor = Color.Unspecified,
            unfocusedIndicatorColor = Color.Unspecified,
            errorIndicatorColor = Color.Unspecified,
            focusedSupportingTextColor = Color.Red,
            unfocusedSupportingTextColor = Color.Red,
        )
    )
}

@Preview
@Composable
fun OTextFieldPreview() {
    OTextField {

    }
}