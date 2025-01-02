package com.example.firebasestore.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.firebasestore.ui.components.AddProductPopup
import com.example.firebasestore.ui.components.ImageDisplay
import com.example.firebasestore.ui.components.LoadIndicator
import com.example.firebasestore.ui.components.PopBackButton
import com.example.firebasestore.ui.viewModels.ProductViewModel

@Composable
fun ProductScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ProductViewModel
) {
    val context = LocalContext.current

    val product by viewModel.product.collectAsState(null)
    val productImages by viewModel.productImages.collectAsState(emptyList())
    val showPopup by viewModel.showPopup.collectAsState(false)
    val isLoading by viewModel.isLoading.collectAsState(false)

    DisposableEffect(Unit) {
        onDispose {
            viewModel.stopListeners()
        }
    }

    LaunchedEffect(Unit) {
        //Get fresh data
        viewModel.getData(context)
    }

    if (isLoading) {
        LoadIndicator(modifier)
    }

    if (showPopup) {
        AddProductPopup(viewModel)
    }

    Box(modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(bottom = 60.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PopBackButton(navController)
                Spacer(Modifier.size(8.dp))
                Text(
                    "Detalhes do produto",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(Modifier.size(8.dp))

            if (product == null || productImages.isEmpty()) {
                LoadIndicator()
            } else {
                ImageDisplay(productImages)

                Text(
                    text = product!!.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = product!!.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Preço: ${product!!.price}€",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }

        Button(
            onClick = { viewModel.checkIfUserHaveCarts(context) },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomEnd)
        ) {
            Text(text = "Adicionar ao Carrinho")
        }
    }
}