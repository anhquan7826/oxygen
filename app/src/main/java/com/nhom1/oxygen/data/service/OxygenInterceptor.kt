package com.nhom1.oxygen.data.service

import android.content.SharedPreferences
import com.nhom1.oxygen.utils.constants.SPKeys
import okhttp3.Interceptor
import okhttp3.Response

class OxygenInterceptor(private val sharedPreferences: SharedPreferences) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = sharedPreferences.getString(SPKeys.TOKEN, null)
        var request = chain.request()
        if (token != null) {
            request = request.newBuilder().addHeader("Authorization", "Bearer $token").build()
        }
        return chain.proceed(request)
    }
}