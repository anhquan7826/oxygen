package com.nhom1.oxygen.common.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OPageIndicator(
    modifier: Modifier = Modifier,
    indicators: List<String>,
    currentIndex: Int,
    onClick: (Int) -> Unit
) {
    Row(
        modifier = modifier.height(32.dp)
    ) {
        for (index in indicators.indices) {
            val indicator = indicators[index]
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .clip(
                        RoundedCornerShape(
                            topStart = if (index == 0) 12.dp else 0.dp,
                            bottomStart = if (index == 0) 12.dp else 0.dp,
                            topEnd = if (index == indicators.size - 1) 12.dp else 0.dp,
                            bottomEnd = if (index == indicators.size - 1) 12.dp else 0.dp,
                        )
                    )
                    .background(
                        color = if (index == currentIndex) Color(0xFF57A000) else Color(0xFFD5E7BF)
                    )
                    .clickable {
                        onClick.invoke(index)
                    }
            ) {
                Text(
                    text = indicator,
                    fontSize = 12.sp,
                    color = if (index == currentIndex) Color.White else Color.Black,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            if (index != indicators.size - 1) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OPageIndicatorPreview() {
    OPageIndicator(
        indicators = listOf(
            "Index 1",
            "Index 2",
            "Index 3"
        ), currentIndex = 0
    ) {}
}