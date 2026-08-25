// app/src/main/java/com/example/tvmobile/MainActivity.kt
package com.example.tvmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tvmobile.shared.models.Movie
import com.example.tvmobile.shared.models.PlaylistItem
import com.example.tvmobile.shared.repositories.PlaylistRepository

class MainActivity : ComponentActivity() {
    private val repository = PlaylistRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black // Fondo negro
                ) {
                    MobileApp(repository)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MobileApp(repository: PlaylistRepository) {
    var selectedTab by remember { mutableStateOf(0) }
    val playlist by repository.playlist.collectAsState()
    val movies = repository.getMovies()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Header con degradado y diseño premium
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1A1A2E),
                            Color(0xFF16213E),
                            Color(0xFF0F3460),
                            Color.Black
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center
            ) {
                // Icono y título
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFFE50914),
                                        Color(0xFFFF6B35)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Logo",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Text(
                        text = "Cine en Casa",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = 1.sp
                    )
                }

                Text(
                    text = "Las mejores películas en un mismo lugar",
                    fontSize = 16.sp,
                    color = Color(0xFFB0B0B0),
                    modifier = Modifier.padding(top = 8.dp),
                    letterSpacing = 0.5.sp
                )

                Text(
                    text = "Disfruta del cine desde tu TV",
                    fontSize = 14.sp,
                    color = Color(0xFF808080),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        // Tabs personalizadas
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color(0xFF1A1A2E),
            contentColor = Color.White,
            indicator = { tabPositions ->
                // El indicator debe ser un Box con posición específica
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    height = 3.dp,
                    color = Color(0xFFE50914) // Rojo Netflix
                )
            }
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = if (selectedTab == 0) Color(0xFFE50914) else Color.Gray
                        )
                        Text(
                            text = "Catálogo",
                            color = if (selectedTab == 0) Color.White else Color.Gray,
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = if (selectedTab == 1) Color(0xFFE50914) else Color.Gray
                        )
                        Text(
                            text = "Mi Cola (${playlist.size})",
                            color = if (selectedTab == 1) Color.White else Color.Gray,
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            )
        }

        when (selectedTab) {
            0 -> MovieCatalogScreen(repository, movies)
            1 -> PlaylistScreen(repository, playlist)
        }
    }
}

@Composable
fun MovieCatalogScreen(repository: PlaylistRepository, movies: List<Movie>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(movies) { movie ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1A1A2E)
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = movie.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "📅 ${movie.year}",
                                color = Color(0xFFB0B0B0),
                                fontSize = 12.sp
                            )
                            Text(
                                text = "•",
                                color = Color(0xFF404040)
                            )
                            Text(
                                text = "🎭 ${movie.genre}",
                                color = Color(0xFFB0B0B0),
                                fontSize = 12.sp
                            )
                            Text(
                                text = "•",
                                color = Color(0xFF404040)
                            )
                            Text(
                                text = "⭐ ${movie.rating}",
                                color = Color(0xFFFFB800),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (movie.description.isNotEmpty()) {
                            Text(
                                text = movie.description,
                                color = Color(0xFF808080),
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Button(
                        onClick = { repository.addToPlaylist(movie) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE50914),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .width(100.dp)
                            .height(40.dp)
                    ) {
                        Text(
                            text = "Ver",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PlaylistScreen(repository: PlaylistRepository, playlist: List<PlaylistItem>) {
    if (playlist.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(60.dp))
                        .background(Color(0xFF1A1A2E)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🎬",
                        fontSize = 48.sp
                    )
                }

                Text(
                    text = "Tu cola está vacía",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = "Explora el catálogo y añade\nlas mejores películas",
                    fontSize = 16.sp,
                    color = Color(0xFF808080),
                    textAlign = TextAlign.Center
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(playlist) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (item.isWatched)
                            Color(0xFF1A3A2E)
                        else
                            Color(0xFF1A1A2E)
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 4.dp
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                if (item.isWatched) {
                                    Text(
                                        text = "✅",
                                        fontSize = 16.sp
                                    )
                                } else {
                                    Text(
                                        text = "▶️",
                                        fontSize = 16.sp
                                    )
                                }
                                Text(
                                    text = item.movie.title,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = if (item.isWatched) Color(0xFF4CAF50) else Color.White,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Text(
                                text = "👤 ${item.addedBy}",
                                color = Color(0xFF808080),
                                fontSize = 12.sp
                            )

                            if (item.isWatched) {
                                Text(
                                    text = "✅ Visto",
                                    color = Color(0xFF4CAF50),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        IconButton(
                            onClick = { repository.removeFromPlaylist(item.id) },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Text(
                                text = "🗑️",
                                fontSize = 20.sp
                            )
                        }
                    }
                }
            }
        }
    }
}