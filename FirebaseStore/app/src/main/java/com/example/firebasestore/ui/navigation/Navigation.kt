package com.example.firebasestore.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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
import com.example.firebasestore.ui.screens.RegisterScreen
import com.example.firebasestore.ui.viewModels.LoginViewModel
import com.example.firebasestore.ui.viewModels.RegisterViewModel

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val database = AppDatabase.getDatabase(LocalContext.current)
    val screensShowBottomBar = listOf(NavigationPaths.ProductList)

    Scaffold(
        bottomBar = {
            val currentRoute =
                navController.currentBackStackEntryAsState().value?.destination?.route
            if (currentRoute in screensShowBottomBar) BottomNavigationBar(navController)
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding).padding(start = 16.dp, end = 16.dp)

        NavHost(navController = navController, startDestination = NavigationPaths.Login) {
            composable(NavigationPaths.Login) {
                val viewModel = LoginViewModel(database, navController)
                LoginScreen(modifier, navController, viewModel)
            }

            composable(NavigationPaths.Register) {
                val viewModel = RegisterViewModel(database, navController)
                RegisterScreen(modifier, navController, viewModel)
            }

            composable(NavigationPaths.ProductList) {
                FirebaseAutentication.logoutAccount()
                navController.popBackStack()
            }
        }
    }

}