package com.nhom1.oxygen.common.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nhom1.oxygen.common.constants.OxygenColors
import com.nhom1.oxygen.utils.extensions.oBorder
import com.nhom1.oxygen.utils.extensions.toPrettyString
import kotlin.random.Random

data class OBarChartData(
    val barsData: List<OBarData>,
    val maxYValue: Double,
) {
    data class OBarData(
        val label: String,
        val value: Double,
        val color: Color,
        val onClick: () -> Unit,
    )
}

@Composable
fun OBarChart(
    modifier: Modifier = Modifier,
    data: OBarChartData,
    barWidth: Dp = 48.dp,
    barPadding: Dp = 8.dp
) {
    OCard(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .oBorder()
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .fillMaxSize()
                .horizontalScroll(rememberScrollState())
        ) {
            val textMeasurer = rememberTextMeasurer()
            var selectedBar by remember {
                mutableStateOf<OBarChartData.OBarData?>(null)
            }
            for (bar in data.barsData) {
                Column(
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            selectedBar = if (selectedBar == bar) null else bar
                            bar.onClick.invoke()
                        }
                ) {
                    Text(
                        text = bar.value.toPrettyString(),
                        color = bar.color,
                        fontSize = if (bar == selectedBar) 12.sp else 9.sp,
                        fontWeight = if (bar == selectedBar) FontWeight.SemiBold else FontWeight.Light
                    )
                    Box(
                        modifier = Modifier
                            .width(barWidth)
                            .height(210.dp.times((bar.value / data.maxYValue).toFloat()))
                            .padding(horizontal = barPadding)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                color = bar.color
                            ),
                    )
                    Canvas(
                        modifier = Modifier
                            .width(barWidth)
                            .height(32.dp)
                    ) {
                        val textLayout = textMeasurer.measure(
                            bar.label,
                            style = TextStyle(
                                fontSize = 12.sp,
                            )
                        )
                        drawLine(
                            color = OxygenColors.mainColor,
                            start = Offset(0F, size.height / 2),
                            end = Offset(
                                size.width / 2 - textLayout.size.width / 2 - 4,
                                size.height / 2
                            ),
                        )
                        drawText(
                            textLayoutResult = textLayout,
                            topLeft = Offset(
                                size.width / 2 - textLayout.size.width / 2,
                                size.height / 2 - textLayout.size.height / 2,
                            )
                        )
                        drawLine(
                            color = OxygenColors.mainColor,
                            start = Offset(
                                size.width / 2 + textLayout.size.width / 2 + 4,
                                size.height / 2
                            ),
                            end = Offset(size.width, size.height / 2),
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OBarChartPreview() {
    OBarChart(
        data = OBarChartData(
            barsData = List(10) {
                OBarChartData.OBarData(
                    label = it.toString(),
                    value = Random.nextInt(10).toDouble(),
                    color = OxygenColors.mainColor,
                ) {

                }
            },
            maxYValue = 10.0
        )
    )
}