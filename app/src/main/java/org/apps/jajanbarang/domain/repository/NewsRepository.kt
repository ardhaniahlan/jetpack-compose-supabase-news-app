package org.apps.jajanbarang.domain.repository

import org.apps.jajanbarang.domain.model.Article

interface NewsRepository {
    suspend fun getTopHeadlines(country: String, page: Int): Result<List<Article>>
    suspend fun searchNews(query: String, page: Int): Result<List<Article>>
}