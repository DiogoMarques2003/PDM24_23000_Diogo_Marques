package com.example.firebasestore.data.repository

import com.example.firebasestore.data.dao.CartDao
import com.example.firebasestore.data.entity.Cart
import kotlinx.coroutines.flow.Flow

class CartRepository(private val cartDao: CartDao) {
    val allCarts: Flow<List<Cart>> = cartDao.getAll()

    suspend fun insertList(carts: List<Cart>) {
        cartDao.insertList(carts)
    }

    suspend fun deleteAll() {
        cartDao.deleteAll()
    }
}