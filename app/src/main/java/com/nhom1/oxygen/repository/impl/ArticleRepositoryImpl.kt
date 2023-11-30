package com.nhom1.oxygen.repository.impl

import com.nhom1.oxygen.data.model.article.OArticle
import com.nhom1.oxygen.data.service.OxygenService
import com.nhom1.oxygen.repository.ArticleRepository
import com.nhom1.oxygen.utils.debugLog
import com.nhom1.oxygen.utils.now
import io.reactivex.rxjava3.core.Single

class ArticleRepositoryImpl(private val service: OxygenService) : ArticleRepository {
    data class CachedArticles(
        val time: Long,
        val articles: List<OArticle>
    )

    private val interval = 300

    private lateinit var cachedArticles: CachedArticles

    override fun getArticle(): Single<List<OArticle>> {
        return when {
            !this::cachedArticles.isInitialized || now() - cachedArticles.time > interval -> {
                debugLog("${this::class.simpleName}: getArticle: new!")
                service.article.getArticles().doOnSuccess {
                    cachedArticles = CachedArticles(
                        now(),
                        it
                    )
                }
            }

            else -> {
                debugLog("${this::class.simpleName}: getArticle: cached!")
                Single.create { it.onSuccess(cachedArticles.articles) }
            }
        }
    }

    override fun findArticle(query: String): Single<List<OArticle>> {
        return service.article.findArticles(query)
    }
}