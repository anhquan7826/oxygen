package com.nhom1.oxygen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.nhom1.oxygen.repository.LocationRepository
import com.nhom1.oxygen.repository.UserRepository
import com.nhom1.oxygen.ui.home.HomeActivity
import com.nhom1.oxygen.ui.landing.LandingActivity
import com.nhom1.oxygen.ui.login.LoginActivity
import com.nhom1.oxygen.utils.FirebaseUtil
import com.nhom1.oxygen.utils.LanguageUtil
import com.nhom1.oxygen.utils.constants.SPKeys
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var locationRepository: LocationRepository

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences(packageName, Context.MODE_PRIVATE)
        LanguageUtil.setLanguage(this)
    }

    @SuppressLint("MissingPermission")
    override fun onStart() {
        super.onStart()
        FirebaseUtil.refreshIdTokenIfNeeded(this)
        MainService.startService(this)
        val isFirstTimeLaunch = sharedPreferences.getBoolean(SPKeys.FIRST_LAUNCH, true)
        if (isFirstTimeLaunch) {
            startActivity(Intent(this, LandingActivity::class.java))
        } else if (!userRepository.isSignedIn()) {
            startActivity(Intent(this, LoginActivity::class.java))
        } else {
            startActivity(Intent(this, HomeActivity::class.java).apply {
                intent.extras?.let { putExtras(it) }
            })
        }
        finish()
    }
}