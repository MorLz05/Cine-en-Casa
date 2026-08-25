// tv/src/main/java/com/example/tvmobile/tv/TVMainActivity.kt
package com.example.tvmobile.tv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import com.example.tvmobile.shared.models.PlaylistItem
import com.example.tvmobile.shared.repositories.PlaylistRepository

class TvMainActivity : ComponentActivity() {
    private val repository = PlaylistRepository()

    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                ) {
                    TvApp(repository)
                }
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun TvApp(repository: PlaylistRepository) {
    val playlist by repository.playlist.collectAsState()
    var selectedItem by remember { mutableStateOf<PlaylistItem?>(null) }

    LaunchedEffect(playlist) {
        if (playlist.isNotEmpty()) {
            // Buscar la primera película NO vista
            val firstUnwatched = playlist.find { !it.isWatched }
            // Si hay una no vista, seleccionarla, sino seleccionar la primera
            val itemToSelect = firstUnwatched ?: playlist.first()

            // Solo actualizar si es diferente al seleccionado actual
            if (selectedItem?.movie?.id != itemToSelect.movie.id) {
                selectedItem = itemToSelect
            }
        } else {
            // Si la playlist está vacía, limpiar selección
            selectedItem = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0A0A0A),
                        Color(0xFF1A1A2E),
                        Color(0xFF0A0A0A)
                    )
                )
            )
            .padding(24.dp)
    ) {
        // HEADER
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
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
                    Text(
                        text = "🎬",
                        fontSize = 32.sp
                    )
                }

                Column {
                    Text(
                        text = "CINE EN CASA",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = "Disfruta en tu televisor",
                        fontSize = 16.sp,
                        color = Color(0xFF808080),
                        letterSpacing = 1.sp
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE50914))
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "${playlist.size} en cola",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // CONTENIDO PRINCIPAL
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // COLUMNA IZQUIERDA - REPRODUCTOR
            Column(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxSize()
            ) {
                selectedItem?.let { item ->
                    // Tarjeta de reproducción - CORREGIDA SIN CardDefaults
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(350.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF1A1A2E))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color(0xFF16213E),
                                            Color(0xFF0F3460),
                                            Color(0xFF1A1A2E)
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(RoundedCornerShape(40.dp))
                                        .background(Color(0xFFE50914).copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "▶️",
                                        fontSize = 40.sp
                                    )
                                }

                                Text(
                                    text = item.movie.title,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "📅 ${item.movie.year}",
                                        fontSize = 16.sp,
                                        color = Color(0xFFB0B0B0)
                                    )
                                    Text(
                                        text = "•",
                                        color = Color(0xFF404040)
                                    )
                                    Text(
                                        text = "🎭 ${item.movie.genre}",
                                        fontSize = 16.sp,
                                        color = Color(0xFFB0B0B0)
                                    )
                                    Text(
                                        text = "•",
                                        color = Color(0xFF404040)
                                    )
                                    Text(
                                        text = "⭐ ${item.movie.rating}",
                                        fontSize = 16.sp,
                                        color = Color(0xFFFFB800),
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                if (item.movie.description.isNotEmpty()) {
                                    Text(
                                        text = item.movie.description,
                                        fontSize = 14.sp,
                                        color = Color(0xFF808080),
                                        textAlign = TextAlign.Center,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.padding(horizontal = 32.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "🟢",
                                        fontSize = 16.sp
                                    )
                                    Text(
                                        text = "REPRODUCIENDO AHORA",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF4CAF50),
                                        letterSpacing = 2.sp
                                    )
                                }

                                Text(
                                    text = "El contenido se reproduce en tu televisor",
                                    fontSize = 13.sp,
                                    color = Color(0xFF606060),
                                    letterSpacing = 1.sp
                                )
                            }
                        }
                    }


                } ?: run {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(350.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF1A1A2E)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "🎬",
                                fontSize = 64.sp
                            )
                            Text(
                                text = "Selecciona una película",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Añade desde tu teléfono móvil",
                                fontSize = 16.sp,
                                color = Color(0xFF808080)
                            )
                        }
                    }
                }
            }

            // COLUMNA DERECHA - LISTA DE REPRODUCCIÓN
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                Text(
                    text = "COLA DE REPRODUCCIÓN",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE50914),
                    letterSpacing = 2.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                if (playlist.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF1A1A2E)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "📭",
                                fontSize = 48.sp
                            )
                            Text(
                                text = "Cola vacía",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Añade películas desde el móvil",
                                fontSize = 14.sp,
                                color = Color(0xFF808080)
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(playlist) { item ->
                            TvPlaylistItem(
                                item = item,
                                isSelected = item.movie.id == selectedItem?.movie?.id,
                                onSelect = {
                                    selectedItem = item
                                },
                                onMarkWatched = { repository.markAsWatched(item.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun TvPlaylistItem(
    item: PlaylistItem,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onMarkWatched: () -> Unit
) {
    // Usando Box en lugar de Card para evitar problemas de API
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(
                when {
                    isSelected -> Color(0xFFE50914).copy(alpha = 0.2f)
                    item.isWatched -> Color(0xFF1A3A2E)
                    else -> Color(0xFF1A1A2E)
                }
            )
            .clickable { onSelect() }
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFE50914))
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.Transparent)
                    )
                }

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = if (isSelected) "▶️" else if (item.isWatched) "✅" else "📺",
                            fontSize = 14.sp
                        )
                        Text(
                            text = item.movie.title,
                            fontSize = 18.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color.White else Color(0xFFB0B0B0),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "📅 ${item.movie.year}",
                            fontSize = 12.sp,
                            color = Color(0xFF606060)
                        )
                        Text(
                            text = "⭐ ${item.movie.rating}",
                            fontSize = 12.sp,
                            color = Color(0xFFFFB800)
                        )

                    }
                }
            }


        }
    }
}
