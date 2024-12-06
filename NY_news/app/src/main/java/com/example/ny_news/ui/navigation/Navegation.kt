package com.example.ny_news.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ny_news.presentation.news_list.NewDetailViewModel
import com.example.ny_news.presentation.news_list.NewsListViewModel
import com.example.ny_news.ui.screens.NewDetail
import com.example.ny_news.ui.screens.NewsScreen

@Composable
fun Navegation(modifier: Modifier) {
    val navController = rememberNavController()

    // Criar models
    val newsListViewModel = NewsListViewModel()
    val newDetailViewModel = NewDetailViewModel()

    // criar "Rotas" dos ecrãs
    NavHost(navController = navController, startDestination = "news") {
        composable("news") {
            NewsScreen(modifier, newsListViewModel, navController)
        }

        composable("new/{newId}") { backStackEntry ->
            NewDetail(modifier, backStackEntry, newDetailViewModel, navController)
        }
    }
}