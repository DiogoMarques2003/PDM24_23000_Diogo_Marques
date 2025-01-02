package com.example.firebasestore.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.firebasestore.data.firebase.FirebaseCollections
import com.example.firebasestore.data.firebase.FirebaseFirestore
import com.google.firebase.firestore.DocumentReference

@Entity(tableName = "cartProduct")
data class CartProduct(
    @PrimaryKey(autoGenerate = false) val id: String,
    val cartId: String,
    val productId: String,
    var quantity: Long
) {
    fun toFirebaseMap(): Map<String, Any?> {
        return mapOf(
            "cartId" to FirebaseFirestore.getReferenceById(FirebaseCollections.Cart, cartId),
            "productId" to FirebaseFirestore.getReferenceById(FirebaseCollections.Product, productId),
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
