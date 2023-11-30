package com.nhom1.oxygen.ui.home

import androidx.lifecycle.ViewModel
import com.nhom1.oxygen.data.model.location.OLocation
import com.nhom1.oxygen.data.model.weather.OWeather
import com.nhom1.oxygen.repository.LocationRepository
import com.nhom1.oxygen.repository.NotificationRepository
import com.nhom1.oxygen.repository.SettingRepository
import com.nhom1.oxygen.repository.SuggestionRepository
import com.nhom1.oxygen.repository.WeatherRepository
import com.nhom1.oxygen.utils.constants.LoadState
import com.nhom1.oxygen.utils.listen
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class OverviewViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val locationRepository: LocationRepository,
    private val suggestionRepository: SuggestionRepository,
    private val settingRepository: SettingRepository,
    private val notificationRepository: NotificationRepository,
) : ViewModel() {
    data class OverviewState(
        val state: LoadState = LoadState.LOADING,
        val notifications: Int = 0,
        val location: OLocation? = null,
        val suggestion: String? = null,
        val weatherCurrent: OWeather? = null,
        val weather24h: List<OWeather>? = null,
        val error: String? = null
    )

    private val _overviewState = MutableStateFlow(OverviewState())
    val overviewState = _overviewState.asStateFlow()

    val tempUnit: Boolean get() = settingRepository.temperatureUnit

    fun load() {
        _overviewState.update {
            OverviewState(
                state = LoadState.LOADING
            )
        }
        locationRepository.getCurrentLocation().listen(
            onError = { exception ->
                _overviewState.update {
                    OverviewState(
                        state = LoadState.ERROR,
                        error = exception.message
                    )
                }
            }
        ) { location ->
            Single.zip(
                notificationRepository.countNotifications(),
                weatherRepository.getCurrentWeatherInfo(location.latitude, location.longitude),
                weatherRepository.getWeatherInfoIn24h(location.latitude, location.longitude),
            ) { notifications, current, next24h ->
                Triple(notifications, current, next24h)
            }.listen(
                onError = { exception ->
                    _overviewState.update {
                        OverviewState(
                            state = LoadState.ERROR,
                            error = exception.message
                        )
                    }
                }
            ) { result ->
                val notifications = result.first
                val current = result.second
                val next24h = result.third
                suggestionRepository.getShortSuggestion(current.airQuality).listen(
                    onError = { exception ->
                        _overviewState.update {
                            OverviewState(
                                state = LoadState.ERROR,
                                error = exception.message
                            )
                        }
                    }
                ) { suggestion ->
                    _overviewState.update {
                        OverviewState(
                            state = LoadState.LOADED,
                            notifications = notifications,
                            location = location,
                            suggestion = suggestion,
                            weatherCurrent = current,
                            weather24h = next24h
                        )
                    }

                }

            }
        }

    }

    fun setNotificationCount(count: Int) {
        _overviewState.update {
            it.copy(
                notifications = count
            )
        }
    }
}