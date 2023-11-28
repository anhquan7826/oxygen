package com.nhom1.oxygen.repository.impl

import android.content.SharedPreferences
import com.nhom1.oxygen.repository.SettingRepository
import com.nhom1.oxygen.utils.constants.OLanguage
import com.nhom1.oxygen.utils.constants.SPKeys

class SettingRepositoryImpl(
    private val sharedPreferences: SharedPreferences
) : SettingRepository {
    override var language: String
        get() = sharedPreferences.getString(SPKeys.Settings.LANGUAGE, OLanguage.UNSPECIFIED)!!
        set(value) {
            sharedPreferences.edit().putString(SPKeys.Settings.LANGUAGE, value).apply()
        }

    override var temperatureUnit: Boolean
        get() = sharedPreferences.getBoolean(SPKeys.Settings.TEMP_UNIT, true)
        set(value) {
            sharedPreferences.edit().putBoolean(SPKeys.Settings.TEMP_UNIT, value).apply()
        }
    override var receiveNotification: Boolean
        get() = sharedPreferences.getBoolean(SPKeys.Settings.RECEIVE_NOTIFICATION, true)
        set(value) {
            sharedPreferences.edit().putBoolean(SPKeys.Settings.RECEIVE_NOTIFICATION, value).apply()
        }
}