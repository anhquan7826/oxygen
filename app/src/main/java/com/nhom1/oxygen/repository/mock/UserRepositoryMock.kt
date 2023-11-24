package com.nhom1.oxygen.repository.mock

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.auth
import com.nhom1.oxygen.data.model.user.ODisease
import com.nhom1.oxygen.data.model.user.OUser
import com.nhom1.oxygen.data.model.user.OUserProfile
import com.nhom1.oxygen.repository.UserRepository
import io.reactivex.rxjava3.core.Single
import java.util.concurrent.TimeUnit

class UserRepositoryMock : UserRepository {
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

    override fun signInWithCredential(credential: AuthCredential, onResult: (AuthResult) -> Unit) {
        Firebase.auth.signInWithCredential(credential).addOnSuccessListener(onResult::invoke)
    }

    override fun isSignedIn(): Boolean {
        return Firebase.auth.currentUser != null
    }

    override fun getUserData(): Single<OUser> {
        return Single.create {
            it.onSuccess(
                OUser(
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
                    profile = OUserProfile(
                        address = "lmao",
                        country = "vietnam",
                        dateOfBirth = "01-12-2002",
                        district = "alo",
                        height = 175.0,
                        province = "alo",
                        sex = true,
                        ward = "alo",
                        weight = 60.0,
                    ),
                    uid = "1"
                )
            )
        }.delay(1000, TimeUnit.MILLISECONDS)
    }

    override fun signOut() {
        Firebase.auth.signOut()
    }
}