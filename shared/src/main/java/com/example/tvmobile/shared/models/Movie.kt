package com.example.tvmobile.shared.models

data class Movie(
    var id: String = "",
    var title: String = "",
    var year: String = "",
    var genre: String = "",
    var rating: String = "",
    var imageUrl: String = "",
    var description: String = "",
    var videoUrl: String = ""
)

data class PlaylistItem(
    var id: String = "",
    var movie: Movie = Movie(),
    var addedBy: String = "",
    var timestamp: Long = 0L,
    var isWatched: Boolean = false
)

data class Playlist(
    val items: List<PlaylistItem> = emptyList(),
    val currentIndex: Int = 0
)