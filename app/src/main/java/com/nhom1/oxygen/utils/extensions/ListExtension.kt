package com.nhom1.oxygen.utils.extensions

import com.nhom1.oxygen.data.model.history.OHourlyHistory
import com.nhom1.oxygen.utils.getHour

fun <T> List<T>.compareIgnoreOrder(other: List<T>) =
    size == other.size && this.toSet() == other.toSet()

fun List<OHourlyHistory>.fillGaps(): List<OHourlyHistory?> {
    val newList = mutableListOf<OHourlyHistory?>()
    var j = 0
    for (i in 0..23) {
        val h = getOrNull(j) ?: break
        if (i == getHour(h.time)) {
            newList.add(h)
            j++
        } else {
            newList.add(null)
        }
    }
    return newList
}