package com.nhom1.oxygen.common.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nhom1.oxygen.R

@Composable
fun OError(
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.cannot_load_data),
    buttonLabel: String = stringResource(R.string.retry),
    error: String? = null,
    onRetry: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = R.drawable.error),
            contentDescription = null,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .size(64.dp)
        )
        Text(
            text = title,
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        if (error != null) Text(
            text = error,
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            color = Color.Gray,
        )
        OButton(
            text = buttonLabel,
            modifier = Modifier.padding(top = 16.dp),
            onClick = onRetry
        )
    }
}