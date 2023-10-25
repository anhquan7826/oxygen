package com.nhom1.oxygen.ui.home

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import com.nhom1.oxygen.common.theme.OxygenTheme

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OxygenTheme {
                Surface {
                    Text(text = "Home")
                }
            }
        }
    }
}