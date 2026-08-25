package com.example.tvmobile.shared.services

import android.util.Log
import com.example.tvmobile.shared.models.PlaylistItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseService {
    private val db = FirebaseFirestore.getInstance()
    private val PLAYLIST_COLLECTION = "playlist"
    private val TAG = "FirebaseService"

    suspend fun addToPlaylist(item: PlaylistItem) {
        try {
            Log.d(TAG, "Intentando guardar: ${item.movie.title}")
            db.collection(PLAYLIST_COLLECTION)
                .document(item.id)
                .set(item)
                .await()
            Log.d(TAG, "✅ Guardado exitoso: ${item.movie.title}")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error al guardar: ${e.message}", e)
            throw e // Relanzamos para que el repositorio lo maneje
        }
    }

    suspend fun removeFromPlaylist(itemId: String) {
        try {
            db.collection(PLAYLIST_COLLECTION)
                .document(itemId)
                .delete()
                .await()
        } catch (e: Exception) {
            Log.e(TAG, "Error al eliminar: ${e.message}", e)
            throw e
        }
    }

    suspend fun markAsWatched(itemId: String) {
        try {
            db.collection(PLAYLIST_COLLECTION)
                .document(itemId)
                .update("isWatched", true)
                .await()
        } catch (e: Exception) {
            Log.e(TAG, "Error al marcar como visto: ${e.message}", e)
            throw e
        }
    }

    fun listenToPlaylist(): Flow<List<PlaylistItem>> = callbackFlow {
        Log.d(TAG, "Iniciando listener de playlist")
        val subscription = db.collection(PLAYLIST_COLLECTION)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, "Error en listener: ${error.message}", error)
                    close(error)
                    return@addSnapshotListener
                }

                try {
                    val items = snapshot?.documents?.mapNotNull { doc ->
                        doc.toObject(PlaylistItem::class.java)
                    } ?: emptyList()

                    Log.d(TAG, "📋 Playlist actualizada: ${items.size} items")
                    trySend(items)
                } catch (e: Exception) {
                    Log.e(TAG, "Error al deserializar: ${e.message}", e)
                    close(e)
                }
            }

        awaitClose {
            Log.d(TAG, "Cerrando listener de playlist")
            subscription.remove()
        }
    }
}