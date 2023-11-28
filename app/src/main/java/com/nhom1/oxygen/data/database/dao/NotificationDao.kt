package com.nhom1.oxygen.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.nhom1.oxygen.data.model.notification.ONotification
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notification")
    fun getAllNotifications(): Single<List<ONotification>>

    @Query("SELECT COUNT(*) FROM notification")
    fun countNotifications(): Single<Int>

    @Insert
    fun addNotification(notification: ONotification): Completable

    @Delete
    fun deleteNotification(notification: ONotification): Completable

    @Query("DELETE FROM notification")
    fun deleteAllNotifications(): Completable
}