@file:OptIn(ExperimentalMaterial3Api::class)

package com.nhom1.oxygen.common.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.nhom1.oxygen.utils.extensions.oBorder

@Composable
fun OTextField(
    modifier: Modifier = Modifier,
    initialValue: String = "",
    placeholder: @Composable (() -> Unit)? = null,
    leading: @Composable (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onValueChange: (String) -> Unit
) {
    var value by rememberSaveable {
        mutableStateOf(initialValue)
    }
    TextField(
        modifier = modifier
            .fillMaxWidth()
            .oBorder(),
        value = value,
        onValueChange = {
            value = it
            onValueChange.invoke(it)
        },
        shape = RoundedCornerShape(12.dp),
        placeholder = placeholder,
        leadingIcon = leading,
        trailingIcon = trailing,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        maxLines = 1,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFF2F2F2),
            unfocusedContainerColor = Color(0xFFF2F2F2),
            focusedIndicatorColor = Color.Unspecified,
            unfocusedIndicatorColor = Color.Unspecified,
        )
    )
}

@Preview
@Composable
fun OTextFieldPreview() {
    OTextField {

    }
}