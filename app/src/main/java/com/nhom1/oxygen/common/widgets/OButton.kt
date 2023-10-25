package com.nhom1.oxygen.common.widgets

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun OButtonPrimary(modifier: Modifier = Modifier, text: String, minWidth: Dp? = null, onClick: () -> Unit) {
    Button(
        modifier = modifier.defaultMinSize(
            minWidth = minWidth ?: Dp.Unspecified
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF57A000),
            contentColor = Color.White
        ),
        onClick = onClick,
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 2.dp
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(text = text)
    }
}

@Composable
fun OButtonSecondary(modifier: Modifier = Modifier, text: String, minWidth: Dp? = null, onClick: () -> Unit) {
    Button(
        modifier = modifier.defaultMinSize(
            minWidth = minWidth ?: Dp.Unspecified
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFD4EBBB),
            contentColor = Color.Gray
        ),
        onClick = onClick,
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 2.dp
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(text = text)
    }
}

@Preview
@Composable
fun OButtonPrimaryPreview() {
    OButtonPrimary(text = "Preview", minWidth = 128.dp) {}
}

@Preview
@Composable
fun OButtonSecondaryPreview() {
    OButtonSecondary(text = "Preview", minWidth = 128.dp) {}
}