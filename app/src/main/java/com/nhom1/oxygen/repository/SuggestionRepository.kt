package com.nhom1.oxygen.repository

import com.nhom1.oxygen.data.model.weather.OAirQuality
import io.reactivex.rxjava3.core.Single

interface SuggestionRepository {
    fun getShortSuggestion(airQuality: OAirQuality): Single<String>

    fun getLongSuggestion(airQuality: OAirQuality, diseases: List<String>): Single<List<String>>
}