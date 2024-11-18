package com.example.ny_news.domain.model

data class News(val section: String,
                val results: List<NewsResult>)
