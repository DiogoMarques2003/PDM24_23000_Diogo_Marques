package com.example.firebasestore.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.firebasestore.data.database.AppDatabase
import com.example.firebasestore.data.repository.CategoryRepository
import com.example.firebasestore.data.repository.ProductImageRepository
import com.example.firebasestore.data.repository.ProductRepository

class ProductListViewModel(database: AppDatabase, private val navController: NavController) :
    ViewModel() {
    private val productRepository = ProductRepository(database.productDao())
    private val categoryRepository = CategoryRepository(database.categoryDao())
    private val productImageRepository = ProductImageRepository(database.productImageDao())

    val allProducts = productRepository.allProducts
    val allCategories = categoryRepository.allCategories
    val allProductImages = productImageRepository.allProductsImages
}