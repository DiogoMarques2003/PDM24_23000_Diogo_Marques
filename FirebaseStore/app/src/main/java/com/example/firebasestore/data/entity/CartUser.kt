package com.example.firebasestore.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.firebasestore.data.firebase.FirebaseCollections
import com.example.firebasestore.data.firebase.FirebaseFirestore
import com.google.firebase.firestore.DocumentReference

@Entity(tableName = "cartUser")
data class CartUser(
    @PrimaryKey(autoGenerate = false) val id: String,
    val cartId: String,
    val userId: String,
) {
    fun toFirebaseMap(): Map<String, Any?> {
        return mapOf(
            "cartId" to FirebaseFirestore.getReferenceById(FirebaseCollections.Cart, cartId),
            "userId" to FirebaseFirestore.getReferenceById(FirebaseCollections.User, userId)
        )
    }

    companion object {
        fun firebaseMapToClass(data: Map<String, Any?>): CartUser {
            val userReference = data["userId"] as DocumentReference
            val cartReference = data["cartId"] as DocumentReference

            return CartUser(
                id = data["id"] as String,
                cartId = cartReference.id,
                userId = userReference.id
            )
        }
    }
}
