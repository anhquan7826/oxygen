package com.nhom1.oxygen.repository.impl

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.nhom1.oxygen.data.model.user.OUser
import com.nhom1.oxygen.data.service.OxygenService
import com.nhom1.oxygen.repository.UserRepository
import com.nhom1.oxygen.utils.constants.SPKeys
import com.nhom1.oxygen.utils.listen
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class UserRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val service: OxygenService
) : UserRepository {
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
        cachedUserData = null
        firebaseAuth.signInWithCredential(credential).addOnSuccessListener {
            if (it.user == null) {
                onResult.invoke(null)
            } else {
                it.user!!.getIdToken(true).addOnSuccessListener { tokenResult ->
                    context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
                        .edit().putString(
                            SPKeys.TOKEN, tokenResult.token
                        ).apply()
                    service.user.onSignIn().listen(
                        onError = {
                            onResult.invoke(null)
                        }
                    ) { result ->
                        onResult.invoke(result)
                    }
                }
            }
        }
    }

    override fun isSignedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    private var cachedUserData: OUser? = null
    override fun getUserData(): Single<OUser> {
        return if (cachedUserData == null) {
            service.user.getInfo().map {
                cachedUserData = it
                it
            }
        } else {
            Single.create { it.onSuccess(cachedUserData!!) }
        }
    }

    override fun setUserData(newUserData: OUser): Completable {
        cachedUserData = null
        return service.user.setInfo(
            OxygenService.User.SetInfoRequest(
                name = newUserData.name,
                profile = newUserData.profile!!
            )
        )
    }

    override fun setUserAvatar(avatar: Uri, contentResolver: ContentResolver): Completable {
        cachedUserData = null
        return Completable.create { emitter ->
            contentResolver.openInputStream(avatar).use { inputStream ->
                if (inputStream != null) {
                    val part = MultipartBody.Part.createFormData(
                        "file", "avatar", inputStream.readBytes()
                            .toRequestBody(
                                "image/*".toMediaTypeOrNull(),
                                0
                            )
                    )
                    service.user.setAvatar(part).listen(
                        onError = { emitter.onError(it) }
                    ) {
                        emitter.onComplete()
                    }
                }
            }
        }
    }

    override fun setUserDiseases(
        weight: Double,
        height: Double,
        diseases: List<String>
    ): Completable {
        cachedUserData = null
        return service.user.setDiseases(
            OxygenService.User.SetDiseasesRequest(
                weight, height, diseases
            )
        )
    }

    override fun signOut() {
        cachedUserData = null
        firebaseAuth.signOut()
    }
}