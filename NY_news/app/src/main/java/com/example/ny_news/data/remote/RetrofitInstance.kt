package com.example.ny_news.data.remote

import com.example.ny_news.BuildConfig
import com.example.ny_news.data.remote.api.NyTimesApi

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    // Criar um interceptor para adicionar a API key nos requests
    private val authInterceptor = Interceptor{ chain ->
        val originalRequest: Request = chain.request()
        val url = originalRequest.url.newBuilder()
            .addQueryParameter("api-key", BuildConfig.NYT_API_KEY)
            .build()

        val newRequest = originalRequest.newBuilder().url(url).build()
        chain.proceed(newRequest)
    }

    // Criar um cliente OkHttp com o interceptor
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()

    val api: NyTimesApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.nytimes.com")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NyTimesApi::class.java)
    }
}