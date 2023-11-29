package com.nhom1.oxygen.repository.mock

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

class UserRepositoryMock : UserRepository {
    private var userDataMock = OUser(
        avatar = "https://upload.wikimedia.org/wikipedia/commons/4/48/RedCat_8727.jpg",
        diseases = listOf(
            ODisease(
                name = "Lậu"
            ),
            ODisease(
                name = "Giang mai"
            ),
            ODisease(
                name = "AIDS"
            )
        ),
        email = "anhquan7826@gmail.com",
        name = "Anh Quan",
        profile = null,
    )

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

    override fun setUserAvatar(avatar: Uri, contentResolver: ContentResolver): Completable {
        return Completable.create { emitter ->
            contentResolver.openFileDescriptor(avatar, "r").use { descriptor ->
                descriptor?.fileDescriptor?.toRequestBody("multipart/form-data".toMediaTypeOrNull())?.let { requestBody ->
                    val multipart = MultipartBody.Part.createFormData("image", "avatar", requestBody)
                    emitter.onComplete()
                }
            }
        }
    }

    override fun setUserDiseases(weight: Double, height: Double, diseases: List<String>): Completable {
        return Completable.create { it.onComplete() }.delay(1, TimeUnit.SECONDS)
    }

    override fun signOut() {
        Firebase.auth.signOut()
    }
}