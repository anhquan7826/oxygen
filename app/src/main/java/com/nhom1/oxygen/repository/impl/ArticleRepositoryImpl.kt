package com.nhom1.oxygen.repository.impl

import com.nhom1.oxygen.data.model.article.OArticle
import com.nhom1.oxygen.data.service.OxygenService
import com.nhom1.oxygen.repository.ArticleRepository
import io.reactivex.rxjava3.core.Single

class ArticleRepositoryImpl(private val service: OxygenService) : ArticleRepository {
    override fun getArticle(): Single<List<OArticle>> {
        return service.article.getArticles()
    }

    override fun findArticle(query: String): Single<List<OArticle>> {
        return service.article.findArticles(query)
    }
}