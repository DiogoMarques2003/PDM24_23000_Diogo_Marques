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
    fun toFirebaseMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "categoryID" to categoryID, // TODO: Change for reference
            "name" to name,
            "description" to description,
            "price" to price
        )
    }

    companion object {
        fun firebaseMapToClass(data: Map<String, Any?>): Product {
            val categoryReference = data["categoryId"] as DocumentReference

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
