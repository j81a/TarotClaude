package com.waveapp.tarotai.presentation.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.waveapp.tarotai.domain.model.CardOrientation
import com.waveapp.tarotai.domain.model.DrawnCard
import java.text.SimpleDateFormat
import java.util.*

/**
 * Pantalla de detalle de una lectura guardada.
 *
 * Muestra:
 * - Encabezado (consultante, fecha, tipo de tirada)
 * - Pregunta completa
 * - Cartas (clickeables para ver detalle)
 * - Interpretación completa
 * - Campo de notas editable con autosave
 *
 * @param onNavigateBack Callback para navegar atrás
 * @param onCardClick Callback cuando se hace click en una carta (recibe card ID)
 * @param viewModel ViewModel del detalle (inyectado por Hilt)
 *
 * @since v1.1.0
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadingDetailScreen(
    onNavigateBack: () -> Unit,
    onCardClick: (Int) -> Unit,
    viewModel: ReadingDetailViewModel = hiltViewModel()
) {
    val reading by viewModel.reading.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val isSavingNotes by viewModel.isSavingNotes.collectAsState()
    val notesSaved by viewModel.notesSaved.collectAsState()

    var notesText by remember { mutableStateOf("") }

    // Sincronizar notas con el estado del ViewModel
    LaunchedEffect(reading) {
        reading?.notes?.let { notes ->
            if (notesText.isEmpty()) {
                notesText = notes
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Lectura") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Error",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = error ?: "Error desconocido",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = onNavigateBack) {
                            Text("Volver")
                        }
                    }
                }
            }

            reading != null -> {
                ReadingDetailContent(
                    reading = reading!!,
                    notesText = notesText,
                    onNotesChanged = {
                        notesText = it
                        viewModel.onNotesChanged(it)
                    },
                    isSavingNotes = isSavingNotes,
                    notesSaved = notesSaved,
                    onCardClick = onCardClick,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }
        }
    }
}

@Composable
private fun ReadingDetailContent(
    reading: com.waveapp.tarotai.domain.model.ReadingHistory,
    notesText: String,
    onNotesChanged: (String) -> Unit,
    isSavingNotes: Boolean,
    notesSaved: Boolean,
    onCardClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Encabezado
        ReadingHeader(reading = reading)

        Spacer(modifier = Modifier.height(24.dp))

        // Pregunta
        if (!reading.question.isNullOrBlank()) {
            SectionTitle("Pregunta")
            Text(
                text = reading.question,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Cartas
        SectionTitle("Cartas")
        Spacer(modifier = Modifier.height(8.dp))
        reading.drawnCards.forEach { drawnCard ->
            DrawnCardItem(
                drawnCard = drawnCard,
                onClick = { onCardClick(drawnCard.card.id) }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Interpretación
        SectionTitle("Interpretación")
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = reading.interpretation.generalInterpretation,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Notas editables
        SectionTitle("Notas Personales")
        Spacer(modifier = Modifier.height(8.dp))

        NotesTextField(
            notes = notesText,
            onNotesChanged = onNotesChanged,
            isSaving = isSavingNotes,
            saved = notesSaved
        )
    }
}

@Composable
private fun ReadingHeader(reading: com.waveapp.tarotai.domain.model.ReadingHistory) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = reading.consultantName,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = formatTimestamp(reading.timestamp),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = getSpreadTypeName(reading.spreadType.name),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun DrawnCardItem(
    drawnCard: DrawnCard,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = drawnCard.card.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = drawnCard.positionName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = if (drawnCard.orientation == CardOrientation.UPRIGHT) "Derecha" else "Invertida",
                style = MaterialTheme.typography.bodySmall,
                color = if (drawnCard.orientation == CardOrientation.UPRIGHT)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
private fun NotesTextField(
    notes: String,
    onNotesChanged: (String) -> Unit,
    isSaving: Boolean,
    saved: Boolean
) {
    Column {
        OutlinedTextField(
            value = notes,
            onValueChange = onNotesChanged,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Agrega tus notas personales...") },
            minLines = 5,
            maxLines = 10,
            supportingText = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("${notes.length}/2000 caracteres")

                    // Indicador de guardado
                    when {
                        isSaving -> Text("Guardando...", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        saved -> Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Guardado",
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Guardado", color = Color(0xFF4CAF50))
                        }
                    }
                }
            }
        )
    }
}

private fun formatTimestamp(timestamp: Long): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.getDefault())
    return dateFormat.format(Date(timestamp))
}

private fun getSpreadTypeName(spreadType: String): String {
    return when (spreadType) {
        "YES_NO" -> "Tirada Sí o No"
        "PRESENT" -> "Tirada de 3 Cartas (Presente)"
        "TENDENCY" -> "Tirada de 3 Cartas (Tendencia)"
        "CROSS" -> "Cruz Celta"
        "SIMPLE" -> "Carta Simple"
        else -> spreadType
    }
}
