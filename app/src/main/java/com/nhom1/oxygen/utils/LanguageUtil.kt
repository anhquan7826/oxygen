package com.nhom1.oxygen.utils

import android.content.Context
import android.content.res.Configuration
import com.nhom1.oxygen.utils.constants.OLanguage
import java.util.Locale

object LanguageUtil {
    fun changeLanguage(context: Context, language: OLanguage): Context {
        val locale = when (language) {
            OLanguage.VIETNAMESE -> Locale("vi")
            OLanguage.ENGLISH -> Locale("en")
            else -> Locale.getDefault()
        }
        val config = Configuration()
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }
}