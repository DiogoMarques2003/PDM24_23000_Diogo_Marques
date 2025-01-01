package com.example.firebasestore.data.repository

import com.example.firebasestore.data.dao.ProductDao
import com.example.firebasestore.data.entity.Product
import kotlinx.coroutines.flow.Flow

class ProductRepository(private val productDao: ProductDao) {
    val allProducts: Flow<List<Product>> = productDao.getAll()

    suspend fun insertList(products: List<Product>) {
        productDao.insertList(products)
    }

    suspend fun deleteAll() {
        productDao.deleteAll()
    }
}