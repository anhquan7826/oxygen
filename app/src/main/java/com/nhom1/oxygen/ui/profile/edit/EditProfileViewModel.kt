package com.nhom1.oxygen.ui.profile.edit

import androidx.lifecycle.ViewModel
import com.nhom1.oxygen.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
}