package com.nhom1.oxygen.common.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun ODialog(
    title: String,
    content: String,
    cancelText: String,
    confirmText: String,
    onCancel: () -> Unit,
    onDismiss: () -> Unit = onCancel,
    onConfirm: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        OCard(

        ) {
            Column {
                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = content,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(
                        top = 16.dp,
                        bottom = 32.dp
                    )
                )
                Row {
                    OButton(
                        text = cancelText,
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .weight(1f),
                        onClick = onCancel
                    )
                    OButton(
                        text = confirmText,
                        containerColor = Color(0xFFDB3939),
                        contentColor = Color.White,
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .weight(1f),
                        onClick = onConfirm
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ODialogPreview() {
    ODialog(
        title = "Warning",
        content = "Are you sure to sign out?",
        cancelText = "Sign out",
        confirmText = "Cancel",
        onCancel = { }) {

    }
}
