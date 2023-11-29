package com.nhom1.oxygen.ui.profile.edit

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.nhom1.oxygen.data.model.user.OUser
import com.nhom1.oxygen.data.model.user.OUserProfile
import com.nhom1.oxygen.repository.LocationRepository
import com.nhom1.oxygen.repository.UserRepository
import com.nhom1.oxygen.utils.errorLog
import com.nhom1.oxygen.utils.listen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    data class EditProfileState(
        val provinces: Map<String, String> = mapOf(),
        val districts: Map<String, String> = mapOf(),
        val wards: Map<String, String> = mapOf(),

        val currentAvt: Uri = Uri.EMPTY,
        val currentName: String = "",
        val currentDob: Long? = null,
        val currentSex: Boolean? = null,
        val currentProvince: String = "",
        val currentDistrict: String = "",
        val currentWard: String = "",
        val currentAddress: String = ""
    )

    data class SaveProfileState(
        /**
         * idle: null, saving: false, saved: true
         */
        val saved: Boolean? = null,
        val error: String? = null
    )

    private lateinit var initialUserData: OUser

    private val _saveState = MutableStateFlow(SaveProfileState())
    val saveState = _saveState.asStateFlow()

    private val _state = MutableStateFlow(EditProfileState())
    val state = _state.asStateFlow()

    fun load(initialUserData: OUser) {
        this.initialUserData = initialUserData

        _state.update {
            EditProfileState(
                currentName = initialUserData.name,
                currentDob = initialUserData.profile?.dateOfBirth,
                currentSex = initialUserData.profile?.sex,
                currentProvince = initialUserData.profile?.province ?: "",
                currentDistrict = initialUserData.profile?.district ?: "",
                currentWard = initialUserData.profile?.ward ?: "",
                currentAddress = initialUserData.profile?.address ?: "",
            )
        }

        _state.value.apply {
            locationRepository.getProvinces().listen { provinces ->
                _state.update { it.copy(provinces = provinces.associate { e -> e.id to e.name }) }
                if (currentProvince.isNotEmpty()) {
                    val provinceId = provinces.first { it.name == currentProvince }.id
                    locationRepository.getDistricts(provinceId).listen { districts ->
                        _state.update { it.copy(districts = districts.associate { e -> e.id to e.name }) }
                        if (currentDistrict.isNotEmpty()) {
                            val districtId = districts.first { it.name == currentDistrict }.id
                            locationRepository.getWards(districtId).listen { wards ->
                                _state.update { it.copy(wards = wards.associate { e -> e.id to e.name }) }
                            }
                        }
                    }
                }
            }
        }

    }

    fun setAvt(uri: Uri) {
        _state.update {
            it.copy(
                currentAvt = uri
            )
        }
    }

    fun setName(name: String) {
        _state.update {
            it.copy(
                currentName = name
            )
        }
    }

    fun setDateOfBirth(date: Long) {
        _state.update {
            it.copy(
                currentDob = date
            )
        }
    }

    fun setSex(sex: Boolean) {
        _state.update {
            it.copy(
                currentSex = sex
            )
        }
    }

    fun setProvince(provinceId: String) {
        _state.update {
            it.copy(
                currentProvince = it.provinces[provinceId] ?: "",
                currentDistrict = "",
                currentWard = "",
                districts = mapOf(),
                wards = mapOf()
            )
        }
        locationRepository.getDistricts(provinceId).listen { result ->
            _state.update {
                it.copy(
                    districts = result.associate { e -> e.id to e.name }
                )
            }
        }
    }

    fun setDistrict(districtId: String) {
        _state.update {
            it.copy(
                currentDistrict = it.districts[districtId] ?: "",
                currentWard = "",
                wards = mapOf()
            )
        }
        locationRepository.getWards(districtId).listen { result ->
            _state.update {
                it.copy(
                    wards = result.associate { e -> e.id to e.name }
                )
            }
        }
    }

    fun setWard(wardId: String) {
        _state.update {
            it.copy(
                currentWard = it.wards[wardId] ?: "",
            )
        }
    }

    fun setAddress(address: String) {
        _state.update {
            it.copy(
                currentAddress = address
            )
        }
    }

    private fun newUserData(): OUser {
        return _state.value.run {
            initialUserData.copy(
                name = currentName,
                profile = (initialUserData.profile ?: OUserProfile()).copy(
                    dateOfBirth = currentDob,
                    sex = currentSex,
                    province = currentProvince,
                    district = currentDistrict,
                    ward = currentWard,
                    address = currentAddress
                )
            )
        }
    }

    fun hasModified(): Boolean {
        return _state.value.run {
            when {
                currentAvt != Uri.EMPTY -> true
                currentName != initialUserData.name -> true
                currentDob != initialUserData.profile?.dateOfBirth -> true
                currentSex != initialUserData.profile?.sex -> true
                currentProvince != initialUserData.profile?.province -> true
                currentDistrict != initialUserData.profile?.district -> true
                currentWard != initialUserData.profile?.ward -> true
                currentAddress != initialUserData.profile?.address -> true
                else -> false
            }
        }
    }

    fun canSave(): Boolean {
        return _state.value.run {
            when {
                currentName.isEmpty() -> false
                currentDob == null -> false
                currentSex == null -> false
                currentProvince.isEmpty() -> false
                currentDistrict.isEmpty() -> false
                currentWard.isEmpty() -> false
                currentAddress.isEmpty() -> false
                else -> true
            }
        }
    }

    fun saveUserData(context: Context) {
        _saveState.update {
            SaveProfileState(
                saved = false
            )
        }
        userRepository.setUserData(newUserData()).listen(
            onError = { exception ->
                _saveState.update {
                    SaveProfileState(
                        error = exception.message
                    )
                }
            }
        ) {
            if (_state.value.currentAvt != Uri.EMPTY) {
                userRepository.setUserAvatar(_state.value.currentAvt, context.contentResolver)
                    .listen(
                        onError = { exception ->
                            errorLog(exception)
                            _saveState.update {
                                SaveProfileState(
                                    saved = null,
                                    error = exception.message
                                )
                            }
                        }
                    ) {
                        _saveState.update {
                            SaveProfileState(
                                saved = true
                            )
                        }
                    }
            } else {
                _saveState.update {
                    SaveProfileState(
                        saved = true
                    )
                }
            }
        }
    }
}