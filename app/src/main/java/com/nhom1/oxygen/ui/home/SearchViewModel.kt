package com.nhom1.oxygen.ui.home

import androidx.lifecycle.ViewModel
import com.nhom1.oxygen.data.model.location.OLocation
import com.nhom1.oxygen.data.model.weather.OWeather
import com.nhom1.oxygen.repository.LocationRepository
import com.nhom1.oxygen.repository.WeatherRepository
import com.nhom1.oxygen.utils.listen
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    locationRepository: LocationRepository,
    weatherRepository: WeatherRepository
) : ViewModel() {
    data class SearchState(
        val searchValue: String,
        val result: List<Pair<OLocation, OWeather>>
    )

    private val _state = MutableStateFlow(SearchState("", listOf()))
    val state = _state.asStateFlow()

    private lateinit var emitter: ObservableEmitter<String>

    init {
        Observable.create { emitter = it }.debounce(1000, TimeUnit.MILLISECONDS).listen { query ->
            if (query.isEmpty()) {
                _state.update {
                    SearchState("", listOf())
                }
            } else {
                locationRepository.findLocation(query).listen { locations ->
                    Single.zip(
                        locations.map { location ->
                            weatherRepository.getCurrentWeatherInfo(location)
                        }
                    ) {
                        it.map { e -> e as OWeather }
                    }.listen { weathers ->
                        _state.update {
                            SearchState(
                                searchValue = query,
                                result = List(locations.size) { index ->
                                    Pair(locations[index], weathers[index])
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    fun onQueryChanged(value: String) {
        emitter.onNext(value)
    }
}