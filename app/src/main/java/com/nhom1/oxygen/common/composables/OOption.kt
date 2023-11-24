package com.nhom1.oxygen.common.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nhom1.oxygen.R
import com.nhom1.oxygen.utils.extensions.oBorder

@Composable
fun OOption(
    modifier: Modifier = Modifier,
    leading: Painter,
    title: String,
    titleColor: Color = Color.Black,
    subtitle: String? = null,
    subtitleColor: Color? = null,
    onClick: () -> Unit
) {
    OCard(
        modifier = modifier
            .fillMaxWidth()
            .oBorder(),
        onClick = {
            onClick.invoke()
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = leading,
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(32.dp)
            )
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    color = titleColor
                )
                subtitle?.let {
                    Text(
                        text = it,
                        fontSize = 11.sp,
                        color = subtitleColor ?: Color.Black,
                    )
                }
            }
            Icon(
                painter = painterResource(id = R.drawable.chevron_right),
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
fun OOptionPreview() {
    OOption(
        leading = painterResource(id = R.drawable.history_colored),
        title = "Preview title",
        subtitle = "Preview subtitle"
    ) {

    }
}