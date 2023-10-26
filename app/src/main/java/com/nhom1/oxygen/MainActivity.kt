package com.nhom1.oxygen

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.nhom1.oxygen.ui.home.HomeActivity
import com.nhom1.oxygen.ui.landing.LandingActivity
import com.nhom1.oxygen.utils.ConfigUtil

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ConfigUtil.setup(this)
    }

    override fun onStart() {
        super.onStart()
        val isFirstTimeLaunch = ConfigUtil.firstLaunch
        if (isFirstTimeLaunch) {
            startActivity(Intent(this, LandingActivity::class.java))
        } else {
            startActivity(Intent(this, HomeActivity::class.java))
        }
        finish()
    }
}