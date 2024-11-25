package com.example.ny_news.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.ny_news.common.dateTimeFormater
import com.example.ny_news.domain.model.NewsResult

@Composable
fun New(news: NewsResult) {
    val context = LocalContext.current

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
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Autor: ${news.byline}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = "Publicado em: ${dateTimeFormater(news.published_date)}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}