package com.nhom1.oxygen

import com.nhom1.oxygen.utils.extensions.compareIgnoreOrder
import org.junit.Assert
import org.junit.Test

class ListExtensionTest {
    @Test
    fun listTest1() {
        Assert.assertTrue(listOf(1, 3, 2, 5, 4).compareIgnoreOrder(listOf(1, 3, 2, 5, 4)))
    }

    @Test
    fun listTest2() {
        Assert.assertTrue(!listOf(1, 3, 2, 5, 4, 4, 2).compareIgnoreOrder(listOf(1, 3, 2, 5, 4)))
    }

    @Test
    fun listTest3() {
        Assert.assertTrue(listOf(3, 3, 3, 3, 3).compareIgnoreOrder(listOf(3, 3, 3, 3, 3)))
    }

    @Test
    fun listTest4() {
        Assert.assertTrue(listOf<String>().compareIgnoreOrder(listOf()))
    }
}