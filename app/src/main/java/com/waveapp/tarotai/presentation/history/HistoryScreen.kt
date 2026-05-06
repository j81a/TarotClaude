package com.waveapp.tarotai.presentation.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

/**
 * Pantalla de historial de lecturas guardadas.
 *
 * Muestra:
 * - Lista de todas las lecturas guardadas (más reciente primero)
 * - Empty state si no hay lecturas
 * - FAB para nueva lectura
 * - Navegación a los detalles de cada lectura
 *
 * @param onNavigateBack Callback para navegar atrás
 * @param onReadingClick Callback cuando se hace click en una lectura (recibe ID)
 * @param onNewReading Callback para crear nueva lectura
 * @param viewModel ViewModel del historial (inyectado por Hilt)
 *
 * @since v1.1.0
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    onNavigateBack: () -> Unit,
    onReadingClick: (Long) -> Unit,
    onNewReading: () -> Unit,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val readings by viewModel.readings.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historial de Lecturas") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNewReading,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Nueva lectura"
                )
            }
        }
    ) { paddingValues ->
        if (readings.isEmpty()) {
            // Empty state
            EmptyHistoryState(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
        } else {
            // Lista de lecturas
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = readings,
                    key = { it.id }
                ) { reading ->
                    ReadingHistoryItem(
                        reading = reading,
                        onClick = { onReadingClick(reading.id) }
                    )
                }
            }
        }
    }
}

/**
 * Componente para el estado vacío (sin lecturas guardadas).
 */
@Composable
private fun EmptyHistoryState(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "📚",
            style = MaterialTheme.typography.displayLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "No hay lecturas guardadas",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Las lecturas que guardes aparecerán aquí.\nToca el botón + para realizar una nueva lectura.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
