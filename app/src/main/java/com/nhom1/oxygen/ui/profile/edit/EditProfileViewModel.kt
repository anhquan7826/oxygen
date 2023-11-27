package com.nhom1.oxygen.ui.profile.edit

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.nhom1.oxygen.data.model.user.OUser
import com.nhom1.oxygen.data.model.user.OUserProfile
import com.nhom1.oxygen.repository.LocationRepository
import com.nhom1.oxygen.repository.UserRepository
import com.nhom1.oxygen.utils.debugLog
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

        val currentAvt: Uri = Uri.EMPTY
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

    private val _divisionState = MutableStateFlow(EditProfileState())
    val divisionState = _divisionState.asStateFlow()

    private var currentAvt: Uri? = null
    private var currentName = ""
    private var currentDob: Long? = null
    private var currentSex: Boolean? = null
    private var currentProvince: String? = null
    private var currentDistrict: String? = null
    private var currentWard: String? = null
    private var currentAddress: String? = null

    fun load(initialUserData: OUser) {
        this.initialUserData = initialUserData

        currentName = initialUserData.name
        currentDob = initialUserData.profile?.dateOfBirth
        currentSex = initialUserData.profile?.sex
        currentProvince = initialUserData.profile?.province
        currentDistrict = initialUserData.profile?.district
        currentWard = initialUserData.profile?.ward
        currentAddress = initialUserData.profile?.address

        locationRepository.getProvinces().listen { provinces ->
            _divisionState.update { it.copy(provinces = provinces.associate { e -> e.id to e.name }) }
            if (currentProvince != null) {
                val provinceId = provinces.first { it.name == currentProvince }.id
                locationRepository.getDistricts(provinceId).listen { districts ->
                    _divisionState.update { it.copy(districts = districts.associate { e -> e.id to e.name }) }
                    if (currentDistrict != null) {
                        val districtId = districts.first { it.name == currentDistrict }.id
                        locationRepository.getWards(districtId).listen { wards ->
                            _divisionState.update { it.copy(wards = wards.associate { e -> e.id to e.name }) }
                        }
                    }
                }
            }
        }
    }

    fun setAvt(uri: Uri) {
        currentAvt = uri
        debugLog(uri)
    }

    fun setName(name: String) {
        currentName = name
    }

    fun setDateOfBirth(date: Long) {
        currentDob = date
    }

    fun setSex(sex: Boolean) {
        currentSex = sex
    }

    fun setProvince(provinceId: String) {
        currentProvince = _divisionState.value.provinces[provinceId]
        locationRepository.getDistricts(provinceId).listen { result ->
            _divisionState.update {
                it.copy(
                    districts = result.associate { e -> e.id to e.name }
                )
            }
        }
    }

    fun setDistrict(districtId: String) {
        currentDistrict = _divisionState.value.districts[districtId]
        locationRepository.getWards(districtId).listen { result ->
            _divisionState.update {
                it.copy(
                    wards = result.associate { e -> e.id to e.name }
                )
            }
        }
    }

    fun setWard(wardId: String) {
        currentWard = _divisionState.value.wards[wardId]
    }

    fun setAddress(address: String) {
        currentAddress = address
    }

    private fun newUserData(): OUser {
        return initialUserData.copy(
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

    fun hasModified(): Boolean {
        return when {
            currentAvt != null -> true
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

    fun canSave(): Boolean {
        return when {
            currentName.isEmpty() -> false
            currentDob == null -> false
            currentSex == null -> false
            currentProvince == null || currentProvince!!.isEmpty() -> false
            currentDistrict == null || currentDistrict!!.isEmpty() -> false
            currentWard == null || currentWard!!.isEmpty() -> false
            currentAddress == null || currentAddress!!.isEmpty() -> false
            else -> true
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
            if (currentAvt != null) {
                userRepository.setUserAvatar(currentAvt!!, context.contentResolver).listen(
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