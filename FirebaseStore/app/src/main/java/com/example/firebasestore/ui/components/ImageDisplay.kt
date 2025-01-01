package com.example.firebasestore.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.SubcomposeAsyncImage
import com.example.firebasestore.R
import com.example.firebasestore.data.entity.ProductImage

@Composable
fun ImageDisplay(images: List<ProductImage>) {
    var currentImageIndex by remember { mutableIntStateOf(0) }

    val multimedia = if (images.isNotEmpty()) images
    else listOf(ProductImage(id = "", productId = "", image = null))

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Button(
            onClick = {
                if (currentImageIndex > 0) {
                    currentImageIndex--
                }
            },
            enabled = (currentImageIndex > 0),
            shape = RoundedCornerShape(100),
            modifier = Modifier
                .size(50.dp)
        ) {
            Text(
                "<", style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }

        SubcomposeAsyncImage(
            model = multimedia[currentImageIndex]?.image ?: R.drawable.image_default,
            contentDescription = "Imagem ${currentImageIndex}",
            loading = { CircularProgressIndicator() },
            modifier = Modifier.weight(1f).height(260.dp)
        )

        Button(
            onClick = {
                if (currentImageIndex < multimedia.size - 1) {
                    currentImageIndex++
                }
            },
            enabled = (currentImageIndex < multimedia.size - 1),
            shape = RoundedCornerShape(100),
            modifier = Modifier
                .size(50.dp)
        ) {
            Text(
                ">", style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }
    }
}