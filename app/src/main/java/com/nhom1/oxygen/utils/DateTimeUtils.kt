package com.nhom1.oxygen.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun getTimeString(epoch: Long, format: String = "HH:mm"): String {
    val instant = Instant.ofEpochSecond(epoch)
    val formatter = DateTimeFormatter.ofPattern(format)
    return instant.atZone(ZoneId.of("GMT+7")).format(formatter)
}

fun getHour(epoch: Long): Int {
    val instant = Instant.ofEpochSecond(epoch)
    return instant.atZone(ZoneId.of("GMT+7")).hour
}

fun now(): Long {
    return Instant.now().atZone(ZoneId.of("GMT+7")).toEpochSecond()
}