package com.example.firebasestore.data.firebase

import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await

object FirebaseStorage {
    private lateinit var storagerefence: StorageReference

    fun start() {
        storagerefence = FirebaseStorage.getInstance().reference
    }

    suspend fun getImageUrl(path: String): String? {
        return try {
            storagerefence.child(path).downloadUrl.await().toString()
        } catch (e: Exception) {
            null
        }
    }
}