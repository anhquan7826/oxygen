package com.nhom1.oxygen.utils

import com.google.gson.Gson
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

val gson = Gson()

fun <T : Any> toMap(obj: T): Map<String, Any?> {
    return (obj::class as KClass<T>).memberProperties.associate { prop ->
        prop.name to prop.get(obj)?.let { value ->
            if (value::class.isData) {
                toMap(value)
            } else {
                value
            }
        }
    }
}