package com.example.ny_news.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.ny_news.presentation.news_list.NewsListViewModel
import com.example.ny_news.ui.ImageDisplay

@Composable
fun NewsScreen(modifier: Modifier = Modifier, newsListViewModel: NewsListViewModel) {
    val context = LocalContext.current
    val news = newsListViewModel.news.collectAsState()

    // Obter as noticias
    newsListViewModel.fetchNews()

    LazyColumn (modifier = modifier.fillMaxSize()) {
        items(news.value.results) { news ->
            ElevatedCard (
                onClick = {
                    // Abrir o browser com o link da noticia
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(news.url)
                    }
                    context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column (
                    Modifier.fillMaxWidth()
                    .padding(16.dp)) {

                    if (news.multimedia.isNotEmpty()) {
                        ImageDisplay(news.multimedia)
                    }

                    Text(
                        text = news.title,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = news.abstract,
                        style = MaterialTheme.typography.bodyMedium,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}