package com.nhom1.oxygen.utils

import android.content.Context
import android.content.SharedPreferences
import com.nhom1.oxygen.common.constants.SPKeys

object ConfigUtil {
    private lateinit var sharedPreferences: SharedPreferences

    fun setup(context: Context) {
        sharedPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    }

    var firstLaunch: Boolean
        get() = sharedPreferences.getBoolean(SPKeys.firstLaunch, true)
        set(value) {
            sharedPreferences.edit().putBoolean(SPKeys.firstLaunch, value).apply()
        }
}