package com.nhom1.oxygen.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nhom1.oxygen.data.database.dao.SearchedLocationDao
import com.nhom1.oxygen.data.model.location.OLocation

@Database(
    entities = [OLocation::class],
    version = 1
)
abstract class OxygenDatabase : RoomDatabase() {
    companion object {
        fun build(context: Context): OxygenDatabase {
            return Room.databaseBuilder(
                context,
                OxygenDatabase::class.java, "oxygen-database"
            ).build()
        }
    }

    abstract fun searchedLocationDao(): SearchedLocationDao
}