package com.example.firebasestore.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.firebasestore.data.entity.ProductImage
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductImageDao {
    @Insert
    suspend fun insertList(productImages: List<ProductImage>)

    @Query("SELECT * FROM productImage")
    fun getAll(): Flow<List<ProductImage>>

    @Query("DELETE FROM productImage")
    suspend fun deleteAll()
}