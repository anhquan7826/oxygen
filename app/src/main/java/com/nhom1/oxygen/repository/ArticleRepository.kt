package com.nhom1.oxygen.repository

import com.nhom1.oxygen.data.model.article.OArticle
import io.reactivex.rxjava3.core.Single

interface ArticleRepository {
    fun getArticle(): Single<List<OArticle>>

    fun findArticle(query: String): Single<List<OArticle>>
}