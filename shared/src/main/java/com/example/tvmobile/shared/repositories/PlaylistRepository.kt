package com.example.tvmobile.shared.repositories

import android.util.Log
import com.example.tvmobile.shared.models.Movie
import com.example.tvmobile.shared.models.PlaylistItem
import com.example.tvmobile.shared.services.FirebaseService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PlaylistRepository {
    private val firebaseService = FirebaseService()
    private val _playlist = MutableStateFlow<List<PlaylistItem>>(emptyList())
    val playlist: StateFlow<List<PlaylistItem>> = _playlist.asStateFlow()
    private val TAG = "PlaylistRepository"

    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        // Escuchar cambios en Firebase con manejo de errores
        scope.launch {
            try {
                firebaseService.listenToPlaylist().collectLatest { items ->
                    _playlist.value = items
                    Log.d(TAG, "📋 Playlist actualizada: ${items.size} items")
                }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Error en listener de playlist: ${e.message}", e)
            }
        }
    }

    private val movieCatalog = listOf(
        Movie(
            id = "1",
            title = "Inception",
            year = "2010",
            genre = "Ciencia Ficción",
            rating = "4.8",
            description = "Un ladrón que roba secretos del subconsciente",
            videoUrl = "https://www.youtube.com/embed/YoHD9XEInc0?autoplay=1&rel=0"
        ),
        Movie(
            id = "2",
            title = "The Dark Knight",
            year = "2008",
            genre = "Acción",
            rating = "4.9",
            description = "Batman enfrenta al Joker",
            videoUrl = "https://www.youtube.com/embed/EXeTwQWrcwY?autoplay=1&rel=0"
        ),
        Movie(
            id = "3",
            title = "Interstellar",
            year = "2014",
            genre = "Ciencia Ficción",
            rating = "4.7",
            description = "Un viaje a través del espacio-tiempo",
            videoUrl = "https://www.youtube.com/embed/LYS2O1nl9iM?autoplay=1&rel=0"
        ),
        Movie(
            id = "4",
            title = "The Matrix",
            year = "1999",
            genre = "Ciencia Ficción",
            rating = "4.6",
            description = "Descubre la realidad virtual",
            videoUrl = "https://www.youtube.com/embed/OM0tSTEQCQA?autoplay=1&rel=0"
        ),
        Movie(
            id = "5",
            title = "Pulp Fiction",
            year = "1994",
            genre = "Crimen",
            rating = "4.7",
            description = "Historias entrelazadas de crimen",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
        )
    )

    fun getMovies(): List<Movie> = movieCatalog

    fun addToPlaylist(movie: Movie, addedBy: String = "Móvil") {
        val item = PlaylistItem(
            id = System.currentTimeMillis().toString(),
            movie = movie,
            addedBy = addedBy,
            timestamp = System.currentTimeMillis(),
            isWatched = false
        )
        Log.d(TAG, "➕ Añadiendo a playlist: ${movie.title}")

        scope.launch {
            try {
                firebaseService.addToPlaylist(item)
                Log.d(TAG, "✅ ${movie.title} añadida exitosamente")
            } catch (e: Exception) {
                Log.e(TAG, "❌ Error al añadir ${movie.title}: ${e.message}", e)
                // Aquí podrías mostrar un Toast o Snackbar al usuario
            }
        }
    }

    fun removeFromPlaylist(itemId: String) {
        Log.d(TAG, "🗑️ Eliminando item: $itemId")
        scope.launch {
            try {
                firebaseService.removeFromPlaylist(itemId)
                Log.d(TAG, "✅ Item eliminado exitosamente")
            } catch (e: Exception) {
                Log.e(TAG, "❌ Error al eliminar: ${e.message}", e)
            }
        }
    }

    fun markAsWatched(itemId: String) {
        scope.launch {
            try {
                firebaseService.markAsWatched(itemId)
                Log.d(TAG, "✅ Marcado como visto")
            } catch (e: Exception) {
                Log.e(TAG, "❌ Error al marcar como visto: ${e.message}", e)
            }
        }
    }

    fun clearPlaylist() {
        _playlist.value.forEach { item ->
            scope.launch {
                try {
                    firebaseService.removeFromPlaylist(item.id)
                } catch (e: Exception) {
                    Log.e(TAG, "Error al limpiar item: ${e.message}", e)
                }
            }
        }
    }
}