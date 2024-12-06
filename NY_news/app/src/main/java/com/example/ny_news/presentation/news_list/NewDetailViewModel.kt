package com.example.ny_news.presentation.news_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ny_news.data.remote.RetrofitInstance
import com.example.ny_news.data.repository.NewsRepositoryIml
import com.example.ny_news.domain.model.NewsResult
import com.example.ny_news.domain.use_case.GetNewsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class NewDetailViewModel : ViewModel() {
    private val api = RetrofitInstance.api
    private val repository = NewsRepositoryIml(api)
    private val getNewsUseCase = GetNewsUseCase(repository)

    val newDetail = MutableStateFlow<NewsResult?>(null)
    val isLoading = MutableStateFlow(true)

    fun fetchNewDetail(newId: String) {
        viewModelScope.launch {
            try {
                val news = getNewsUseCase()
                newDetail.value = news.results.firstOrNull{ it.id == newId}
            } catch (e: Exception) {
                newDetail.value = null
            } finally {
                isLoading.value = false
            }
        }
    }

    fun clearData() {
        newDetail.value = null
        isLoading.value = true
    }
}