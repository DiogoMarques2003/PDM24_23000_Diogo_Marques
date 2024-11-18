package com.example.ny_news.presentation.news_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ny_news.data.remote.RetrofitInstance
import com.example.ny_news.data.repository.NewsRepositoryIml
import com.example.ny_news.domain.model.News
import com.example.ny_news.domain.use_case.GetNewsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class NewsListViewModel: ViewModel() {
    private val api = RetrofitInstance.api
    private val repository = NewsRepositoryIml(api)
    private val getNewsUseCase = GetNewsUseCase(repository)

    val news = MutableStateFlow(News(section = "",
                                     results = emptyList()))

    fun fetchNews() {
        viewModelScope.launch {
            try {
                news.value = getNewsUseCase()
            } catch (e: Exception) {
                news.value = News(section = "",
                                  results = emptyList())
            }
        }
    }
}