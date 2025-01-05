package com.example.firebasestore.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.firebasestore.ui.viewModels.CartViewModel

@Composable
fun PaymentPopup(viewModel: CartViewModel, totalCartValue: Long) {
    val context = LocalContext.current

    Dialog(onDismissRequest = { viewModel.cartIdToBuy.value = null }) {
        ElevatedCard(
            shape = MaterialTheme.shapes.medium,
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Opções de Pagamento",
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = "Total: $totalCartValue€",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { viewModel.buyCart(context, "Paypal")  },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Pagar com PayPal")
                }

                Button(
                    onClick = { viewModel.buyCart(context, "Cartão") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Pagar com Cartão")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { viewModel.cartIdToBuy.value = null },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
                ) {
                    Text("Cancelar")
                }
            }
        }
    }
}