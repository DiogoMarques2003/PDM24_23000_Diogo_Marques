package com.example.firebasestore.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentReference

@Entity(tableName = "cart")
data class Cart(
    @PrimaryKey(autoGenerate = false) val id: String,
    val ownerId: String,
) {
    fun toFirebaseMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "ownerId" to ownerId
        )
    }

    companion object {
        fun firebaseMapToClass(data: Map<String, Any?>): Cart {
            val ownerReference = data["ownerId"] as DocumentReference

            return Cart(
                id = data["id"] as String,
                ownerId = ownerReference.id
            )
        }
    }
}
