package com.example.ny_news.data.remote.model

import com.example.ny_news.domain.model.NewsResult

data class NewsResultDto(val subsection: String,
                         val title: String,
                         val abstract: String,
                         val url: String,
                         val byline: String,
                         val published_date: String,
                         val multimedia: List<MultimediaDto>
) {
    fun toNewsResult(): NewsResult {
        return NewsResult(subsection = subsection,
                    title = title,
                    abstract = abstract,
                    url = url,
                    byline = byline.substring(2).replace("and", "e"),
                    published_date = published_date,
                    multimedia = if (multimedia.isNotEmpty()) multimedia.map { it.toMultimedia() } else emptyList() )
    }
}
