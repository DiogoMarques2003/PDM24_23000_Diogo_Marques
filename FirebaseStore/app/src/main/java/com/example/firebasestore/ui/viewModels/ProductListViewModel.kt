package com.example.firebasestore.ui.viewModels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebasestore.data.database.AppDatabase
import com.example.firebasestore.data.entity.Category
import com.example.firebasestore.data.entity.Product
import com.example.firebasestore.data.entity.ProductImage
import com.example.firebasestore.data.firebase.FirebaseCollections
import com.example.firebasestore.data.firebase.FirebaseFirestore
import com.example.firebasestore.data.firebase.FirebaseStorage
import com.example.firebasestore.data.repository.CategoryRepository
import com.example.firebasestore.data.repository.ProductImageRepository
import com.example.firebasestore.data.repository.ProductRepository
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.launch

class ProductListViewModel(database: AppDatabase) :
    ViewModel() {
    private val productRepository = ProductRepository(database.productDao())
    private val categoryRepository = CategoryRepository(database.categoryDao())
    private val productImageRepository = ProductImageRepository(database.productImageDao())

    private var productListener: ListenerRegistration? = null
    private var categoryListener: ListenerRegistration? = null
    private var productImageListener: ListenerRegistration? = null

    val allProducts = productRepository.allProducts
    val allCategories = categoryRepository.allCategories
    val allProductImages = productImageRepository.allProductsImages

    fun getData(context: Context) {
        productImageListener = FirebaseFirestore.listenToData(
            FirebaseCollections.ProductImage,
            null,
            { updateProductImages(it) },
            { Toast.makeText(context, "Erro a obter as imagens dos produtos", Toast.LENGTH_LONG).show() }
        )

        categoryListener = FirebaseFirestore.listenToData(
            FirebaseCollections.Category,
            null,
            { updateCategoriesData(it) },
            { Toast.makeText(context, "Erro a obter as categorias", Toast.LENGTH_LONG).show() }
        )

        productListener = FirebaseFirestore.listenToData(
            FirebaseCollections.Product,
            null,
            { updateProductsData(it) },
            { Toast.makeText(context, "Erro a obter os produtos", Toast.LENGTH_LONG).show() }
        )
    }

    fun stopListeners() {
        productListener?.remove()
        productListener = null
        categoryListener?.remove()
        categoryListener = null
        productImageListener?.remove()
        productImageListener = null
    }

    private fun updateProductsData(productsList: List<Map<String, Any>>?) {
        viewModelScope.launch {
            if (productsList == null) {
                return@launch productRepository.deleteAll()
            }

            val productsListClass = productsList.map { Product.firebaseMapToClass(it) }

            productRepository.deleteAll()

            productRepository.insertList(productsListClass)
        }
    }

    private fun updateCategoriesData(categoriesList: List<Map<String, Any>>?) {
        viewModelScope.launch {
            if (categoriesList == null) {
                return@launch categoryRepository.deleteAll()
            }

            val categoriesListClass = categoriesList.map { Category.firebaseMapToClass(it) }

            categoryRepository.deleteAll()

            categoryRepository.insertList(categoriesListClass)
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
}