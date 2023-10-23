package com.nhom1.oxygen.data.database.converter

import androidx.room.TypeConverter
import com.google.gson.reflect.TypeToken
import com.nhom1.oxygen.utils.fromJson
import com.nhom1.oxygen.utils.toJson

class MapConverter {
    @TypeConverter
    fun fromMap(map: Map<String, String>): String {
        return toJson(map)
    }

    @TypeConverter
    fun toMap(data: String): Map<String, String>? {
        val type = object : TypeToken<Map<String, String>>() {}.type
        return fromJson(data, type)
    }
}