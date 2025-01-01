package com.example.firebasestore.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.SubcomposeAsyncImage
import com.example.firebasestore.R
import com.example.firebasestore.data.entity.Product
import com.example.firebasestore.ui.navigation.NavigationPaths
import com.example.firebasestore.ui.viewModels.ProductListViewModel

@Composable
fun ProductListScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ProductListViewModel
) {
    val context = LocalContext.current

    val allProducts by viewModel.allProducts.collectAsState(emptyList())
    val allCategories by viewModel.allCategories.collectAsState(emptyList())
    val allProductImages by viewModel.allProductImages.collectAsState(emptyList())

    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var displayProducts by remember { mutableStateOf<List<Product>>(emptyList()) }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.stopListeners()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getData(context)
    }

    LaunchedEffect(selectedCategory, allProducts) {
        displayProducts =
            if (selectedCategory == null) allProducts
            else allProducts.filter { it.categoryID == selectedCategory }
    }

    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                "Lista de Produtos",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        LazyRow(Modifier.fillMaxWidth()) {
            items(allCategories) { category ->
                AssistChip(
                    onClick = {
                        selectedCategory = if (selectedCategory == category.id) null
                        else category.id
                    },
                    label = {
                        Text(
                            text = category.nome,
                            color = if (selectedCategory == category.id) MaterialTheme.colorScheme.onPrimary
                            else MaterialTheme.colorScheme.onSurface
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = if (selectedCategory == category.id) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surface
                    )

                )
                Spacer(Modifier.size(8.dp))
            }
        }

        Spacer(Modifier.size(8.dp))
        if (displayProducts.isEmpty()) {
            Text(
                "Produtos não encontrados",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize()
            ) {
                items(displayProducts) { product ->
                    val productImage =
                        allProductImages.firstOrNull { it.productId == product.id && it.image != null }?.image
                            ?: R.drawable.image_default

                    ElevatedCard(
                        modifier = Modifier
                            .size(height = 200.dp, width = 200.dp)
                            .padding(8.dp),
                        onClick = { navController.navigate("${NavigationPaths.Product}/${product.id}") }
                    ) {
                        Row(modifier = Modifier.weight(1f)) {
                            SubcomposeAsyncImage(
                                model = productImage,
                                contentDescription = "Imagem produto ${product.name}",
                                loading = { CircularProgressIndicator() },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Column(Modifier.padding(5.dp)) {
                            Text(
                                product.name,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                "Preço: ${product.price}€",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}