package com.example.firebasestore.data.repository

import com.example.firebasestore.data.dao.UserDao
import com.example.firebasestore.data.entity.User
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {
    val allUsers: Flow<List<User>> = userDao.getAll()

    suspend fun insert(user: User) {
        userDao.insert(user)
    }

    suspend fun insertList(users: List<User>) {
        deleteAll()

        userDao.insertList(users)
    }

    fun getById(id: String): Flow<User> {
        return userDao.getById(id)
    }

    suspend fun deleteAll() {
        userDao.deleteAll()
    }
}