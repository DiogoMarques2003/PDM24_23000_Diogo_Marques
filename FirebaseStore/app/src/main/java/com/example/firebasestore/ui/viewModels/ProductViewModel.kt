package com.example.firebasestore.ui.viewModels

import android.content.Context
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
import com.example.firebasestore.data.repository.CartRepository
import com.example.firebasestore.data.repository.CartUserRepository
import com.example.firebasestore.data.repository.ProductImageRepository
import com.example.firebasestore.data.repository.ProductRepository
import com.example.firebasestore.data.repository.UserRepository
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProductViewModel(database: AppDatabase, private val productId: String) : ViewModel() {
    private val userAuthentication = FirebaseAutentication.getCurrentUser()

    private val productRepository = ProductRepository(database.productDao())
    private val productImageRepository = ProductImageRepository(database.productImageDao())
    private val userRepository = UserRepository(database.userDao())
    private val cartUserRepository = CartUserRepository(database.cartUserDao())
    private val cartRepository = CartRepository(database.cartDao())

    private var productListener: ListenerRegistration? = null
    private var productImageListener: ListenerRegistration? = null
    private var userListener: ListenerRegistration? = null
    private var cartUserListener: ListenerRegistration? = null
    private var cartListener: ListenerRegistration? = null

    val product = productRepository.getById(productId)
    val productImages = productImageRepository.getByProductId(productId)
    val allUsers = userRepository.allUsers
    val cartsUser = cartUserRepository.getByUserId(userAuthentication!!.uid)
    val allCarts = cartRepository.allCarts

    var showPopup = MutableStateFlow(false)
    var isLoading = MutableStateFlow(false)

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
            productId,
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
    }

    fun checkIfUserHaveCarts(context: Context) {
        isLoading.value = true
        viewModelScope.launch {
            val carts = cartsUser.first() // Observa apenas o primeiro valor emitido
            if (carts.isNotEmpty()) {
                isLoading.value = false
                showPopup.value = true
                return@launch
            }

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

            isLoading.value = false
            showPopup.value = true
        }
    }

    fun addItemToCart(cartId: String, quantity: Long, context: Context) {
        if (cartId.isEmpty()) {
            return Toast.makeText(
                context,
                "Tens de escolher um carrinho",
                Toast.LENGTH_LONG
            ).show()
        }

        if (quantity <= 0) {
            return Toast.makeText(
                context,
                "Tens de introduzir uma quantidade",
                Toast.LENGTH_LONG
            ).show()
        }

        viewModelScope.launch {
            val cartProducts = FirebaseFirestore.getData(FirebaseCollections.CartProduct)

            val cartProductClass = cartProducts?.map { CartProduct.firebaseMapToClass(it) }
            var cartProductComb =
                cartProductClass?.firstOrNull { it.productId == productId && it.cartId == cartId }

            if (cartProductComb != null) {
                cartProductComb.quantity += quantity
            } else {
                cartProductComb = CartProduct(
                    id = "",
                    cartId = cartId,
                    productId = productId,
                    quantity = quantity
                )
            }

            val carProductId = FirebaseFirestore.changeData(
                FirebaseCollections.CartProduct,
                cartProductComb.toFirebaseMap(),
                cartProductComb.id.ifEmpty { null })

            if (carProductId == null) {
                Toast.makeText(
                    context,
                    "Erro a adicionar produto ao carrinho",
                    Toast.LENGTH_LONG
                ).show()
                return@launch
            }

            Toast.makeText(
                context,
                "Produto adicionado com sucesso",
                Toast.LENGTH_LONG
            ).show()

            showPopup.value = false
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
}