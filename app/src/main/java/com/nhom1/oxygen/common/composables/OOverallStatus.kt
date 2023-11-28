package com.nhom1.oxygen.common.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nhom1.oxygen.R
import com.nhom1.oxygen.common.constants.getAQIColor
import com.nhom1.oxygen.utils.extensions.oBorder
import java.lang.Integer.min

@Composable
fun OOverallStatus(
    modifier: Modifier = Modifier,
    place: String,
    country: String,
    aqi: Int,
) {
    val statuses = listOf(
        stringResource(R.string.good),
        stringResource(R.string.moderate),
        stringResource(R.string.bad),
        stringResource(R.string.unhealthy),
        stringResource(R.string.very_unhealthy),
        stringResource(R.string.hazardous)
    )
    val descriptions = listOf(
        stringResource(R.string.good_description),
        stringResource(R.string.moderate_description),
        stringResource(R.string.bad_description),
        stringResource(R.string.unhealthy_description),
        stringResource(R.string.very_unhealthy_description),
        stringResource(R.string.hazardous_description)
    )
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = getAQIColor(aqi),
            contentColor = if ((aqi in 0..100)) Color.Black else Color.White
        ),
        modifier = modifier.oBorder()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text(
                    text = place,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = country,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = LocalContentColor.current.copy(alpha = 0.5f)
                )
                Text(
                    text = stringResource(R.string.current_atmosphere),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 12.dp)
                )
                Text(
                    text = when {
                        (aqi in 0..50) -> statuses[0]
                        (aqi in 51..100) -> statuses[1]
                        (aqi in 101..150) -> statuses[2]
                        (aqi in 201..300) -> statuses[4]
                        (aqi in 151..200) -> statuses[3]
                        else -> statuses[5]
                    },
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = when {
                        (aqi in 0..50) -> descriptions[0]
                        (aqi in 51..100) -> descriptions[1]
                        (aqi in 101..150) -> descriptions[2]
                        (aqi in 151..200) -> descriptions[3]
                        (aqi in 201..300) -> descriptions[4]
                        else -> descriptions[5]
                    },
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
            Box {
                PieChart(
                    value = min(aqi, 500) / 500.0,
                    dark = aqi in 0..100,
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Text(
                        text = aqi.toString(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "AQI",
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Composable
fun OOverallStatusCompact(
    modifier: Modifier = Modifier,
    aqi: Int,
    fillMaxHeight: Boolean = false
) {
    val statuses = listOf(
        stringResource(R.string.good),
        stringResource(R.string.moderate),
        stringResource(R.string.bad),
        stringResource(R.string.unhealthy),
        stringResource(R.string.very_unhealthy),
        stringResource(R.string.hazardous)
    )
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = getAQIColor(aqi),
            contentColor = if ((aqi in 0..100)) Color.Black else Color.White
        ),
        modifier = modifier.oBorder()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = if (fillMaxHeight) Arrangement.SpaceBetween else Arrangement.Top,
            modifier = Modifier.padding(16.dp).fillMaxHeight()
        ) {
            Text(
                text = stringResource(R.string.current_atmosphere),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Box(
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                PieChart(
                    value = min(aqi, 500) / 500.0,
                    dark = aqi in 0..100,
                    size = 96.dp
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Text(
                        text = aqi.toString(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "AQI",
                        fontSize = 13.sp
                    )
                }
            }
            Text(
                text = when {
                    (aqi in 0..50) -> statuses[0]
                    (aqi in 51..100) -> statuses[1]
                    (aqi in 101..150) -> statuses[2]
                    (aqi in 201..300) -> statuses[4]
                    (aqi in 151..200) -> statuses[3]
                    else -> statuses[5]
                },
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
fun PieChart(modifier: Modifier = Modifier, value: Double, dark: Boolean = true, size: Dp = 120.dp) {
    Canvas(
        modifier = modifier.size(size)
    ) {
        drawCircle(
            color = if (dark) Color.Black.copy(alpha = 0.25f) else Color.White.copy(alpha = 0.25f),
            style = Stroke(
                width = 10f,
            ),
        )
        drawArc(
            color = if (dark) Color.Black else Color.White,
            startAngle = -90f,
            sweepAngle = (360f * value).toFloat(),
            useCenter = false,
            style = Stroke(
                width = 10f,
            ),
        )
    }
}

@Preview
@Composable
fun OOverallStatusPreview() {
    OOverallStatus(
        place = "Cầu Giấy, Hà Nội",
        country = "Việt Nam",
        aqi = 100
    )
}

@Preview
@Composable
fun OOverallStatusCompactPreview() {
    OOverallStatusCompact(
        aqi = 200
    )
}