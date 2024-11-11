package com.example.ny_news.domain.repository

import com.example.ny_news.domain.model.News

interface NewsRepository {
    suspend fun getNews(): List<News>
}