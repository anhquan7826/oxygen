package com.nhom1.oxygen.data.database.converter

import androidx.room.TypeConverter
import com.google.gson.reflect.TypeToken
import com.nhom1.oxygen.utils.gson

class MapConverter {
    @TypeConverter
    fun fromMap(map: Map<String, String>): String {
        return gson.toJson(map)
    }

    @TypeConverter
    fun toMap(data: String): Map<String, String>? {
        val type = object : TypeToken<Map<String, String>>() {}.type
        return gson.fromJson(data, type)
    }
}