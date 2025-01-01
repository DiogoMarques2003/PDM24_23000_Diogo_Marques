package com.example.firebasestore.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.firebasestore.data.entity.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Insert
    suspend fun insertList(users: List<User>)

    @Query("SELECT * FROM user")
    fun getAll(): Flow<List<User>>

    @Query("SELECT * FROM user WHERE id = :id")
    fun getById(id: String): Flow<User>

    @Query("DELETE FROM user")
    suspend fun deleteAll()
}