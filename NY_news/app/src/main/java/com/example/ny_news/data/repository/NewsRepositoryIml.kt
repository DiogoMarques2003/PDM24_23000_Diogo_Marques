package com.example.ny_news.data.repository

import com.example.ny_news.data.remote.api.NyTimesApi
import com.example.ny_news.domain.model.News
import com.example.ny_news.domain.repository.NewsRepository

class NewsRepositoryIml(private val api: NyTimesApi): NewsRepository {
    override suspend fun getNews(): News {
        return api.getNews().toNews()
    }
}