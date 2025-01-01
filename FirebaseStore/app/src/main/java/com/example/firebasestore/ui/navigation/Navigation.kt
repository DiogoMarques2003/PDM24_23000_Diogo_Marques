package com.example.firebasestore.ui.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.firebasestore.data.database.AppDatabase
import com.example.firebasestore.data.firebase.FirebaseAutentication
import com.example.firebasestore.ui.components.BottomNavigationBar
import com.example.firebasestore.ui.screens.LoginScreen
import com.example.firebasestore.ui.screens.ProductListScreen
import com.example.firebasestore.ui.screens.ProductScreen
import com.example.firebasestore.ui.screens.RegisterScreen
import com.example.firebasestore.ui.viewModels.LoginViewModel
import com.example.firebasestore.ui.viewModels.NavigationBarViewModel
import com.example.firebasestore.ui.viewModels.ProductListViewModel
import com.example.firebasestore.ui.viewModels.ProductViewModel
import com.example.firebasestore.ui.viewModels.RegisterViewModel

@Composable
fun Navigation() {
    val context = LocalContext.current
    val navController = rememberNavController()
    val database = AppDatabase.getDatabase(LocalContext.current)
    val screensShowBottomBar = listOf(NavigationPaths.ProductList)
    val navigationBarViewModel = NavigationBarViewModel(database)

    var displayScreen by remember { mutableStateOf(NavigationPaths.Login) }
    if (FirebaseAutentication.getCurrentUser()?.uid != null) {
        displayScreen = NavigationPaths.ProductList
    }

    Scaffold(
        bottomBar = {
            val currentRoute =
                navController.currentBackStackEntryAsState().value?.destination?.route
            if (currentRoute in screensShowBottomBar) BottomNavigationBar(
                navController,
                navigationBarViewModel
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        val modifier = Modifier
            .padding(innerPadding)
            .padding(start = 16.dp, end = 16.dp)

        NavHost(navController = navController, startDestination = displayScreen) {
            composable(NavigationPaths.Login) {
                val viewModel = LoginViewModel(database, navController)
                LoginScreen(modifier, navController, viewModel)
            }

            composable(NavigationPaths.Register) {
                val viewModel = RegisterViewModel(database, navController)
                RegisterScreen(modifier, navController, viewModel)
            }

            composable(NavigationPaths.ProductList) {
                val viewModel = ProductListViewModel(database)
                ProductListScreen(modifier, navController, viewModel)
            }

            composable("${NavigationPaths.Product}/{productId}") { backstageEntry ->
                val productId = backstageEntry.arguments?.getString("productId")

                if (productId == null) {
                    LaunchedEffect(Unit) {
                        Toast.makeText(context, "Produto não encontrado", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    }
                } else {
                    val viewModel = ProductViewModel(database, productId)
                    ProductScreen(modifier, navController, viewModel)
                }
            }
        }
    }

}