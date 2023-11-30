package com.nhom1.oxygen.utils

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.nhom1.oxygen.utils.constants.SPKeys

object FirebaseUtil {
    fun isExpired(): Boolean {
        return Firebase.auth.currentUser?.let {
            System.currentTimeMillis() - it.metadata?.lastSignInTimestamp!! >= 3300 * 1000
        } ?: true
    }

    fun refreshIdTokenIfNeeded(context: Context) {
        val user = Firebase.auth.currentUser
        user?.let {
            if (isExpired()) {
                infoLog("ID token expired or about to expire. Refreshing...")
                it.getIdToken(true)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
                                .edit().putString(
                                    SPKeys.TOKEN, task.result.token
                                ).apply()
                        } else {
                            errorLog("Token refresh failed: ${task.exception}")
                        }
                    }
            } else {
                infoLog("ID token is still valid")
            }
        } ?: run {
            infoLog("User is not signed in")
        }
    }
}