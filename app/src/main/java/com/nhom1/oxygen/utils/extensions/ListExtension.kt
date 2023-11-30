package com.nhom1.oxygen.utils.extensions

import com.nhom1.oxygen.data.model.history.OHourlyHistory
import com.nhom1.oxygen.utils.getHour

fun <T> List<T>.compareIgnoreOrder(other: List<T>) =
    size == other.size && this.toSet() == other.toSet()