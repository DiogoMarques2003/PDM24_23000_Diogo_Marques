package com.example.ny_news.domain.model

data class News(val subsection: String,
                val title: String,
                val abstract: String,
                val url: String,
                val multimedia: List<Multimedia>)
