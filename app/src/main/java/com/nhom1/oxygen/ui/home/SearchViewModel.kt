package com.nhom1.oxygen.ui.home

import androidx.lifecycle.ViewModel
import com.nhom1.oxygen.data.model.location.OLocation
import com.nhom1.oxygen.repository.LocationRepository
import com.nhom1.oxygen.repository.WeatherRepository
import com.nhom1.oxygen.utils.debugLog
import com.nhom1.oxygen.utils.listen
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val weatherRepository: WeatherRepository
) : ViewModel() {
    data class SearchState(
        val searchValue: String = "",
        val result: List<OLocation> = listOf(),
        val searchHistory: List<OLocation> = listOf(),
    )

    private val _state = MutableStateFlow(SearchState())
    val state = _state.asStateFlow()

    private lateinit var emitter: ObservableEmitter<String>

    init {
        load()
    }

    fun load() {
        locationRepository.getSearchedLocation().listen { locations ->
            _state.update {
                it.copy(
                    searchHistory = locations
                )
            }
        }
        Observable.create { emitter = it }.debounce(1000, TimeUnit.MILLISECONDS).listen { query ->
            if (query.isEmpty()) {
                _state.update {
                    it.copy(searchValue = "", result = listOf())
                }
            } else {
                locationRepository.findLocation(query).listen { locations ->
                    debugLog(locations)
                    if (locations.isEmpty()) {
                        _state.update {
                            it.copy(
                                searchValue = query,
                                result = listOf()
                            )
                        }
                    } else {
                        _state.update {
                            it.copy(
                                searchValue = query,
                                result = locations
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

    fun saveLocation(location: OLocation) {
        locationRepository.addSearchedLocation(location).listen(observeOn = Schedulers.io()) {
            locationRepository.getSearchedLocation().listen { locations ->
                _state.update {
                    it.copy(
                        searchHistory = locations
                    )
                }
            }
        }
    }
}