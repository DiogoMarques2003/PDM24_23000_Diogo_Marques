package com.example.firebasestore.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.firebasestore.data.entity.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Insert
    suspend fun insertList(categories: List<Category>)

    @Query("SELECT * FROM category")
    fun getAll(): Flow<List<Category>>

    @Query("DELETE FROM category")
    suspend fun deleteAll()
}