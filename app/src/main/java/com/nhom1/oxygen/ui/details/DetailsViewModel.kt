package com.nhom1.oxygen.ui.details

import androidx.lifecycle.ViewModel
import com.nhom1.oxygen.data.model.location.OLocation
import com.nhom1.oxygen.data.model.weather.OWeather
import com.nhom1.oxygen.repository.SettingRepository
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
    private val settingRepository: SettingRepository
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

    val tempUnit: Boolean get() = settingRepository.temperatureUnit

    fun load(
        location: OLocation,
        weatherCurrent: OWeather? = null,
        weather24h: List<OWeather>? = null,
        weather7d: List<OWeather>? = null
    ) {
        _state.update { DetailState() }
        Single.zip(
            if (weatherCurrent != null)
                Single.create { it.onSuccess(weatherCurrent) }
            else
                weatherRepository.getCurrentWeatherInfo(location.latitude, location.longitude),
            if (weather24h != null)
                Single.create { it.onSuccess(weather24h) }
            else
                weatherRepository.getWeatherInfoIn24h(location.latitude, location.longitude),
            if (weather7d != null)
                Single.create { it.onSuccess(weather7d) }
            else
                weatherRepository.getWeatherInfoIn7d(location.latitude, location.longitude)
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