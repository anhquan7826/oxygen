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

    override fun setUserAvatar(avatar: Uri, contentResolver: ContentResolver): Completable {
        return Completable.create { emitter ->
            contentResolver.openFileDescriptor(avatar, "r").use { descriptor ->
                descriptor?.fileDescriptor?.toRequestBody("multipart/form-data".toMediaTypeOrNull())?.let { requestBody ->
                    val multipart = MultipartBody.Part.createFormData("image", "avatar", requestBody)
                    service.user.setAvatar(avatar = multipart).listen(
                        onError = { emitter.onError(it) }
                    ) {
                        emitter.onComplete()
                    }
                }
            }
        }
    }

    override fun setUserDiseases(diseases: List<String>): Completable {
        return service.user.setDiseases(
            OxygenService.User.SetDiseasesRequest(
                diseases
            )
        )
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }
}