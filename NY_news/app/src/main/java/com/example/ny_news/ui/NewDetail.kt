package com.example.ny_news.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.ny_news.common.dateTimeFormater
import com.example.ny_news.domain.model.NewsResult

@Composable
fun NewDetailContent(detail: NewsResult, context: Context) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        // Imagem de destaque
        if (detail.multimedia.isNotEmpty()) {
            ImageDisplay(
                multimedia = detail.multimedia
            )
        }

        Text(
            text = detail.title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = detail.abstract,
            style = MaterialTheme.typography.bodyMedium,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Column(
            modifier = Modifier
                .padding(bottom = 16.dp)
        ) {
            Text(
                text = "Autor: ${detail.byline}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Publicado em: ${dateTimeFormater(detail.published_date)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .clickable {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(detail.url))
                    context.startActivity(intent)
                }
        ) {
            Text(
                text = "Ler notícia",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .padding(16.dp)
                    .padding(horizontal = 24.dp, vertical = 12.dp)
            )
        }
    }
}