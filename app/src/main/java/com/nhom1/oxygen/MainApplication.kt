package com.nhom1.oxygen

import android.app.Application
import com.nhom1.oxygen.utils.errorLog
import dagger.hilt.android.HiltAndroidApp
import io.reactivex.rxjava3.plugins.RxJavaPlugins

@HiltAndroidApp
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        RxJavaPlugins.setErrorHandler {
            errorLog(it)
        }
    }
}