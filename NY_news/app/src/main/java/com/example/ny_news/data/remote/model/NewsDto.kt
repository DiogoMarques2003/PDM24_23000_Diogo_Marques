package com.example.ny_news.data.remote.model

import com.example.ny_news.domain.model.News

data class NewsDto(val section: String,
                   val results: List<NewsResultDto>
) {
    fun toNews(): News {
        return News(section = section,
                    results = results.map { it.toNewsResult() })
    }
}
