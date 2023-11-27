package com.nhom1.oxygen.ui.medical

import androidx.lifecycle.ViewModel
import com.nhom1.oxygen.data.model.user.OUser
import com.nhom1.oxygen.repository.PathologyRepository
import com.nhom1.oxygen.repository.UserRepository
import com.nhom1.oxygen.utils.constants.LoadState
import com.nhom1.oxygen.utils.extensions.compareIgnoreOrder
import com.nhom1.oxygen.utils.listen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.math.pow

@HiltViewModel
class DeclareMedicalHistoryViewModel @Inject constructor(
    private val pathologyRepository: PathologyRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    data class MedicalHistoryState(
        val state: LoadState = LoadState.LOADED,
        val bmi: Double? = null,
        val diseases: List<String> = listOf(),
        val error: String? = null,
    )

    private val _state = MutableStateFlow(MedicalHistoryState())
    val state = _state.asStateFlow()

    data class SaveMedicalHistoryState(
        /**
         * idle: null, saving: false, saved: true
         */
        val saved: Boolean? = null,
        val error: String? = null
    )

    private val _saveState = MutableStateFlow(SaveMedicalHistoryState())
    val saveState = _saveState.asStateFlow()

    private lateinit var initialUserData: OUser

    private var currentWeight: Double = -1.0
    private var currentHeight: Double = -1.0
    private var currentPathology: String = ""

    fun load(userData: OUser) {
        initialUserData = userData
        currentWeight = userData.profile?.weight ?: -1.0
        currentHeight = userData.profile?.height ?: -1.0
        _state.update {
            it.copy(
                diseases = initialUserData.diseases?.map { e -> e.name } ?: listOf()
            )
        }
    }

    fun setWeight(weight: Double) {
        currentWeight = weight
        _state.update {
            it.copy(
                bmi = if (currentWeight == -1.0 || currentHeight == -1.0)
                    null
                else
                    currentWeight / (currentHeight / 100).pow(2)
            )
        }
    }

    fun setHeight(height: Double) {
        currentHeight = height
        _state.update {
            it.copy(
                bmi = if (currentWeight == -1.0 || currentHeight == -1.0)
                    null
                else
                    currentWeight / (currentHeight / 100).pow(2)
            )
        }
    }

    fun setPathology(pathology: String) {
        currentPathology = pathology
    }

    fun analyzePathology() {
        _state.update {
            it.copy(
                state = LoadState.LOADING
            )
        }
        pathologyRepository.analyzePathology(currentPathology).listen(
            onError = { exception ->
                _state.update {
                    it.copy(
                        state = LoadState.ERROR,
                        error = exception.message
                    )
                }
            }
        ) { result ->
            _state.update {
                it.copy(
                    state = LoadState.LOADED,
                    diseases = result
                )
            }
        }
    }

    fun deleteDisease(disease: String) {
        _state.update {
            it.copy(
                diseases = it.diseases.filter { e -> e != disease }
            )
        }
    }

    fun canSave(): Boolean {
        return currentWeight != -1.0 && currentHeight != -1.0
    }

    fun hasModified(): Boolean {
        return when {
            (initialUserData.profile?.weight ?: -1.0) != currentWeight -> true
            (initialUserData.profile?.height ?: -1.0) != currentHeight -> true
            (initialUserData.diseases?.size ?: 0) != state.value.diseases.size -> true
            !(initialUserData.diseases ?: listOf()).map { e -> e.name }
                .compareIgnoreOrder(state.value.diseases) -> true

            else -> false
        }
    }

    fun save() {
        _saveState.update {
            SaveMedicalHistoryState(
                saved = false
            )
        }
        userRepository.setUserDiseases(currentWeight, currentHeight / 100, _state.value.diseases).listen(
            onError = { exception ->
                _saveState.update {
                    SaveMedicalHistoryState(
                        saved = null,
                        error = exception.message
                    )
                }
            }
        ) {
            _saveState.update {
                SaveMedicalHistoryState(
                    saved = true
                )
            }
        }
    }
}