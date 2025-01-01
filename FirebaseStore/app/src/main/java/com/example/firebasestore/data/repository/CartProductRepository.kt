package com.example.firebasestore.data.repository

import com.example.firebasestore.data.dao.CartProductDao
import com.example.firebasestore.data.entity.CartProduct
import kotlinx.coroutines.flow.Flow

class CartProductRepository(private val cartProductDao: CartProductDao) {
    val allCartProducts: Flow<List<CartProduct>> = cartProductDao.getAll()

    suspend fun insertList(cartProducts: List<CartProduct>) {
        cartProductDao.insertList(cartProducts)
    }

    suspend fun deleteAll() {
        cartProductDao.deleteAll()
    }
}