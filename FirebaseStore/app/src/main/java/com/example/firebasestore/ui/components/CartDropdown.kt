package com.example.firebasestore.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.firebasestore.data.entity.Cart
import com.example.firebasestore.data.entity.CartUser
import com.example.firebasestore.data.entity.User
import com.example.firebasestore.data.firebase.FirebaseAutentication

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartDropdown(
    cartsUser: List<CartUser>,
    allCarts: List<Cart>,
    allUsers: List<User>,
    cartSelected: MutableState<String>
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedCartText by remember { mutableStateOf("") }

    LaunchedEffect(cartSelected.value) {
        if (cartSelected.value.isEmpty()) {
            selectedCartText = ""
        }
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = selectedCartText,
            onValueChange = {},
            readOnly = true,
            label = { Text("Selecione o Carrinho") },
            trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            cartsUser.forEach { cart ->
                val cartInfo = allCarts.firstOrNull { it.id == cart.cartId }
                val displayCartText = when (val ownerId = cartInfo?.ownerId) {
                    null -> ""
                    FirebaseAutentication.getCurrentUser()?.uid -> "O seu carrinho"
                    else -> {
                        val userName = allUsers.firstOrNull { it.id == ownerId }?.name ?: ""
                        "Carrinho do $userName"
                    }
                }
                DropdownMenuItem(
                    text = { Text(displayCartText) },
                    onClick = {
                        cartSelected.value = cart.cartId
                        selectedCartText = displayCartText
                        expanded = false
                    }
                )
            }
        }
    }
}