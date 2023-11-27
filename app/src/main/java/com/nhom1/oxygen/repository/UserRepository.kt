package com.nhom1.oxygen.repository

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.google.firebase.auth.AuthCredential
import com.nhom1.oxygen.data.model.user.OUser
import com.nhom1.oxygen.data.service.OxygenService
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface UserRepository {

    fun getSignInIntent(context: Context): Intent

    fun signInWithCredential(context: Context, credential: AuthCredential, onResult: (OxygenService.User.OnSignInResult?) -> Unit)

    fun isSignedIn(): Boolean

    fun getUserData(): Single<OUser>

    fun setUserData(newUserData: OUser): Completable

    fun setUserAvatar(avatar: Uri, contentResolver: ContentResolver): Completable

    fun setUserDiseases(weight: Double, height: Double, diseases: List<String>): Completable

    fun signOut()
}