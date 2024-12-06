package com.example.ny_news.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ny_news.presentation.news_list.NewDetailViewModel

@Composable
fun RollbackButton(navController: NavController, newDetailViewModel: NewDetailViewModel) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "< Voltar",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(16.dp)
                .clickable {
                    newDetailViewModel.clearData()
                    navController.popBackStack()
                }
        )
    }
}