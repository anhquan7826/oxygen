package com.nhom1.oxygen.ui.home

import androidx.lifecycle.ViewModel
import com.nhom1.oxygen.data.model.user.OUser
import com.nhom1.oxygen.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    fun getUserData(): OUser {
        return userRepository.getUserData()
    }
}