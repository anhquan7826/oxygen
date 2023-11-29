package com.nhom1.oxygen.utils

import androidx.compose.ui.graphics.Color
import com.nhom1.oxygen.common.constants.aqiColors
import com.nhom1.oxygen.common.constants.bmiColors

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

fun getBMIColor(bmi: Double): Color {
    return when {
        bmi < 18.5 -> bmiColors[0]
        bmi >= 18.5 && bmi < 25 -> bmiColors[1]
        bmi >= 25 && bmi < 30 -> bmiColors[2]
        bmi >= 30 && bmi < 35 -> bmiColors[3]
        else -> bmiColors[4]
    }
}

fun getAQILevel(aqi: Int): Int {
    return when {
        (aqi in 0..50) -> 0
        (aqi in 51..100) -> 1
        (aqi in 101..150) -> 2
        (aqi in 151..200) -> 3
        (aqi in 201..300) -> 4
        else -> 5
    }
}

fun compareAQILevel(aqi1: Int, aqi2: Int): Int {
    val level1 = getAQILevel(aqi1)
    val level2 = getAQILevel(aqi2)
    return level1 - level2
}