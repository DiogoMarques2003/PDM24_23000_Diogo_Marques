package com.example.firebasestore.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentReference

@Entity(tableName = "productImage")
data class ProductImage(
    @PrimaryKey(autoGenerate = false) val id: String,
    val productId: String,
    var image: String?
) {
    companion object {
        fun firebaseMapToClass(data: Map<String, Any?>): ProductImage {
            val productReference = data["productId"] as DocumentReference

            return ProductImage(
                id = data["id"] as String,
                productId = productReference.id,
                image = data["image"] as? String
            )
        }
    }
}
