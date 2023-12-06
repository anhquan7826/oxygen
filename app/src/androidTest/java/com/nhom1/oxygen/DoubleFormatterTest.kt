package com.nhom1.oxygen

import com.nhom1.oxygen.utils.CoordinateUtil
import com.nhom1.oxygen.utils.extensions.compareIgnoreOrder
import com.nhom1.oxygen.utils.extensions.toPrettyString
import org.junit.Assert.assertTrue
import org.junit.Test

class DoubleFormatterTest {
    @Test
    fun doubleFormatterTest1() {
        assertTrue(0.123114543.toPrettyString() == "0.1")
    }

    @Test
    fun doubleFormatterTest2() {
        assertTrue(0.19234123.toPrettyString() == "0.2")
    }

    @Test
    fun doubleFormatterTest3() {
        assertTrue(0.00000000.toPrettyString() == "0")
    }

    @Test
    fun doubleFormatterTest4() {
        assertTrue((-1.36234).toPrettyString() == "-1.4")
    }
}