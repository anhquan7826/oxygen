package com.nhom1.oxygen.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun getTimeString(epoch: Long, format: String = "HH:mm"): String {
    val instant = Instant.ofEpochSecond(epoch)
    val formatter = DateTimeFormatter.ofPattern(format)
    return instant.atZone(ZoneId.systemDefault()).format(formatter)
}