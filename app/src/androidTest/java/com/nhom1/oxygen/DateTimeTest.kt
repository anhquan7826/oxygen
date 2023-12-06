package com.nhom1.oxygen

import com.nhom1.oxygen.utils.getTimeString
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class DateTimeTest {
    @Test
    fun dateTimeFormatTest1() {
        val now = Instant.now()
        assertEquals(
            getTimeString(
                now.epochSecond,
                "yyyy-MM-dd HH:mm:ss"
            ),
            LocalDateTime.ofInstant(now, ZoneId.of("GMT+7")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        )
    }

    @Test
    fun dateTimeFormatTest2() {
        val now = Instant.now()
        assertEquals(
            getTimeString(
                now.epochSecond,
                "HH:mm:ss"
            ),
            LocalDateTime.ofInstant(now, ZoneId.of("GMT+7")).format(DateTimeFormatter.ofPattern("HH:mm:ss"))
        )
    }

    @Test
    fun dateTimeFormatTest3() {
        val now = Instant.now()
        assertEquals(
            getTimeString(
                now.epochSecond,
                "HH:yyyy:dd:MM:ss"
            ),
            LocalDateTime.ofInstant(now, ZoneId.of("GMT+7")).format(DateTimeFormatter.ofPattern("HH:yyyy:dd:MM:ss"))
        )
    }

    @Test
    fun dateTimeFormatTest4() {
        val now = Instant.now()
        assertEquals(
            getTimeString(
                now.epochSecond,
                ""
            ),
            LocalDateTime.ofInstant(now, ZoneId.of("GMT+7")).format(DateTimeFormatter.ofPattern(""))
        )
    }
}