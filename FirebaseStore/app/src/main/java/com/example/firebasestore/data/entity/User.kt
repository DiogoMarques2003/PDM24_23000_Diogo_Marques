package com.example.firebasestore.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = false) val id: String,
    val email: String,
    val name: String
) {
    fun toFirebaseMap(): Map<String, Any?> {
        return mapOf(
            "email" to email,
            "name" to name
        )
    }

    companion object {
        fun firebaseMapToClass(data: Map<String, Any?>): User {
            return User(
                id = data["id"] as String,
                email = data["email"] as String,
                name = data["name"] as String
            )
        }
    }
}
