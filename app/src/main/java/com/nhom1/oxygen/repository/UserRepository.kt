package com.nhom1.oxygen.repository

import android.content.Context
import android.content.Intent
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult

interface UserRepository {
    fun isUserSignedIn(): Boolean

    fun getSignInIntent(context: Context): Intent

    fun signInWithCredential(credential: AuthCredential, onResult: (AuthResult) -> Unit)

    fun isSignedIn(): Boolean

    fun signOut()
}