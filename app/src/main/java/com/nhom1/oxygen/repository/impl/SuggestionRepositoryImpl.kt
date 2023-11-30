package com.nhom1.oxygen.repository.impl

import android.content.SharedPreferences
import com.nhom1.oxygen.data.model.weather.OAirQuality
import com.nhom1.oxygen.data.service.OxygenService
import com.nhom1.oxygen.repository.SuggestionRepository
import com.nhom1.oxygen.utils.compareAQILevel
import com.nhom1.oxygen.utils.constants.SPKeys
import com.nhom1.oxygen.utils.debugLog
import com.nhom1.oxygen.utils.extensions.compareIgnoreOrder
import com.nhom1.oxygen.utils.gson
import com.nhom1.oxygen.utils.now
import com.nhom1.oxygen.utils.toMap
import io.reactivex.rxjava3.core.Single

class SuggestionRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
    private val service: OxygenService
) : SuggestionRepository {
    private data class CachedSuggestion(
        val time: Long,
        val aqi: Int,
        val diseases: List<String> = listOf(),
        val suggestion: String? = null,
        val suggestions: List<String>? = null
    )

    private val interval = 300

    private lateinit var cachedShortSuggestion: CachedSuggestion
    private lateinit var cachedLongSuggestion: CachedSuggestion

    init {
        if (sharedPreferences.contains(SPKeys.Cache.CACHE_SHORT_SUGGESTION)) {
            cachedShortSuggestion = gson.fromJson(
                sharedPreferences.getString(SPKeys.Cache.CACHE_SHORT_SUGGESTION, ""),
                CachedSuggestion::class.java
            )!!
        }
        if (sharedPreferences.contains(SPKeys.Cache.CACHE_LONG_SUGGESTION)) {
            cachedLongSuggestion = gson.fromJson(
                sharedPreferences.getString(SPKeys.Cache.CACHE_LONG_SUGGESTION, ""),
                CachedSuggestion::class.java
            )!!
        }
    }

    override fun getShortSuggestion(airQuality: OAirQuality): Single<String> {
        return when {
            !this::cachedShortSuggestion.isInitialized || now() - cachedShortSuggestion.time > interval || compareAQILevel(
                cachedShortSuggestion.aqi,
                airQuality.aqi
            ) != 0 -> {
                debugLog("${this::class.simpleName}: getShortSuggestion: new!")
                service.suggestion
                    .getShortSuggestion(toMap(airQuality).mapValues { it.value.toString() })
                    .map {
                        it.suggestion
                    }
                    .doOnSuccess {
                        cachedShortSuggestion = CachedSuggestion(
                            time = now(),
                            aqi = airQuality.aqi,
                            suggestion = it
                        )
                        sharedPreferences.edit().putString(
                            SPKeys.Cache.CACHE_SHORT_SUGGESTION,
                            gson.toJson(cachedShortSuggestion)
                        ).apply()
                    }
            }

            else -> {
                debugLog("${this::class.simpleName}: getShortSuggestion: cached!")
                Single.create {
                    it.onSuccess(cachedShortSuggestion.suggestion!!)
                }
            }
        }
    }

    override fun getLongSuggestion(
        airQuality: OAirQuality,
        diseases: List<String>
    ): Single<List<String>> {
        return when {
            !this::cachedShortSuggestion.isInitialized || now() - cachedShortSuggestion.time > interval || compareAQILevel(
                cachedShortSuggestion.aqi,
                airQuality.aqi
            ) != 0 || diseases.compareIgnoreOrder(cachedLongSuggestion.diseases) -> {
                debugLog("${this::class.simpleName}: getLongSuggestion: new!")
                service
                    .suggestion
                    .getLongSuggestion(
                        toMap(airQuality).apply {
                            if (diseases.isNotEmpty()) plus("illness" to diseases.joinToString(", "))
                        }.mapValues { it.value.toString() }
                    ).map {
                        it.suggestions
                    }
                    .doOnSuccess {
                        cachedLongSuggestion = CachedSuggestion(
                            time = now(),
                            aqi = airQuality.aqi,
                            suggestions = it,
                            diseases = diseases
                        )
                        sharedPreferences.edit().putString(
                            SPKeys.Cache.CACHE_LONG_SUGGESTION,
                            gson.toJson(cachedLongSuggestion)
                        ).apply()
                    }
            }

            else -> {
                debugLog("${this::class.simpleName}: getLongSuggestion: cached!")
                Single.create {
                    it.onSuccess(cachedLongSuggestion.suggestions!!)
                }
            }
        }
    }
}