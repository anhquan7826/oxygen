package com.nhom1.oxygen.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import com.nhom1.oxygen.MainService
import com.nhom1.oxygen.data.model.article.OArticle
import com.nhom1.oxygen.data.model.history.OHistory
import com.nhom1.oxygen.data.model.location.OLocation
import com.nhom1.oxygen.data.model.user.OUser
import com.nhom1.oxygen.data.model.weather.OWeather
import com.nhom1.oxygen.repository.ArticleRepository
import com.nhom1.oxygen.repository.HistoryRepository
import com.nhom1.oxygen.repository.LocationRepository
import com.nhom1.oxygen.repository.NotificationRepository
import com.nhom1.oxygen.repository.SettingRepository
import com.nhom1.oxygen.repository.SuggestionRepository
import com.nhom1.oxygen.repository.UserRepository
import com.nhom1.oxygen.repository.WeatherRepository
import com.nhom1.oxygen.repository.impl.ArticleRepositoryImpl
import com.nhom1.oxygen.utils.constants.LoadState
import com.nhom1.oxygen.utils.listen
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val notificationRepository: NotificationRepository,
    private val locationRepository: LocationRepository,
    private val articleRepository: ArticleRepository,
    private val suggestionRepository: SuggestionRepository,
    private val historyRepository: HistoryRepository,
    private val userRepository: UserRepository,
    private val settingRepository: SettingRepository
) : ViewModel() {
    data class HomeState(
        val state: LoadState = LoadState.LOADING,
        val notificationCount: Int = 0,
        val currentLocation: OLocation? = null,
        val weatherCurrent: OWeather? = null,
        val weather24h: List<OWeather> = listOf(),
        val searchHistory: List<OLocation> = listOf(),
        val articles: List<OArticle> = listOf(),
        val shortSuggestion: String = "",
        val longSuggestion: List<String> = listOf(),
        val todayHistory: OHistory? = null,
        val userData: OUser? = null,
        val error: String? = null,
    )

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    fun load() {
        _state.update { HomeState() }
        locationRepository.getCurrentLocation().listen(
            onError = { e ->
                _state.update {
                    HomeState(
                        state = LoadState.ERROR,
                        error = e.message
                    )
                }
            }
        ) { location ->
            Single.zip(
                weatherRepository.getCurrentWeatherInfo(location.latitude, location.longitude),
                weatherRepository.getWeatherInfoIn24h(location.latitude, location.longitude),
                notificationRepository.countNotifications(),
                locationRepository.getSearchedLocation(),
                articleRepository.getArticle(),
                historyRepository.getTodayHistory(),
                userRepository.getUserData()
            ) { weatherCurrent, weather24h, notificationCount, searchHistory, articles, todayHistory, userData ->
                mapOf(
                    "weatherCurrent" to weatherCurrent,
                    "weather24h" to weather24h,
                    "notificationCount" to notificationCount,
                    "searchHistory" to searchHistory,
                    "articles" to articles,
                    "todayHistory" to todayHistory,
                    "userData" to userData
                )
            }.listen(
                onError = { e ->
                    _state.update {
                        HomeState(
                            state = LoadState.ERROR,
                            error = e.message
                        )
                    }
                }
            ) { map1 ->
                val airQuality = (map1["weatherCurrent"] as OWeather).airQuality
                val diseases = (map1["userData"] as OUser).diseases?.map { d -> d.name } ?: listOf()
                Single.zip(
                    suggestionRepository.getShortSuggestion(airQuality),
                    suggestionRepository.getLongSuggestion(airQuality, diseases),
                ) { shortSuggestion, longSuggestion ->
                    map1.plus(
                        mapOf(
                            "shortSuggestion" to shortSuggestion,
                            "longSuggestion" to longSuggestion
                        )
                    )
                }.listen(
                    onError = { e ->
                        _state.update {
                            HomeState(
                                state = LoadState.ERROR,
                                error = e.message
                            )
                        }
                    }
                ) { map2 ->
                    _state.update {
                        HomeState(
                            state = LoadState.LOADED,
                            notificationCount = map2["notificationCount"] as Int,
                            currentLocation = map2["currentLocation"] as OLocation,
                            weatherCurrent = map2["weatherCurrent"] as OWeather,
                            weather24h = (map2["weather24h"] as List<*>).map { e -> e as OWeather },
                            searchHistory = (map2["searchHistory"] as List<*>).map { e -> e as OLocation },
                            articles = (map2["articles"] as List<*>).map { e -> e as OArticle },
                            shortSuggestion = map2["shortSuggestion"].toString(),
                            longSuggestion = (map2["weather24h"] as List<*>).map { e -> e.toString() },
                            todayHistory = map2["todayHistory"] as OHistory,
                            userData = map2["userData"] as OUser,
                        )
                    }
                }
            }
        }
    }

    val tempUnit: Boolean get() = settingRepository.temperatureUnit

    fun setNotificationCount(count: Int) {
        _state.update {
            it.copy(
                notificationCount = count
            )
        }
    }

    fun saveLocation(location: OLocation) {
        locationRepository.addSearchedLocation(location).listen(observeOn = Schedulers.io()) {
            _state.update {
                it.copy(
                    searchHistory = (it.searchHistory ?: listOf()).plusElement(location)
                )
            }
        }
    }

    fun logout() {
        userRepository.signOut()
    }
}