package com.nhom1.oxygen.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nhom1.oxygen.data.database.dao.NotificationDao
import com.nhom1.oxygen.data.database.dao.SearchedLocationDao
import com.nhom1.oxygen.data.model.location.OLocation
import com.nhom1.oxygen.data.model.notification.ONotification

@Database(
    entities = [OLocation::class, ONotification::class],
    version = 2
)
abstract class OxygenDatabase : RoomDatabase() {
    companion object {
        fun build(context: Context): OxygenDatabase {
            return Room.databaseBuilder(
                context,
                OxygenDatabase::class.java, "oxygen-database"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    abstract fun searchedLocationDao(): SearchedLocationDao
    abstract fun notificationDao(): NotificationDao
}