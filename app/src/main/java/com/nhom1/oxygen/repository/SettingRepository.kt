package com.nhom1.oxygen.repository

interface SettingRepository {
    /**
     * true: Celsius, false: Fahrenheit
     */
    var temperatureUnit: Boolean

    var receiveNotification: Boolean
}