package com.nhom1.oxygen.ui.details

import androidx.lifecycle.ViewModel
import com.nhom1.oxygen.data.model.location.OLocation
import com.nhom1.oxygen.data.model.weather.OWeather
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
class DetailsViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
) : ViewModel() {
    data class DetailState(
        val state: LoadState = LoadState.LOADING,
        val weatherCurrent: OWeather? = null,
        val weather24h: List<OWeather>? = null,
        val weather7d: List<OWeather>? = null,
        val error: String? = null,
    )

    private val _state = MutableStateFlow(DetailState())
    val state = _state.asStateFlow()

    fun load(location: OLocation) {
        _state.update { DetailState() }
        Single.zip(
            weatherRepository.getCurrentWeatherInfo(location),
            weatherRepository.getWeatherInfoIn24h(location),
            weatherRepository.getWeatherInfoIn7d(location)
        ) { current, next24h, next7d ->
            DetailState(
                state = LoadState.LOADED,
                weatherCurrent = current,
                weather24h = next24h,
                weather7d = next7d
            )
        }.listen(
            onError = { exception ->
                _state.update {
                    DetailState(
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