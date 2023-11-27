package com.nhom1.oxygen

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.nhom1.oxygen.repository.UserRepository
import com.nhom1.oxygen.ui.home.HomeActivity
import com.nhom1.oxygen.ui.landing.LandingActivity
import com.nhom1.oxygen.ui.login.LoginActivity
import com.nhom1.oxygen.utils.ConfigUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var userRepository: UserRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ConfigUtil.setup(this)
    }

    override fun onStart() {
        super.onStart()
        MainService.startService(this)
        val isFirstTimeLaunch = ConfigUtil.firstLaunch
        if (isFirstTimeLaunch) {
            startActivity(Intent(this, LandingActivity::class.java))
        } else if (!userRepository.isSignedIn()) {
            startActivity(Intent(this, LoginActivity::class.java))
        } else {
            startActivity(Intent(this, HomeActivity::class.java))
        }
        finish()
    }
}