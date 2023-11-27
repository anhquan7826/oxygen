package com.nhom1.oxygen.ui.home

import androidx.lifecycle.ViewModel
import com.nhom1.oxygen.data.model.article.OArticle
import com.nhom1.oxygen.data.model.weather.OWeather
import com.nhom1.oxygen.repository.ArticleRepository
import com.nhom1.oxygen.repository.LocationRepository
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
    private val locationRepository: LocationRepository
) : ViewModel() {
    data class SuggestionState(
        val state: LoadState = LoadState.LOADING,
        val weather: OWeather? = null,
        val articles: List<OArticle>? = null,
        val error: String? = null
    )

    private val _state = MutableStateFlow(SuggestionState())
    val state = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        _state.update {
            SuggestionState()
        }
        locationRepository.getCurrentLocation().listen { location ->
            Single.zip(
                weatherRepository.getCurrentWeatherInfo(location),
                articleRepository.getArticle()
            ) { weather, articles ->
                SuggestionState(
                    state = LoadState.LOADED,
                    weather = weather,
                    articles = articles
                )
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
                _state.update { result }
            }
        }

    }
}