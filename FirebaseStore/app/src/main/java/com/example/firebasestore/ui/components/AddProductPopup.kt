package com.example.firebasestore.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.firebasestore.ui.viewModels.ProductViewModel

@Composable
fun AddProductPopup(viewModel: ProductViewModel) {
    val product by viewModel.product.collectAsState(null)
    val showPopup = viewModel.showPopup
    val cartsUser by viewModel.cartsUser.collectAsState(emptyList())
    val allCarts by viewModel.allCarts.collectAsState(emptyList())
    val allUsers by viewModel.allUsers.collectAsState(emptyList())
    val context = LocalContext.current

    var selectedCart by remember { mutableStateOf("") }
    var quantity by remember { mutableLongStateOf(0) }

    Dialog(onDismissRequest = { showPopup.value = false }) {
        ElevatedCard(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Adicionar ${product?.name ?: "N/A"} ao carrinho",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                CartDropdown(
                    cartsUser,
                    allCarts,
                    allUsers,
                    onCartSelected = { selectedCart = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Input para quantidade
                OutlinedTextField(
                    value = quantity.toString(),
                    onValueChange = { quantity = it.toLongOrNull() ?: 0 },
                    maxLines = 1,
                    label = { Text("Quantidade") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botões de ação
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { showPopup.value = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        ),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Cancelar")
                    }
                    Button(
                        onClick = {
                            viewModel.addItemToCart(selectedCart, quantity, context)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text("Adicionar")
                    }
                }
            }
        }
    }
}