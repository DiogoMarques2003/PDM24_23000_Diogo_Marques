package com.example.firebasestore.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.firebasestore.data.entity.CartUser
import kotlinx.coroutines.flow.Flow

@Dao
interface CartUserDao {
    @Insert
    suspend fun insertList(cartUsers: List<CartUser>)

    @Query("SELECT * FROM cartUser")
    fun getAll(): Flow<List<CartUser>>

    @Query("DELETE FROM cartUser")
    suspend fun deleteAll()
}