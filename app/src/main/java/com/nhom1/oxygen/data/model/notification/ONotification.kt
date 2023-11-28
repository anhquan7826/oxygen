package com.nhom1.oxygen.data.model.notification

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notification")
data class ONotification(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: Int,
    val time: Long,
    val message: String
) {
    companion object {
        const val TYPE_WARNING = 0
        const val TYPE_SUGGESTION = 1
    }
}
