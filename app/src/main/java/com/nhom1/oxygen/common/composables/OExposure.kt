package com.nhom1.oxygen.common.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nhom1.oxygen.common.constants.aqiColors
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun OExposure(aqis: List<Int>, canvasSize: Dp = 128.dp) {
    val textMeasurer = rememberTextMeasurer()
    Canvas(
        modifier = Modifier
            .size(canvasSize)
    ) {
        for (i in 0..23) {
            val aqi = aqis[i]
            drawArc(
                color = when {
                    (aqi in 0..50) -> aqiColors[0]
                    (aqi in 51..100) -> aqiColors[1]
                    (aqi in 101..150) -> aqiColors[2]
                    (aqi in 201..300) -> aqiColors[4]
                    (aqi in 151..200) -> aqiColors[3]
                    else -> aqiColors[5]
                }.copy(alpha = 0.25f),
                startAngle = 15f * i,
                sweepAngle = 4f,
                useCenter = false,
                style = Stroke(
                    width = 30f,
                    cap = StrokeCap.Round
                ),
            )
        }
        for (i in 0..23) {
            val style = TextStyle(
                fontSize = 8.sp
            )
            val measuredText = textMeasurer.measure(
                (i + 1).toString(),
                style = style
            )
            drawText(
                textMeasurer,
                (i + 1).toString(),
                topLeft = Offset(
                    cos(15f * i + 2f) * (size.width / 2) + size.width / 2 - measuredText.size.width / 2,
                    sin(15f * i + 2f) * (size.height / 2) + size.height / 2 - measuredText.size.height / 2
                ),
                style = style
            )
        }
    }
}

@Preview
@Composable
fun OExposurePreview() {
    OExposure(aqis = (0..23).map { 100 })
}