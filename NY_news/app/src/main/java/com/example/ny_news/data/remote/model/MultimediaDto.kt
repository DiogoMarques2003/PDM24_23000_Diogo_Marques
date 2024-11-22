package com.example.ny_news.data.remote.model

import com.example.ny_news.domain.model.Multimedia

data class MultimediaDto(val url: String,
                         val height: Int,
                         val width: Int,
                         val type: String,
                         val caption: String
) {
    fun toMultimedia(): Multimedia {
        return Multimedia(url = url,
                          height = height,
                          width = width,
                          type = type,
                          caption = caption)
    }
}
