package com.example.firebasestore.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebasestore.data.database.AppDatabase
import com.example.firebasestore.data.firebase.FirebaseAutentication
import com.example.firebasestore.data.repository.CartProductRepository
import com.example.firebasestore.data.repository.CartRepository
import com.example.firebasestore.data.repository.CartUserRepository
import com.example.firebasestore.data.repository.CategoryRepository
import com.example.firebasestore.data.repository.ProductImageRepository
import com.example.firebasestore.data.repository.ProductRepository
import com.example.firebasestore.data.repository.UserRepository
import kotlinx.coroutines.launch

class NavigationBarViewModel(database: AppDatabase): ViewModel() {
    private val cartProductRepository = CartProductRepository(database.cartProductDao())
    private val cartRepository = CartRepository(database.cartDao())
    private val cartUserRepository = CartUserRepository(database.cartUserDao())
    private val categoryRepository = CategoryRepository(database.categoryDao())
    private val productRepository = ProductRepository(database.productDao())
    private val productImageRepository = ProductImageRepository(database.productImageDao())
    private val userRepository = UserRepository(database.userDao())

    fun logoutAndClearCache() {
        FirebaseAutentication.logoutAccount()

        viewModelScope.launch {
            cartProductRepository.deleteAll()
            cartRepository.deleteAll()
            cartUserRepository.deleteAll()
            categoryRepository.deleteAll()
            productRepository.deleteAll()
            productImageRepository.deleteAll()
            userRepository.deleteAll()
        }
    }

}