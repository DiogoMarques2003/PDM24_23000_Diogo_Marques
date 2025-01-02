package com.example.firebasestore.data.repository

import com.example.firebasestore.data.dao.CartUserDao
import com.example.firebasestore.data.entity.CartUser
import kotlinx.coroutines.flow.Flow

class CartUserRepository(private val cartUserDao: CartUserDao) {
    val allCartUsers: Flow<List<CartUser>> = cartUserDao.getAll()

    suspend fun insertList(cartUsers: List<CartUser>) {
        cartUserDao.insertList(cartUsers)
    }

    fun getByUserId(userId: String): Flow<List<CartUser>> {
        return cartUserDao.getByUserId(userId)
    }

    suspend fun deleteAll() {
        cartUserDao.deleteAll()
    }
}