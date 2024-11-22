package com.example.ny_news.data.remote.model

import com.example.ny_news.domain.model.NewsResult

data class NewsResultDto(val subsection: String,
                         val title: String,
                         val abstract: String,
                         val url: String,
                         val multimedia: List<MultimediaDto>
) {
    fun toNewsResult(): NewsResult {
        return NewsResult(subsection = subsection,
                    title = title,
                    abstract = abstract,
                    url = url,
                    multimedia = if (multimedia.isNotEmpty()) multimedia.map { it.toMultimedia() } else emptyList() )
    }
}
