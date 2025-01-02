package com.example.firebasestore.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentReference

@Entity(tableName = "product")
data class Product(
    @PrimaryKey(autoGenerate = false) var id: String,
    val categoryID: String,
    val name: String,
    val description: String,
    val price: Long
) {
    companion object {
        fun firebaseMapToClass(data: Map<String, Any?>): Product {
            val categoryReference = data["categoryID"] as DocumentReference

            return Product(
                id = data["id"] as String,
                categoryID = categoryReference.id,
                name = data["name"] as String,
                description = data["description"] as String,
                price = data["price"] as Long
            )
        }
    }
}
