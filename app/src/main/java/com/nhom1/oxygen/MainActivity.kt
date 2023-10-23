package com.nhom1.oxygen

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.nhom1.oxygen.ui.home.HomeActivity
import com.nhom1.oxygen.ui.landing.LandingActivity
import com.nhom1.oxygen.ui.theme.OxygenTheme

class MainActivity : ComponentActivity() {
    override fun onStart() {
        super.onStart()
        var isFirstTimeLaunch = true
        if (isFirstTimeLaunch) {
            startActivity(Intent(this, LandingActivity::class.java))
        } else {
            startActivity(Intent(this, HomeActivity::class.java))
        }
        finish()
    }
}