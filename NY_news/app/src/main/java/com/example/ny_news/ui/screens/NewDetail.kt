package com.example.ny_news.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.example.ny_news.presentation.news_list.NewDetailViewModel
import com.example.ny_news.ui.NewDetailContent
import com.example.ny_news.ui.RollbackButton

@Composable
fun NewDetail(modifier: Modifier = Modifier, navBackStackEntry: NavBackStackEntry, newDetailViewModel: NewDetailViewModel, navController : NavController) {
    val context = LocalContext.current
    val newId = navBackStackEntry.arguments?.getString("newId")

    val isLoading by newDetailViewModel.isLoading.collectAsState()
    val newDetail by newDetailViewModel.newDetail.collectAsState()

    // "customizar" o código do voltar botão de voltar para trás do android
    BackHandler(enabled = true) {
        newDetailViewModel.clearData()
        navController.popBackStack()
    }

    // Verificar se existe id e obter os dados
    LaunchedEffect(newId) {
        if (newId == null) {
            Toast.makeText(context, "Notícia não encontrada", Toast.LENGTH_SHORT).show()
            newDetailViewModel.clearData()
            navController.popBackStack()
        } else {
            newDetailViewModel.fetchNewDetail(newId)
        }
    }

    // Verificar se encontrou a notificia
    if (!isLoading && newDetail == null && newId != null) {
        LaunchedEffect(Unit) {
            Toast.makeText(context, "Notícia não encontrada", Toast.LENGTH_SHORT).show()
            newDetailViewModel.clearData()
            navController.popBackStack()
        }
    }

    // Exibir conteúdo com base no estado de carregamento
    when {
        isLoading -> {
            // "Loading screen"
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        newDetail != null -> {
            // Exibir os detalhes da notícia
            Column(modifier = modifier.fillMaxWidth()) {
                RollbackButton(navController, newDetailViewModel)
                NewDetailContent(detail = newDetail!!, context = context)
            }
        }
    }
}