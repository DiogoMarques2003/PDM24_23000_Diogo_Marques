package com.example.firebasestore.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.firebasestore.ui.viewModels.CartViewModel

@Composable
fun CartButtons(hasAnyCart: Boolean, isCartOwner: Boolean, cartSelected: String, viewModel: CartViewModel) {
    val context = LocalContext.current

    if (!hasAnyCart) {
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = { viewModel.createCart(context) },
            modifier = Modifier.padding(end = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "Criar Carrinho",
                modifier = Modifier.size(20.dp)
            )
        }
    }

    if (isCartOwner && cartSelected.isNotEmpty()) {
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = { viewModel.cartIdManagerUsers.value = cartSelected },
            modifier = Modifier.padding(end = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ManageAccounts,
                contentDescription = "Gerir Acesso",
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Button(
            onClick = { viewModel.deleteCart(context, cartSelected) },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Apagar",
                modifier = Modifier.size(20.dp)
            )
        }
    }

    if (!isCartOwner && cartSelected.isNotEmpty()) {
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = { },
            modifier = Modifier.padding(end = 8.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                contentDescription = "Sair do Carrinho",
                modifier = Modifier.size(20.dp)
            )
        }
    }
}