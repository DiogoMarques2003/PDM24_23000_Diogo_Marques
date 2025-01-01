package com.example.firebasestore.data.repository

import com.example.firebasestore.data.dao.CategoryDao
import com.example.firebasestore.data.entity.Category
import kotlinx.coroutines.flow.Flow

class CategoryRepository(private val categoryDao: CategoryDao) {
    val allCategories: Flow<List<Category>> = categoryDao.getAll()

    suspend fun insertList(categories: List<Category>) {
        categoryDao.insertList(categories)
    }

    suspend fun deleteAll() {
        categoryDao.deleteAll()
    }
}