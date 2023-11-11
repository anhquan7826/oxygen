package com.nhom1.oxygen.utils.extensions

import java.math.BigDecimal

fun Double.toPrettyString(): String {
    return BigDecimal(this).stripTrailingZeros().toPlainString()
}