package com.example.ny_news.data.reository

import com.example.ny_news.data.remote.api.NyTimesApi
import com.example.ny_news.domain.model.News
import com.example.ny_news.domain.repository.NewsRepository

class NewsRepositoryIml(private val api: NyTimesApi): NewsRepository {
    override suspend fun getNews(): List<News> {
        return api.getNews().map { it.toNews() }
    }
}