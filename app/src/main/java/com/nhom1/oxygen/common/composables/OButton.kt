package com.nhom1.oxygen.common.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nhom1.oxygen.common.constants.OxygenColors

@Composable
fun OButton(
    modifier: Modifier = Modifier,
    text: String,
    leading: Painter? = null,
    minWidth: Dp? = null,
    maxWidth: Dp? = null,
    containerColor: Color = OxygenColors.containerColor,
    contentColor: Color = Color.Black,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier
            .sizeIn(
                minWidth = minWidth ?: Dp.Unspecified,
                maxWidth = maxWidth ?: Dp.Unspecified
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        onClick = onClick,
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp
        ),
        contentPadding = PaddingValues(
            horizontal = 16.dp,
            vertical = 8.dp
        ),
        shape = RoundedCornerShape(12.dp),
    ) {
        if (leading != null)
            Icon(
                leading,
                null,
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp)
            )
        Text(
            text = text,
            modifier = if (leading != null) Modifier.weight(1f) else Modifier,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun OButtonPrimary(
    modifier: Modifier = Modifier,
    text: String,
    minWidth: Dp? = null,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier
            .defaultMinSize(
                minWidth = minWidth ?: Dp.Unspecified
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF57A000),
            contentColor = Color.White
        ),
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        elevation = ButtonDefaults.buttonElevation(),
    ) {
        Text(text = text)
    }
}

@Composable
fun OButtonSecondary(
    modifier: Modifier = Modifier,
    text: String,
    leading: (@Composable () -> Unit)? = null,
    minWidth: Dp? = null,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier
            .defaultMinSize(
                minWidth = minWidth ?: Dp.Unspecified
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFD4EBBB),
            contentColor = Color.Gray
        ),
        onClick = onClick,
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp
        ),
        shape = RoundedCornerShape(12.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leading != null) {
                Box(modifier = Modifier
                    .padding(end = 16.dp)
                    .size(24.dp)) {
                    leading()
                }
            }
            Text(text = text)
        }
    }
}

@Preview
@Composable
fun OButtonPreview() {
    OButton(text = "Preview", minWidth = 128.dp) {}
}

@Preview
@Composable
fun OButtonPrimaryPreview() {
    OButtonPrimary(text = "Preview", minWidth = 128.dp) {}
}

@Preview
@Composable
fun OButtonSecondaryPreview() {
    OButtonSecondary(text = "Preview", minWidth = 128.dp, leading = {
        CircularProgressIndicator()
    }) {}
}