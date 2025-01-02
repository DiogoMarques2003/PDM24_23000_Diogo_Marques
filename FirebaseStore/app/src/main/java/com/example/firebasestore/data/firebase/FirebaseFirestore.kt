package com.example.firebasestore.data.firebase

import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

object FirebaseFirestore {
    private lateinit var firestore: FirebaseFirestore

    fun start() {
        firestore = Firebase.firestore
    }

    fun getReferenceById(collection: String, id: String): DocumentReference {
        return firestore.collection(collection).document(id)
    }

    // Método para inserir dados
    suspend fun changeData(
        collection: String,
        data: Map<String, Any?>,
        documentId: String? = null
    ): String? {
        return try {
            val documentReference = if (documentId != null) {
                firestore.collection(collection).document(documentId).set(data).await()
                firestore.collection(collection)
                    .document(documentId) // Retorna a referência do documento criado com ID fornecido
            } else {
                val result = firestore.collection(collection).add(data).await()
                result // Retorna a referência do documento criado com ID automático
            }
            documentReference.id // Retorna o ID do documento
        } catch (e: Exception) {
            null // Retorna null em caso de erro
        }
    }

    // Método para obter dados
    suspend fun getData(collection: String, documentId: String? = null): List<Map<String, Any>>? {
        return try {
            if (documentId != null) {
                // Obter um único documento e retornar como um Map
                val snapshot = firestore.collection(collection).document(documentId).get().await()
                if (snapshot.exists()) {
                    val data = snapshot.data ?: emptyMap()
                    listOf(data + ("id" to snapshot.id)) // Adicionar o id ao objeto
                } else {
                    null
                }
            } else {
                // Obter todos os documentos da coleção e retornar como uma lista de Maps
                val snapshot = firestore.collection(collection).get().await()
                snapshot.documents.mapNotNull { doc ->
                    val data = doc.data ?: return@mapNotNull null
                    data + ("id" to doc.id) // Adiciona o id ao objeto
                }
            }
        } catch (e: Exception) {
            null
        }
    }

    fun listenToData(
        collection: String,
        documentId: String? = null,
        onDataChanged: (List<Map<String, Any>>?) -> Unit,
        onError: (Exception) -> Unit
    ): ListenerRegistration {
        return if (documentId != null) {
            // Listener para um único documento
            firestore.collection(collection).document(documentId)
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        onError(e)
                        return@addSnapshotListener
                    }

                    if (snapshot != null && snapshot.exists()) {
                        val data = snapshot.data ?: emptyMap()
                        onDataChanged(listOf(data + ("id" to snapshot.id))) // Retorna o documento como uma lista
                    } else {
                        onDataChanged(null) // Documento não encontrado
                    }
                }
        } else {
            // Listener para todos os documentos de uma coleção
            firestore.collection(collection)
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        onError(e)
                        return@addSnapshotListener
                    }

                    if (snapshot != null) {
                        val documents = snapshot.documents.mapNotNull { doc ->
                            val data = doc.data ?: return@mapNotNull null
                            data + ("id" to doc.id) // Adiciona o id a cada documento
                        }
                        onDataChanged(documents) // Retorna todos os documentos
                    } else {
                        onDataChanged(null) // Nenhum dado encontrado
                    }
                }
        }
    }

    // Método para apagar dados
    suspend fun deleteData(collection: String, documentId: String): Boolean {
        return try {
            firestore.collection(collection).document(documentId).delete().await()
            true
        } catch (e: Exception) {
            false
        }
    }
}