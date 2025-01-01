package com.example.firebasestore.ui.components

import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun PopBackButton(navController : NavController){
    FloatingActionButton(
        onClick = { navController.popBackStack() },
        modifier = Modifier.width(30.dp),
        containerColor = MaterialTheme.colorScheme.background,
        elevation = FloatingActionButtonDefaults.loweredElevation(0.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.ArrowBackIosNew,
            contentDescription = "voltar atrás",
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}