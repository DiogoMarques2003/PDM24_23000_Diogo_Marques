package com.example.firebasestore.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.example.firebasestore.ui.navigation.NavigationPaths
import com.example.firebasestore.ui.viewModels.NavigationBarViewModel

@Composable
fun BottomNavigationBar(
    navController: NavController,
    viewModel: NavigationBarViewModel
) {
    var selectedItem by remember { mutableIntStateOf(0) }

    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Produtos") },
            label = { Text("Produtos") },
            selected = selectedItem == 0,
            onClick = {
                selectedItem = 0
                navController.navigate(NavigationPaths.PRODUCT_LIST)
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.ShoppingCart, contentDescription = "Carrinho") },
            label = { Text("Carrinho") },
            selected = selectedItem == 1,
            onClick = {
                selectedItem = 1
                navController.navigate(NavigationPaths.CART)
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Sair") },
            label = { Text("Sair") },
            selected = selectedItem == 2,
            onClick = {
                selectedItem = 2
                viewModel.logoutAndClearCache()
                navController.navigate(NavigationPaths.LOGIN) {
                    popUpTo(0) { inclusive = true }
                }
            }
        )
    }
}