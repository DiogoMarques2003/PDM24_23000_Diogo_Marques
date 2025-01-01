package com.example.firebasestore.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentReference

@Entity(tableName = "cartProduct")
data class CartProduct(
    @PrimaryKey(autoGenerate = false) val id: String,
    val cartId: String,
    val productId: String,
    val quantity: Long
) {
    fun toFirebaseMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "cartId" to cartId,
            "productId" to productId,
            "quantity" to quantity
        )
    }

    companion object {
        fun firebaseMapToClass(data: Map<String, Any?>): CartProduct {
            val productReference = data["productId"] as DocumentReference
            val cartReference = data["cartId"] as DocumentReference

            return CartProduct(
                id = data["id"] as String,
                cartId = cartReference.id,
                productId = productReference.id,
                quantity = data["quantity"] as Long
            )
        }
    }
}
