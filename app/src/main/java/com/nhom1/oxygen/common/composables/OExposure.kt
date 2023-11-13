package com.nhom1.oxygen.common.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nhom1.oxygen.R
import com.nhom1.oxygen.common.constants.aqiColors
import com.nhom1.oxygen.common.theme.oxygenColor
import com.nhom1.oxygen.utils.extensions.oShadow
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun OExposureChart(aqis: List<Int>) {
    var selectedColumn by remember {
        mutableIntStateOf(-1)
    }
    OCard(
        modifier = Modifier
            .oShadow()
            .fillMaxWidth()
            .height(300.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.fillMaxSize()
        ) {
            for (i in 0..23) {
                if (i < aqis.size) {
                    if (selectedColumn == i) {
                        Column(
                            verticalArrangement = Arrangement.Bottom,
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 2.dp)
                        ) {
                            Text(
                                text = aqis[i].toString()
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp.times(aqis[i] / 500f))
                                    .clip(
                                        RoundedCornerShape(
                                            topStart = 8.dp,
                                            topEnd = 8.dp,
                                        )
                                    )
                                    .background(
                                        color = when {
                                            (aqis[i] in 0..50) -> aqiColors[0]
                                            (aqis[i] in 51..100) -> aqiColors[1]
                                            (aqis[i] in 101..150) -> aqiColors[2]
                                            (aqis[i] in 201..300) -> aqiColors[4]
                                            (aqis[i] in 151..200) -> aqiColors[3]
                                            else -> aqiColors[5]
                                        }
                                    )
                                    .clickable {
                                        selectedColumn = if (selectedColumn == i) -1 else i
                                    }
                            ) {}
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(300.dp.times(aqis[i] / 500f))
                                .padding(horizontal = 2.dp)
                                .clip(
                                    RoundedCornerShape(
                                        topStart = 8.dp,
                                        topEnd = 8.dp,
                                    )
                                )
                                .background(
                                    color = when {
                                        (aqis[i] in 0..50) -> aqiColors[0]
                                        (aqis[i] in 51..100) -> aqiColors[1]
                                        (aqis[i] in 101..150) -> aqiColors[2]
                                        (aqis[i] in 201..300) -> aqiColors[4]
                                        (aqis[i] in 151..200) -> aqiColors[3]
                                        else -> aqiColors[5]
                                    }
                                )
                                .clickable {
                                    selectedColumn = if (selectedColumn == i) -1 else i
                                }
                        ) {}
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 2.dp)
                    ) {}
                }
            }
        }
    }
}

@Composable
fun OExposure(
    aqis: List<Int>,
    canvasSize: Dp = 256.dp,
    content: (@Composable BoxScope.() -> Unit)? = null
) {
    Box(
    ) {
        Canvas(
            modifier = Modifier
                .align(Alignment.Center)
                .size(canvasSize)
                .padding(32.dp)
        ) {
            for (i in 0..23) {
                val aqi = if (i < aqis.size) aqis[i] else -1
                drawArc(
                    color = when {
                        (aqi == -1) -> Color.Gray
                        (aqi in 0..50) -> aqiColors[0]
                        (aqi in 51..100) -> aqiColors[1]
                        (aqi in 101..150) -> aqiColors[2]
                        (aqi in 201..300) -> aqiColors[4]
                        (aqi in 151..200) -> aqiColors[3]
                        else -> aqiColors[5]
                    }.copy(alpha = 0.75f),
                    startAngle = 120f + (i * 12.5f),
                    sweepAngle = 12f,
                    useCenter = false,
                    style = Stroke(
                        width = 30f,
                    ),
                )
            }
        }
        Canvas(
            modifier = Modifier
                .align(Alignment.Center)
                .size(canvasSize)
                .padding(18.dp)
        ) {
            for (i in 0..24) {
                drawArc(
                    color = oxygenColor,
                    startAngle = 119.5f + (i * 12.5f),
                    sweepAngle = if (i % 3 == 0) 1f else 0.5f,
                    useCenter = false,
                    style = Stroke(
                        width = 15f,
                    ),
                )
            }
        }
        val textMeasurer = rememberTextMeasurer()
        Canvas(
            modifier = Modifier
                .align(Alignment.Center)
                .size(canvasSize)
                .padding(6.dp)
        ) {
            for (i in 0..24 step 3) {
                val textLayout = textMeasurer.measure(
                    text = i.toString(),
                    style = TextStyle(
                        color = oxygenColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Light
                    )
                )
                val r = size.width / 2
                drawText(
                    textLayoutResult = textLayout,
                    topLeft = Offset(
                        x = (r + r * cos(
                            abs((120f + 37.5f * (i / 3))) * PI / 180f
                        ).toFloat()) - (textLayout.size.width / 2),
                        y = (r + r * sin(
                            abs((120f + 37.5f * (i / 3))) * PI / 180f
                        ).toFloat()) - (textLayout.size.height / 2),
                    )
                )
            }
        }
        content?.invoke(this)
    }
}

@Preview(showBackground = true)
@Composable
fun OExposurePreview() {
    OExposure(aqis = (0..12).map { Random.nextInt(501) }) {
        Image(
            painter = painterResource(id = R.drawable.user_colored), contentDescription = null,
            modifier = Modifier.size(96.dp).align(Alignment.Center)
        )
    }
}