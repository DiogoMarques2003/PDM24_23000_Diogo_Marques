package com.example.firebasestore.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.firebasestore.data.entity.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Insert
    suspend fun insertList(products: List<Product>)

    @Query("SELECT * FROM product")
    fun getAll(): Flow<List<Product>>

    @Query("DELETE FROM product")
    suspend fun deleteAll()
}