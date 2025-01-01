package com.example.firebasestore.data.firebase

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await

object FirebaseAutentication {
    private lateinit var auth: FirebaseAuth

    fun start() {
        auth = Firebase.auth
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    suspend fun createAccount(email: String, password: String): String? {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            result.user?.uid // Devolver o id do user
        } catch (e: Exception) {
            null // Retorna null caso não consiga criar conta
        }
    }

    suspend fun loginAccount(email: String, password: String): String? {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            result.user?.uid
        } catch (e: Exception) {
            null // Retorna null caso não consiga fazer login
        }
    }

    fun logoutAccount() {
        auth.signOut()
    }
}