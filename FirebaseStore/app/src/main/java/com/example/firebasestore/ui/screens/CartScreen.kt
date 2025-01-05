package com.example.firebasestore.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.SubcomposeAsyncImage
import com.example.firebasestore.data.entity.CartProduct
import com.example.firebasestore.data.firebase.FirebaseAutentication
import com.example.firebasestore.ui.components.CartButtons
import com.example.firebasestore.ui.components.CartDropdown
import com.example.firebasestore.ui.components.LoadIndicator
import com.example.firebasestore.ui.components.ManageUsersCart
import com.example.firebasestore.ui.components.PaymentPopup
import com.example.firebasestore.ui.viewModels.CartViewModel

@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: CartViewModel
) {
    val context = LocalContext.current

    val products by viewModel.products.collectAsState(emptyList())
    val productImages by viewModel.productImages.collectAsState(emptyList())
    val users by viewModel.users.collectAsState(emptyList())
    val cartsUser by viewModel.cartsUser.collectAsState(emptyList())
    val carts by viewModel.carts.collectAsState(emptyList())
    val cartProducts by viewModel.cartProducts.collectAsState(emptyList())
    val isLoading by viewModel.isLoading.collectAsState(false)
    val cartIdManagerUsers = viewModel.cartIdManagerUsers.collectAsState(null)
    val cartIdToBuy = viewModel.cartIdToBuy.collectAsState(null)

    val cartSelected = remember { mutableStateOf("") }
    var isCartOwner by remember { mutableStateOf(false) }
    var hasAnyCart by remember { mutableStateOf(false) }
    var displayProducts by remember { mutableStateOf<List<CartProduct>>(emptyList()) }
    var totalCart by remember { mutableLongStateOf(0) }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.stopListeners()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getData(context)
    }

    LaunchedEffect(carts) {
        // Validar se o user tem algum carrinho
        val userHasCart =
            carts.firstOrNull { it.ownerId == FirebaseAutentication.getCurrentUser()!!.uid }
        hasAnyCart = userHasCart != null

        if (cartSelected.value.isEmpty()) {
            return@LaunchedEffect
        }

        // Validar se o carrinho selecionado existe
        val cartExist = carts.firstOrNull { it.id == cartSelected.value }
        cartSelected.value = if (cartExist == null) "" else cartSelected.value
    }

    LaunchedEffect(cartsUser) {
        if (cartSelected.value.isEmpty()) {
            return@LaunchedEffect
        }

        // Validar se o user ainda tem acesso ao carrinho
        val cartUserExists = cartsUser.firstOrNull { it.cartId == cartSelected.value }
        cartSelected.value = if (cartUserExists == null) "" else cartSelected.value
    }

    LaunchedEffect(cartSelected.value, cartProducts) {
        if (cartSelected.value.isEmpty()) {
            return@LaunchedEffect
        }

        val cart = carts.firstOrNull { it.id == cartSelected.value }

        isCartOwner =
            !(cart == null || cart.ownerId != FirebaseAutentication.getCurrentUser()!!.uid)

        displayProducts = cartProducts.filter { it.cartId == cartSelected.value }

        totalCart = 0
        for (displayProduct in displayProducts) {
            val product = products.firstOrNull { it.id == displayProduct.productId }
            val price = product?.price ?: 0L

            totalCart += (displayProduct.quantity * price)
        }
    }

    if (cartIdManagerUsers.value != null) {
        ManageUsersCart(viewModel)
    }

    if (cartIdToBuy.value != null) {
        PaymentPopup(viewModel, totalCart)
    }

    if (isLoading) {
        LoadIndicator(modifier)
    }

    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                "Carrinho",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (cartsUser.isEmpty()) {
                Button(
                    onClick = { viewModel.createCart(context) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Criar carrinho")
                }
            } else {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    CartDropdown(
                        cartsUser,
                        carts,
                        users,
                        cartSelected
                    )
                }

                CartButtons(
                    hasAnyCart,
                    isCartOwner,
                    cartSelected.value,
                    viewModel
                )
            }
        }

        if (cartSelected.value.isNotEmpty() && displayProducts.isEmpty()) {
            Text("Não existem produtos no carrinho")
        } else if (cartSelected.value.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(displayProducts) { productCart ->
                    val productImage =
                        productImages.firstOrNull { it.productId == productCart.productId }
                    val product = products.firstOrNull { it.id == productCart.productId }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (productImage != null) {
                            SubcomposeAsyncImage(
                                model = productImage.image,
                                contentDescription = "Imagem produto",
                                loading = { CircularProgressIndicator() },
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(product?.name ?: "N/A", style = MaterialTheme.typography.bodyLarge)
                            Text("Quantidade: ${productCart.quantity}")
                            Text("Preço do produto: ${product?.price ?: "N/A"}€")
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = { viewModel.removeProduct(productCart.id) },
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Remover produto"
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Preço total: ${totalCart}€",
                    style = MaterialTheme.typography.bodyLarge
                )

                Button(
                    onClick = { viewModel.cartIdToBuy.value = cartSelected.value },
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart, // Substitua por outro ícone, se necessário
                        contentDescription = "Comprar carrinho"
                    )
                }
            }
        }

    }
}