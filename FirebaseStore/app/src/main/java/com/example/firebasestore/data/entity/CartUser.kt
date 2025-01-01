package com.example.firebasestore.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentReference

@Entity(tableName = "cartUser")
data class CartUser(
    @PrimaryKey(autoGenerate = false) val id: String,
    val cartId: String,
    val userId: String,
) {
    fun toFirebaseMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "cartId" to cartId,
            "userId" to userId
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
