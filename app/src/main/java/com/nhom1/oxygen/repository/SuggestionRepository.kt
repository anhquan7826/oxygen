package com.nhom1.oxygen.repository

import io.reactivex.rxjava3.core.Single

interface SuggestionRepository {
    fun getShortSuggestion(): Single<String>

    fun getLongSuggestion(): Single<List<String>>
}