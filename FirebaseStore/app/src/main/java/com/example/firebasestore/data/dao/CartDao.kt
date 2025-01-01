package com.example.firebasestore.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.firebasestore.data.entity.Cart
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Insert
    suspend fun insertList(carts: List<Cart>)

    @Query("SELECT * FROM cart")
    fun getAll(): Flow<List<Cart>>

    @Query("DELETE FROM cart")
    suspend fun deleteAll()
}