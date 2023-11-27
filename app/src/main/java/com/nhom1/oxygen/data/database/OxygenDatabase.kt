package com.nhom1.oxygen.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//@Database(
//    entities = [],
//    version = 1
//)
abstract class OxygenDatabase : RoomDatabase() {
    companion object {
        fun build(context: Context): OxygenDatabase {
            return Room.databaseBuilder(
                context,
                OxygenDatabase::class.java, "oxygen-database"
            ).build()
        }
    }
}