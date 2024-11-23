package com.example.ny_news.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.ny_news.domain.model.Multimedia

@Composable
fun ImageDisplay(multimedia: List<Multimedia>) {
    // Variável para saber qual imagem mostrar
    var currentImageIndex = remember { mutableIntStateOf(0) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Button(
            onClick = {
                if (currentImageIndex.value > 0) {
                    currentImageIndex.value--
                }
            },
            enabled = (currentImageIndex.value > 0),
            shape = RoundedCornerShape(100),
            modifier = Modifier
                .size(50.dp)
        ) {
            Text("<", style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
        }

        AsyncImage(
            model = multimedia[currentImageIndex.value].url,
            contentDescription = multimedia[currentImageIndex.value].caption,
            modifier = Modifier
                .weight(1f)
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp))
        )

        Button(
            onClick = {
                if (currentImageIndex.value < multimedia.size - 1) {
                    currentImageIndex.value++
                }
            },
            enabled = (currentImageIndex.value < multimedia.size - 1),
            shape = RoundedCornerShape(100),
            modifier = Modifier
                .size(50.dp)
        ) {
            Text(">", style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
        }
    }
}