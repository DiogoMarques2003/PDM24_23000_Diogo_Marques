package com.example.ny_news.domain.use_case

import com.example.ny_news.domain.model.News
import com.example.ny_news.domain.repository.NewsRepository

class GetNewsUseCase(private val repository: NewsRepository) {
    suspend operator fun invoke(): News {
        return repository.getNews()
    }
}