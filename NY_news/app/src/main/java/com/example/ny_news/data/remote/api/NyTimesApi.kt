package com.example.ny_news.data.remote.api

import com.example.ny_news.data.remote.model.NewsDto
import retrofit2.http.GET

interface NyTimesApi {
    @GET("svc/topstories/v2/world.json")
    suspend fun getNews(): NewsDto
}