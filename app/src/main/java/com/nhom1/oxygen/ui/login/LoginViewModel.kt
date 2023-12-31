package com.nhom1.oxygen.ui.login

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.GoogleAuthProvider
import com.nhom1.oxygen.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _loginState = MutableStateFlow(LoginState(0))
    val loginState = _loginState.asStateFlow()

    fun getSignInIntent(context: Context): Intent {
        return userRepository.getSignInIntent(context)
    }

    fun onSignInResult(context: Context, result: ActivityResult) {
        try {
            _loginState.update {
                LoginState(1)
            }
            val account = GoogleSignIn.getSignedInAccountFromIntent(result.data).result
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            userRepository.signInWithCredential(context, credential) { signInResult ->
                if (signInResult != null) {
                    _loginState.update {
                        LoginState(
                            state = 2,
                            username = signInResult.name,
                            profilePicture = Uri.parse(signInResult.avatar)
                        )
                    }
                } else {
                    _loginState.update { LoginState(0) }
                }
            }
        } catch (_: Exception) {
            _loginState.update {
                LoginState(3)
            }
        }
    }
}