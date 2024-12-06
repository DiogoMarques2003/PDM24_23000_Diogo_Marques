package com.example.ny_news.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.ny_news.presentation.news_list.NewsListViewModel
import com.example.ny_news.ui.New

@Composable
fun NewsScreen(modifier: Modifier = Modifier, newsListViewModel: NewsListViewModel, navController : NavController) {
    val news = newsListViewModel.news.collectAsState()

    // Obter as noticias
    LaunchedEffect(Unit) {
        newsListViewModel.fetchNews()
    }

    LazyColumn (modifier = modifier.fillMaxSize()) {
        items(news.value.results) { new ->
            New(new, navController)
        }
    }
}