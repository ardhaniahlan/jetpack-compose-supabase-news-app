package org.apps.jajanbarang.data.repository

import org.apps.jajanbarang.data.remote.NewsApiService
import org.apps.jajanbarang.domain.model.Article
import org.apps.jajanbarang.domain.repository.NewsRepository

class NewsRepositoryImpl(
    private val apiService: NewsApiService,
    private val apiKey: String
) : NewsRepository {

    override suspend fun getTopHeadlines(country: String, page: Int): Result<List<Article>> {
        return try {
            val response = apiService.getTopHeadlines(
                country = country,
                page = page,
                apiKey = apiKey
            )
            Result.success(response.articles)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchNews(query: String, page: Int): Result<List<Article>> {
        return try {
            val response = apiService.searchNews(
                q = query,
                page = page,
                apiKey = apiKey
            )
            Result.success(response.articles)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}