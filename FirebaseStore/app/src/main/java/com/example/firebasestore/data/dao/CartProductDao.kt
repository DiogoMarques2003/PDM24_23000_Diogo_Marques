package com.example.firebasestore.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.firebasestore.data.entity.CartProduct
import kotlinx.coroutines.flow.Flow

@Dao
interface CartProductDao {
    @Insert
    suspend fun insertList(cartProducts: List<CartProduct>)

    @Query("SELECT * FROM cartProduct")
    fun getAll(): Flow<List<CartProduct>>

    @Query("DELETE FROM cartProduct")
    suspend fun deleteAll()
}