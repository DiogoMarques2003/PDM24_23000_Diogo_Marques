package com.example.firebasestore.ui.viewModels

import android.content.Context
import android.util.Patterns.EMAIL_ADDRESS
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebasestore.data.database.AppDatabase
import com.example.firebasestore.data.entity.Cart
import com.example.firebasestore.data.entity.CartProduct
import com.example.firebasestore.data.entity.CartUser
import com.example.firebasestore.data.entity.Product
import com.example.firebasestore.data.entity.ProductImage
import com.example.firebasestore.data.entity.User
import com.example.firebasestore.data.firebase.FirebaseAutentication
import com.example.firebasestore.data.firebase.FirebaseCollections
import com.example.firebasestore.data.firebase.FirebaseFirestore
import com.example.firebasestore.data.firebase.FirebaseStorage
import com.example.firebasestore.data.repository.CartProductRepository
import com.example.firebasestore.data.repository.CartRepository
import com.example.firebasestore.data.repository.CartUserRepository
import com.example.firebasestore.data.repository.ProductImageRepository
import com.example.firebasestore.data.repository.ProductRepository
import com.example.firebasestore.data.repository.UserRepository
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CartViewModel(database: AppDatabase) : ViewModel() {
    private val userAuthentication = FirebaseAutentication.getCurrentUser()

    private val productRepository = ProductRepository(database.productDao())
    private val productImageRepository = ProductImageRepository(database.productImageDao())
    private val userRepository = UserRepository(database.userDao())
    private val cartUserRepository = CartUserRepository(database.cartUserDao())
    private val cartRepository = CartRepository(database.cartDao())
    private val cartProductRepository = CartProductRepository(database.cartProductDao())

    private var productListener: ListenerRegistration? = null
    private var productImageListener: ListenerRegistration? = null
    private var userListener: ListenerRegistration? = null
    private var cartUserListener: ListenerRegistration? = null
    private var cartListener: ListenerRegistration? = null
    private var cartProductListener: ListenerRegistration? = null

    val products = productRepository.allProducts
    val productImages = productImageRepository.allProductsImages
    val users = userRepository.allUsers
    val cartsUser = cartUserRepository.getByUserId(userAuthentication!!.uid)
    val allCartsUsers = cartUserRepository.allCartUsers
    val carts = cartRepository.allCarts
    val cartProducts = cartProductRepository.allCartProducts

    var isLoading = MutableStateFlow(false)
    var cartIdManagerUsers = MutableStateFlow<String?>(null)

    fun getData(context: Context) {
        productImageListener = FirebaseFirestore.listenToData(
            FirebaseCollections.ProductImage,
            null,
            { updateProductImages(it) },
            {
                Toast.makeText(context, "Erro a obter as imagens dos produtos", Toast.LENGTH_LONG)
                    .show()
            }
        )

        productListener = FirebaseFirestore.listenToData(
            FirebaseCollections.Product,
            null,
            { updateProductsData(it) },
            { Toast.makeText(context, "Erro a obter os produtos", Toast.LENGTH_LONG).show() }
        )

        userListener = FirebaseFirestore.listenToData(
            FirebaseCollections.User,
            null,
            { updateUsersData(it) },
            { Toast.makeText(context, "Erro a obter os utilizadores", Toast.LENGTH_LONG).show() }
        )

        cartUserListener = FirebaseFirestore.listenToData(
            FirebaseCollections.CartUser,
            null,
            { updateCartUserData(it) },
            {
                Toast.makeText(
                    context,
                    "Erro a obter os carrinhos dos utilizadores",
                    Toast.LENGTH_LONG
                ).show()
            }
        )

        cartListener = FirebaseFirestore.listenToData(
            FirebaseCollections.Cart,
            null,
            { updateCarData(it) },
            { Toast.makeText(context, "Erro a obter os carrinhos", Toast.LENGTH_LONG).show() }
        )

        cartProductListener = FirebaseFirestore.listenToData(
            FirebaseCollections.CartProduct,
            null,
            { updateCartProducts(it) },
            {
                Toast.makeText(context, "Erro a obter os produtos do carrinho", Toast.LENGTH_LONG)
                    .show()
            }
        )
    }

    fun stopListeners() {
        productListener?.remove()
        productListener = null
        productImageListener?.remove()
        productImageListener = null
        userListener?.remove()
        userListener = null
        cartUserListener?.remove()
        cartUserListener = null
        cartListener?.remove()
        cartListener = null
        cartProductListener?.remove()
        cartProductListener = null
    }

    fun createCart(context: Context) {
        isLoading.value = true

        viewModelScope.launch {
            val cart = Cart(id = "", ownerId = userAuthentication!!.uid)
            val cartId = FirebaseFirestore.changeData(
                FirebaseCollections.Cart,
                cart.toFirebaseMap()
            )

            if (cartId == null) {
                isLoading.value = false
                Toast.makeText(
                    context,
                    "Erro a criar o seu carrinho",
                    Toast.LENGTH_LONG
                ).show()
                return@launch
            }

            val cartUser = CartUser(id = "", cartId = cartId, userId = userAuthentication.uid)
            val cartUserId = FirebaseFirestore.changeData(
                FirebaseCollections.CartUser,
                cartUser.toFirebaseMap()
            )

            if (cartUserId == null) {
                isLoading.value = false
                Toast.makeText(
                    context,
                    "Erro a associar o carrinho",
                    Toast.LENGTH_LONG
                ).show()
                return@launch
            }

            Toast.makeText(
                context,
                "Carrinho criado com sucesso",
                Toast.LENGTH_LONG
            ).show()

            isLoading.value = false
        }
    }

    fun deleteCart(context: Context, cartId: String) {
        isLoading.value = true

        viewModelScope.launch {
            // filtrar os produtos do carrinho para os apagar
            val cartProductsDel = cartProducts.first().filter { it.cartId == cartId }

            // Filtrar os acessos do carrinho para os apagar
            val cartUsersDel = allCartsUsers.first().filter { it.cartId == cartId }

            // Apagar os acessos ao carrinho
            for (cartUser in cartUsersDel) {
                FirebaseFirestore.deleteData(
                    FirebaseCollections.CartUser,
                    cartUser.id
                )
            }

            // validar se existe produtos para apagar
            if (cartProductsDel.isNotEmpty()) {
                for (cartProduct in cartProductsDel) {
                    FirebaseFirestore.deleteData(
                        FirebaseCollections.CartProduct,
                        cartProduct.id
                    )
                }
            }

            // Apagar o carrinho
            FirebaseFirestore.deleteData(
                FirebaseCollections.Cart,
                cartId
            )

            Toast.makeText(
                context,
                "Carrinho apagado com sucesso",
                Toast.LENGTH_LONG
            ).show()

            isLoading.value = false
        }
    }

    fun addUserToCart(context: Context, email: String) {
        if (email.isEmpty() || !EMAIL_ADDRESS.matcher(email).matches()) {
            return Toast.makeText(
                context,
                "Introduz um email valido",
                Toast.LENGTH_LONG
            ).show()
        }

        isLoading.value = true
        viewModelScope.launch {
            val user = users.first().firstOrNull { it.email == email }

            if (user == null) {
                isLoading.value = false
                Toast.makeText(
                    context,
                    "Nenhum utilizador encontrado com o email indicado",
                    Toast.LENGTH_LONG
                ).show()

                return@launch
            }

            if (user.id == FirebaseAutentication.getCurrentUser()!!.uid) {
                isLoading.value = false
                Toast.makeText(
                    context,
                    "Não te podes adicionar a ti propria ao carrinho",
                    Toast.LENGTH_LONG
                ).show()

                return@launch
            }

            val userInCart = allCartsUsers.first().firstOrNull { it.cartId == cartIdManagerUsers.value && it.userId == user.id }

            if (userInCart != null) {
                isLoading.value = false
                Toast.makeText(
                    context,
                    "Esse utilizador já tem acesso ao seu carrinho",
                    Toast.LENGTH_LONG
                ).show()

                return@launch
            }

            val cartUser = CartUser(id = "", cartId = cartIdManagerUsers.value!!, userId = user.id)
            val cartUserId = FirebaseFirestore.changeData(
                FirebaseCollections.CartUser,
                cartUser.toFirebaseMap(),
            )

            if (cartUserId == null) {
                isLoading.value = false
                Toast.makeText(
                    context,
                    "Ocorreu algum erro a dar acesso ao ${user.name} ao seu carrinho",
                    Toast.LENGTH_LONG
                ).show()

                return@launch
            }

            Toast.makeText(
                context,
                "${user.name} adicionado com sucesso",
                Toast.LENGTH_LONG
            ).show()

            isLoading.value = false
        }
    }

    fun removeUserFromCart(context: Context, id: String) {
        isLoading.value = true
        viewModelScope.launch {
            FirebaseFirestore.deleteData(
                FirebaseCollections.CartUser,
                id
            )

            Toast.makeText(
                context,
                "Utilizador removido com suucesso",
                Toast.LENGTH_LONG
            ).show()

            isLoading.value = false
        }
    }

    private fun updateProductImages(productImagesList: List<Map<String, Any>>?) {
        viewModelScope.launch {
            if (productImagesList == null) {
                return@launch productImageRepository.deleteAll()
            }

            val productImagesListClass =
                productImagesList.map {
                    val productImage = ProductImage.firebaseMapToClass(it)
                    if (productImage.image != null) {
                        productImage.image = FirebaseStorage.getImageUrl(productImage.image!!)
                    }
                    productImage
                }

            productImageRepository.deleteAll()

            productImageRepository.insertList(productImagesListClass)
        }
    }

    private fun updateProductsData(productsList: List<Map<String, Any>>?) {
        viewModelScope.launch {
            if (productsList == null) {
                return@launch productRepository.deleteAll()
            }

            val product = productsList.first()
            val productClass = Product.firebaseMapToClass(product)

            productRepository.deleteAll()

            productRepository.insert(productClass)
        }
    }

    private fun updateUsersData(usersList: List<Map<String, Any>>?) {
        viewModelScope.launch {
            if (usersList == null) {
                return@launch productRepository.deleteAll()
            }

            val usersClass = usersList.map { User.firebaseMapToClass(it) }

            userRepository.deleteAll()

            userRepository.insertList(usersClass)
        }
    }

    private fun updateCartUserData(cartUserList: List<Map<String, Any>>?) {
        viewModelScope.launch {
            if (cartUserList == null) {
                return@launch productRepository.deleteAll()
            }

            val cartUserClass = cartUserList.map { CartUser.firebaseMapToClass(it) }

            cartUserRepository.deleteAll()

            cartUserRepository.insertList(cartUserClass)
        }
    }

    private fun updateCarData(cartList: List<Map<String, Any>>?) {
        viewModelScope.launch {
            if (cartList == null) {
                return@launch productRepository.deleteAll()
            }

            val cartClass = cartList.map { Cart.firebaseMapToClass(it) }

            cartRepository.deleteAll()

            cartRepository.insertList(cartClass)
        }
    }

    private fun updateCartProducts(cartProductsList: List<Map<String, Any>>?) {
        viewModelScope.launch {
            if (cartProductsList == null) {
                return@launch cartProductRepository.deleteAll()
            }

            val cartProductsClass = cartProductsList.map { CartProduct.firebaseMapToClass(it) }

            cartProductRepository.deleteAll()

            cartProductRepository.insertList(cartProductsClass)
        }
    }
}