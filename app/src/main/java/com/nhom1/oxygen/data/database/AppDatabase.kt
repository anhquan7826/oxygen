package com.nhom1.oxygen.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nhom1.oxygen.data.database.dao.SampleDao

@Database(entities = [], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sampleDao(): SampleDao
}