package com.nhom1.oxygen.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import com.nhom1.oxygen.utils.constants.OLanguage
import com.nhom1.oxygen.utils.constants.SPKeys
import java.util.Locale

object LanguageUtil {
    private lateinit var sharedPreferences: SharedPreferences

    fun setLanguage(context: Context) {
        sharedPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        val currentLang =
            sharedPreferences.getString(SPKeys.Settings.LANGUAGE, OLanguage.UNSPECIFIED)!!
        val locale =
            if (currentLang != OLanguage.UNSPECIFIED) Locale(currentLang) else Locale.getDefault()
        context.resources.updateConfiguration(
            Configuration().apply { setLocale(locale) },
            context.resources.displayMetrics
        )
    }
}