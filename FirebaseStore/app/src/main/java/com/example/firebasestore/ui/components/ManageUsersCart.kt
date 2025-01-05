package com.example.firebasestore.ui.components

import android.util.Patterns.EMAIL_ADDRESS
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.firebasestore.data.entity.CartUser
import com.example.firebasestore.data.firebase.FirebaseAutentication
import com.example.firebasestore.ui.viewModels.CartViewModel

@Composable
fun ManageUsersCart(viewModel: CartViewModel) {
    val allCartsUsers by viewModel.allCartsUsers.collectAsState(emptyList())
    val users by viewModel.users.collectAsState(emptyList())
    val cartId by viewModel.cartIdManagerUsers.collectAsState()

    var newUserEmail by remember { mutableStateOf("") }
    var usersWithAccess by remember { mutableStateOf<List<CartUser>>(emptyList()) }
    val context = LocalContext.current

    LaunchedEffect(cartId, allCartsUsers) {
        usersWithAccess =
            allCartsUsers.filter { it.cartId == cartId && it.userId != FirebaseAutentication.getCurrentUser()?.uid }
    }

    Dialog(onDismissRequest = { viewModel.cartIdManagerUsers.value = null }) {
        ElevatedCard(
            shape = MaterialTheme.shapes.medium,
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Acessos ao seu carrinho", style = MaterialTheme.typography.titleLarge)

                if (usersWithAccess.isEmpty()) {
                    Text("Carrinho não partilhado com ninguém")
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 300.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(usersWithAccess) { user ->
                            val username = users.firstOrNull { it.id == user.userId }?.name ?: "N/A"
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(username, style = MaterialTheme.typography.bodyLarge)
                                IconButton(onClick = { viewModel.removeUserFromCart(context, user.id) }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Remover acesso"
                                    )
                                }
                            }
                        }
                    }
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Adicionar utilizadores:", style = MaterialTheme.typography.titleMedium)

                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = newUserEmail,
                            onValueChange = { newUserEmail = it },
                            label = { Text("Email do utilizador") },
                            modifier = Modifier.weight(1f),
                            maxLines = 1,
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            isError = if (newUserEmail.isEmpty()) false else !EMAIL_ADDRESS.matcher(
                                newUserEmail
                            ).matches()
                        )
                        Button(
                            onClick = {
                                viewModel.addUserToCart(context, newUserEmail)
                                newUserEmail = ""
                            },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Adicionar acesso",
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = { viewModel.cartIdManagerUsers.value = null }) {
                    Text("Fechar")
                }
            }
        }
    }
}