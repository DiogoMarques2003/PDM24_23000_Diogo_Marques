package com.example.firebasestore.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.firebasestore.ui.screens.LoginScreen

@Composable
fun Navigation(modifier: Modifier) {
    val navController = rememberNavController()

    val defaultModifier = modifier.padding(start = 16.dp, end = 16.dp)

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(defaultModifier)
        }
    }
}