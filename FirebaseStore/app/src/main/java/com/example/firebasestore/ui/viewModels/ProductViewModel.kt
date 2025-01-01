package com.example.firebasestore.ui.viewModels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebasestore.data.database.AppDatabase
import com.example.firebasestore.data.entity.Product
import com.example.firebasestore.data.entity.ProductImage
import com.example.firebasestore.data.firebase.FirebaseCollections
import com.example.firebasestore.data.firebase.FirebaseFirestore
import com.example.firebasestore.data.firebase.FirebaseStorage
import com.example.firebasestore.data.repository.ProductImageRepository
import com.example.firebasestore.data.repository.ProductRepository
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.launch

class ProductViewModel(database: AppDatabase, private val productId: String) : ViewModel() {
    private val productRepository = ProductRepository(database.productDao())
    private val productImageRepository = ProductImageRepository(database.productImageDao())

    private var productListener: ListenerRegistration? = null
    private var productImageListener: ListenerRegistration? = null

    val product = productRepository.getById(productId)
    val productImages = productImageRepository.getByProductId(productId)

    fun getData(context: Context) {
        productImageListener = FirebaseFirestore.listenToData(
            FirebaseCollections.ProductImage,
            null,
            { updateProductImages(it) },
            { Toast.makeText(context, "Erro a obter as imagens dos produtos", Toast.LENGTH_LONG).show() }
        )

        productListener = FirebaseFirestore.listenToData(
            FirebaseCollections.Product,
            productId,
            { updateProductsData(it) },
            { Toast.makeText(context, "Erro a obter os produtos", Toast.LENGTH_LONG).show() }
        )
    }

    fun stopListeners() {
        productListener?.remove()
        productListener = null
        productImageListener?.remove()
        productImageListener = null
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
}