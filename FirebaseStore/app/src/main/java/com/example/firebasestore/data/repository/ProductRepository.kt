package com.example.firebasestore.data.repository

import com.example.firebasestore.data.dao.ProductDao
import com.example.firebasestore.data.entity.Product
import kotlinx.coroutines.flow.Flow

class ProductRepository(private val productDao: ProductDao) {
    val allProducts: Flow<List<Product>> = productDao.getAll()

    suspend fun insert(product: Product) {
        productDao.insert(product)
    }

    suspend fun insertList(products: List<Product>) {
        productDao.insertList(products)
    }

    fun getById(id: String): Flow<Product> {
        return productDao.getById(id)
    }

    suspend fun deleteAll() {
        productDao.deleteAll()
    }
}