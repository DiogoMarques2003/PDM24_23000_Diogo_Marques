package com.example.ny_news

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import com.example.ny_news.presentation.news_list.NewsListViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    val newsListViewModel = NewsListViewModel()

    val news = newsListViewModel.news.collectAsState()

    newsListViewModel.fetchNews()

    LazyColumn (Modifier.fillMaxSize()) {
        items(news.value.results) { news ->
            ElevatedCard (Modifier.fillMaxSize()) {
                Text(news.title)
            }

            if (news.multimedia.isNotEmpty()) {
                AsyncImage(model = rememberAsyncImagePainter(news.multimedia.first().url),
                    contentDescription = news.multimedia.first().caption,
                    modifier = Modifier.size(128.dp))
            }

            Spacer(Modifier.size(10.dp))
        }
    }
}