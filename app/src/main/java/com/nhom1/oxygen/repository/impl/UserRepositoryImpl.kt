package com.nhom1.oxygen.repository.impl

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.nhom1.oxygen.repository.UserRepository

class UserRepositoryImpl(private val firebaseAuth: FirebaseAuth) : UserRepository {
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

    override fun signInWithCredential(credential: AuthCredential, onResult: (AuthResult) -> Unit) {
        firebaseAuth.signInWithCredential(credential).addOnSuccessListener(onResult::invoke)
    }

    override fun isSignedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }

}