package com.nhom1.oxygen.ui.login

import android.net.Uri

/**
 * state code meaning:
 * 0: not logged in.
 * 1: logging in.
 * 2: logged in.
 * 3: login failed.
 */
data class LoginState(
    val state: Int,
    val username: String? = null,
    val profilePicture: Uri? = null,
)
