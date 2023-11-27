package com.nhom1.oxygen.repository.impl

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.nhom1.oxygen.common.constants.SPKeys
import com.nhom1.oxygen.data.model.user.OUser
import com.nhom1.oxygen.data.service.OxygenService
import com.nhom1.oxygen.repository.UserRepository
import com.nhom1.oxygen.utils.listen
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class UserRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val service: OxygenService
) : UserRepository {
    override fun isUserSignedIn(): Boolean {
        return firebaseAuth.currentUser != null
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
        firebaseAuth.signInWithCredential(credential).addOnSuccessListener {
            if (it.user == null) {
                onResult.invoke(null)
            } else {
                it.user!!.getIdToken(true).addOnSuccessListener { tokenResult ->
                    context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
                        .edit().putString(
                            SPKeys.token, tokenResult.token
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

    override fun getUserData(): Single<OUser> {
        return service.user.getInfo()
    }

    override fun setUserData(newUserData: OUser): Completable {
        return service.user.setInfo(
            OxygenService.User.SetInfoRequest(
                name = newUserData.name,
                profile = newUserData.profile!!
            )
        )
    }

    override fun setUserAvatar(avatar: File): Completable {
        val requestBody = avatar.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val multipart = MultipartBody.Part.createFormData("image", avatar.name, requestBody)
        return service.user.setAvatar(avatar = multipart)
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }
}