package com.nhom1.oxygen.ui.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.nhom1.oxygen.common.theme.OxygenTheme
import org.osmdroid.views.MapView

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OxygenTheme {
                Surface {
                    HomeView()
                }
            }
        }
    }

    @Composable
    private fun HomeView() {
        AndroidView(
            factory = {
                MapView(it)
            }
        )
    }
}