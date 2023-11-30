package com.nhom1.oxygen.data.model.history

data class OHistory(
    val time: Long,
    val history: List<OHourlyHistory?>
)