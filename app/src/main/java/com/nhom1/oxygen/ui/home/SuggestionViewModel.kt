package com.nhom1.oxygen.ui.home

import androidx.lifecycle.ViewModel
import com.nhom1.oxygen.data.model.article.OArticle
import com.nhom1.oxygen.data.model.weather.OWeather
import com.nhom1.oxygen.repository.ArticleRepository
import com.nhom1.oxygen.repository.LocationRepository
import com.nhom1.oxygen.repository.SuggestionRepository
import com.nhom1.oxygen.repository.UserRepository
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
class SuggestionViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val articleRepository: ArticleRepository,
    private val locationRepository: LocationRepository,
    private val userRepository: UserRepository,
    private val suggestionRepository: SuggestionRepository
) : ViewModel() {
    data class SuggestionState(
        val state: LoadState = LoadState.LOADING,
        val weather: OWeather? = null,
        val suggestions: List<String>? = null,
        val articles: List<OArticle>? = null,
        val error: String? = null
    )

    private val _state = MutableStateFlow(SuggestionState())
    val state = _state.asStateFlow()

    fun load() {
        _state.update {
            SuggestionState()
        }
        locationRepository.getCurrentLocation().listen(
            onError = { exception ->
                _state.update {
                    SuggestionState(
                        state = LoadState.ERROR,
                        error = exception.message
                    )
                }
            }
        ) { location ->
            Single.zip(
                weatherRepository.getCurrentWeatherInfo(location.latitude, location.longitude),
                articleRepository.getArticle(),
                userRepository.getUserData()
            ) { weather, articles, userData ->
                Triple(weather, articles, userData)
            }.listen(
                onError = { exception ->
                    _state.update {
                        SuggestionState(
                            state = LoadState.ERROR,
                            error = exception.message
                        )
                    }
                }
            ) { result ->
                val weather = result.first
                val articles = result.second
                val userData = result.third
                suggestionRepository.getLongSuggestion(
                    airQuality = weather.airQuality,
                    diseases = userData.diseases?.map { e -> e.name } ?: listOf()
                ).listen(
                    onError = { exception ->
                        _state.update {
                            SuggestionState(
                                state = LoadState.ERROR,
                                error = exception.message
                            )
                        }
                    }
                ) { suggestions ->
                    _state.update {
                        SuggestionState(
                            state = LoadState.LOADED,
                            weather = weather,
                            suggestions = suggestions,
                            articles = articles
                        )
                    }
                }
            }
        }
    }
}