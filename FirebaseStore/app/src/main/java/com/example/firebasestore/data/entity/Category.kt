package com.example.firebasestore.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class Category(
    @PrimaryKey(autoGenerate = false) val id: String,
    val nome: String
) {
    companion object {
        fun firebaseMapToClass(data: Map<String, Any?>): Category {
            return Category(
                id = data["id"] as String,
                nome = data["nome"] as String
            )
        }
    }
}