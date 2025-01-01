package com.example.firebasestore.data.repository

import com.example.firebasestore.data.dao.ProductImageDao
import com.example.firebasestore.data.entity.ProductImage
import kotlinx.coroutines.flow.Flow

class ProductImageRepository(private val productImageDao: ProductImageDao) {
    val allProductsImages: Flow<List<ProductImage>> = productImageDao.getAll()

    suspend fun insertList(productImages: List<ProductImage>) {
        productImageDao.insertList(productImages)
    }

    fun getByProductId(productId: String): Flow<List<ProductImage>> {
        return  productImageDao.getByProductId(productId)
    }

    suspend fun deleteAll() {
        productImageDao.deleteAll()
    }
}