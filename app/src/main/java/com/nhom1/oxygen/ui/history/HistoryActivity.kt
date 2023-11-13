package com.nhom1.oxygen.ui.history

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.nhom1.oxygen.common.theme.OxygenTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryActivity : ComponentActivity() {
    private lateinit var viewModel: HistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        viewModel = ViewModelProvider(this)[HistoryViewModel::class.java]
        setContent {
            OxygenTheme {
                Surface {
                    HistoryView()
                }
            }
        }
    }

    @Composable
    fun HistoryView() {

    }
}