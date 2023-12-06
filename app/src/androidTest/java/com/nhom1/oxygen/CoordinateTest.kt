package com.nhom1.oxygen

import com.nhom1.oxygen.utils.CoordinateUtil
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CoordinateTest {
    @Test
    fun coordinateTest1() {
        assertEquals(
            CoordinateUtil.distance(
                Pair(21.035225, 105.777838),
                Pair(21.035225, 105.777838)
            ),
            0.0,
            0.0
        )
    }

    @Test
    fun coordinateTest2() {
        assertTrue(
            CoordinateUtil.distance(
                Pair(21.035225, 105.777838),
                Pair(21.036318, 105.789613)
            ) > 1000
        )
    }

    @Test
    fun coordinateTest3() {
        assertTrue(
            CoordinateUtil.distance(
                Pair(21.035225, 105.777838),
                Pair(21.035111, 105.777934)
            ) < 50
        )
    }

    @Test
    fun coordinateTest4() {
        assertEquals(
            CoordinateUtil.distance(
                Pair(0.0, 0.0),
                Pair(0.0, 0.0)
            ),
            0.0,
            0.0
        )
    }

    @Test
    fun coordinateTest5() {
        assertEquals(
            CoordinateUtil.distance(
                Pair(-1.0, -1.0),
                Pair(0.0, 0.0)
            ),
            160000.0,
            10000.0
        )
    }
}