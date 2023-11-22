package com.nhom1.oxygen.repository

import android.content.Context
import android.content.Intent
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.nhom1.oxygen.data.model.user.OUser
import io.reactivex.rxjava3.core.Single

interface UserRepository {
    fun isUserSignedIn(): Boolean

    fun getSignInIntent(context: Context): Intent

    fun signInWithCredential(credential: AuthCredential, onResult: (AuthResult) -> Unit)

    fun isSignedIn(): Boolean

    fun getUserData(): Single<OUser>

    fun signOut()
}