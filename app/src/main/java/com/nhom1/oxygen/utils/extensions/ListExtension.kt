package com.nhom1.oxygen.utils.extensions

fun <T> List<T>.compareIgnoreOrder(other: List<T>) =
    size == other.size && this.toSet() == other.toSet()