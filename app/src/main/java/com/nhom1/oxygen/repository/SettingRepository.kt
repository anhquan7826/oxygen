package com.nhom1.oxygen.repository

interface SettingRepository {
    var language: String

    /**
     * true: Celsius, false: Fahrenheit
     */
    var temperatureUnit: Boolean

    var receiveNotification: Boolean
}