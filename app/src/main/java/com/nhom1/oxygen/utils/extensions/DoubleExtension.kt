package com.nhom1.oxygen.utils.extensions

import kotlin.math.roundToInt

fun Double.toPrettyString(): String {
//    return BigDecimal(this).stripTrailingZeros().toPlainString()
    return ((this * 10).roundToInt() / 10.0).toString().removeSuffix(".0")
}