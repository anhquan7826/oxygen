package com.nhom1.oxygen.repository.mock

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.auth
import com.nhom1.oxygen.data.model.user.ODisease
import com.nhom1.oxygen.data.model.user.OUser
import com.nhom1.oxygen.data.service.OxygenService
import com.nhom1.oxygen.repository.UserRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import java.io.File
import java.util.concurrent.TimeUnit

class UserRepositoryMock : UserRepository {
    private var userDataMock = OUser(
        id = 1,
        avt = "https://upload.wikimedia.org/wikipedia/commons/4/48/RedCat_8727.jpg",
        diseases = listOf(
            ODisease(
                id = 0,
                name = "Lậu"
            ),
            ODisease(
                id = 0,
                name = "Giang mai"
            ),
            ODisease(
                id = 0,
                name = "AIDS"
            )
        ),
        email = "anhquan7826@gmail.com",
        name = "Anh Quan",
        profile = null,
        uid = "1"
    )

    override fun isUserSignedIn(): Boolean {
        return Firebase.auth.currentUser != null
    }

    override fun getSignInIntent(context: Context): Intent {
        val googleSignInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("420419796226-ruh6hr4v86okejj87pccrejp47duv8qk.apps.googleusercontent.com")
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(context, googleSignInOption)
        return googleSignInClient.signInIntent
    }

    override fun signInWithCredential(
        context: Context,
        credential: AuthCredential,
        onResult: (OxygenService.User.OnSignInResult?) -> Unit
    ) {
        Firebase.auth.signInWithCredential(credential).addOnSuccessListener {
            onResult.invoke(
                OxygenService.User.OnSignInResult(
                    name = it.user!!.displayName!!,
                    email = it.user!!.email!!,
                    avatar = it.user!!.photoUrl.toString()
                )
            )
        }
    }

    override fun isSignedIn(): Boolean {
        return Firebase.auth.currentUser != null
    }

    override fun getUserData(): Single<OUser> {
        return Single.create {
            it.onSuccess(
                userDataMock
            )
        }.delay(1000, TimeUnit.MILLISECONDS)
    }

    override fun setUserData(newUserData: OUser): Completable {
        return Completable.create {
            userDataMock = newUserData
            it.onComplete()
        }.delay(1000, TimeUnit.MILLISECONDS)
    }

    override fun setUserAvatar(avatar: File): Completable {
        return Completable.create { it.onComplete() }.delay(500, TimeUnit.MILLISECONDS)
    }

    override fun signOut() {
        Firebase.auth.signOut()
    }
}