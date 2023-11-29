package com.nhom1.oxygen.repository.impl

import android.content.SharedPreferences
import com.nhom1.oxygen.data.service.OxygenService
import com.nhom1.oxygen.repository.LocationRepository
import com.nhom1.oxygen.repository.SuggestionRepository
import com.nhom1.oxygen.repository.UserRepository
import com.nhom1.oxygen.repository.WeatherRepository
import com.nhom1.oxygen.utils.compareAQILevel
import com.nhom1.oxygen.utils.constants.SPKeys
import com.nhom1.oxygen.utils.fromJson
import com.nhom1.oxygen.utils.listen
import com.nhom1.oxygen.utils.now
import com.nhom1.oxygen.utils.toJson
import com.nhom1.oxygen.utils.toMap
import io.reactivex.rxjava3.core.Single

class SuggestionRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
    private val service: OxygenService,
    private val locationRepository: LocationRepository,
    private val weatherRepository: WeatherRepository,
    private val userRepository: UserRepository
) : SuggestionRepository {
    private data class CachedSuggestion(
        val time: Long,
        val aqi: Int,
        val suggestion: String? = null,
        val suggestions: List<String>? = null
    )

    private val interval = 14400

    private lateinit var cachedShortSuggestion: CachedSuggestion
    private lateinit var cachedLongSuggestion: CachedSuggestion

    init {
        if (sharedPreferences.contains(SPKeys.Cache.CACHE_SHORT_SUGGESTION)) {
            cachedShortSuggestion = fromJson(
                sharedPreferences.getString(SPKeys.Cache.CACHE_SHORT_SUGGESTION, "")!!,
                CachedSuggestion::class.java
            )!!
        }
        if (sharedPreferences.contains(SPKeys.Cache.CACHE_LONG_SUGGESTION)) {
            cachedLongSuggestion = fromJson(
                sharedPreferences.getString(SPKeys.Cache.CACHE_LONG_SUGGESTION, "")!!,
                CachedSuggestion::class.java
            )!!
        }
    }

    override fun getShortSuggestion(): Single<String> {
        return Single.create { emitter ->
            locationRepository.getCurrentLocation().listen(
                onError = { emitter.onError(it) }
            ) { location ->
                weatherRepository.getCurrentWeatherInfo(location).listen(
                    onError = { emitter.onError(it) }
                ) { weather ->
                    val isCacheInitialized = this::cachedShortSuggestion.isInitialized
                    val isTimeExceedInterval = now() - cachedShortSuggestion.time > interval
                    val isAQIChanged = compareAQILevel(
                        cachedShortSuggestion.aqi,
                        weather.airQuality.aqi
                    ) != 0
                    when {
                        !isCacheInitialized || isTimeExceedInterval || isAQIChanged -> {
                            service.suggestion
                                .getShortSuggestion(toMap(weather.airQuality).mapValues { it.value!! })
                                .listen(
                                    onError = { emitter.onError(it) }
                                ) {
                                    cachedShortSuggestion = CachedSuggestion(
                                        time = now(),
                                        aqi = weather.airQuality.aqi,
                                        suggestion = it.suggestion
                                    )
                                    sharedPreferences.edit().putString(
                                        SPKeys.Cache.CACHE_SHORT_SUGGESTION,
                                        toJson(cachedShortSuggestion)
                                    ).apply()
                                    emitter.onSuccess(it.suggestion)
                                }
                        }

                        else -> {
                            emitter.onSuccess(cachedShortSuggestion.suggestion!!)
                        }
                    }
                }
            }
        }
    }

    override fun getLongSuggestion(): Single<List<String>> {
        return Single.create { emitter ->
            Single.zip(
                locationRepository.getCurrentLocation(),
                userRepository.getUserData().map { (it.diseases ?: listOf()).map { d -> d.name } }
            ) { location, diseases ->
                Pair(location, diseases)
            }.listen(
                onError = { emitter.onError(it) }
            ) { r ->
                weatherRepository.getCurrentWeatherInfo(r.first).listen(
                    onError = { emitter.onError(it) }
                ) { weather ->
                    val isCacheInitialized = this::cachedShortSuggestion.isInitialized
                    val isTimeExceedInterval = now() - cachedShortSuggestion.time > interval
                    val isAQIChanged = compareAQILevel(
                        cachedShortSuggestion.aqi,
                        weather.airQuality.aqi
                    ) != 0
                    when {
                        !isCacheInitialized || isTimeExceedInterval || isAQIChanged -> {
                            service
                                .suggestion
                                .getLongSuggestion(
                                    toMap(weather.airQuality)
                                        .mapValues { it.value!! }
                                        .plus("illness" to r.second.joinToString(", "))
                                )
                                .listen(
                                    onError = { emitter.onError(it) }
                                ) {
                                    cachedLongSuggestion = CachedSuggestion(
                                        time = now(),
                                        aqi = weather.airQuality.aqi,
                                        suggestions = it.suggestions
                                    )
                                    sharedPreferences.edit().putString(
                                        SPKeys.Cache.CACHE_LONG_SUGGESTION,
                                        toJson(cachedLongSuggestion)
                                    ).apply()
                                    emitter.onSuccess(it.suggestions)
                                }
                        }

                        else -> {
                            emitter.onSuccess(cachedLongSuggestion.suggestions!!)
                        }
                    }
                }
            }
        }
    }
}