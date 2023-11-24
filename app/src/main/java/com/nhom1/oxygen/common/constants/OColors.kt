package com.nhom1.oxygen.common.constants

import androidx.compose.ui.graphics.Color

val aqiColors = listOf(
    Color(0xFF00E400),
    Color(0xFFFFFF00),
    Color(0xFFE06D2D),
    Color(0xFFC51919),
    Color(0xFF6C07D1),
    Color(0xFF7E0023)
)

fun getAQIColor(aqi: Int): Color {
    return when {
        (aqi in 0..50) -> aqiColors[0]
        (aqi in 51..100) -> aqiColors[1]
        (aqi in 101..150) -> aqiColors[2]
        (aqi in 151..200) -> aqiColors[3]
        (aqi in 201..300) -> aqiColors[4]
        else -> aqiColors[5]
    }
}

val bmiColors = listOf(
    Color(0xFF8AB4D8),
    Color(0xFF7FC89B),
    Color(0xFFFFD401),
    Color(0xFFF19451),
    Color(0xFFE75055)
)

fun getBMIColor(bmi: Double): Color {
    return when {
        bmi < 18.5 -> bmiColors[0]
        bmi >= 18.5 && bmi < 25 -> bmiColors[1]
        bmi >= 25 && bmi < 30 -> bmiColors[2]
        bmi >= 30 && bmi < 35 -> bmiColors[3]
        else -> bmiColors[4]
    }
}