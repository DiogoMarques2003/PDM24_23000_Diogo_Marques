package com.example.ny_news.data.remote.model

import com.example.ny_news.domain.model.Multimedia
import com.example.ny_news.domain.model.News

data class NewsDto(val subsection: String,
                   val title: String,
                   val abstract: String,
                   val url: String,
                   val multimedia: List<Multimedia>
) {
    fun toNews(): News {
        return News(subsection = subsection,
                    title = title,
                    abstract = abstract,
                    url = url,
                    multimedia = multimedia)
    }
}
