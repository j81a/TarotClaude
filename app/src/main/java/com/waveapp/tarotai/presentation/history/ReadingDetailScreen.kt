package com.waveapp.tarotai.presentation.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.waveapp.tarotai.domain.model.DrawnCard
import com.waveapp.tarotai.domain.model.LayoutType
import com.waveapp.tarotai.domain.model.ReadingNote
import com.waveapp.tarotai.presentation.reading.components.CrossCardsLayout
import com.waveapp.tarotai.presentation.reading.components.GeneralInterpretationCard
import com.waveapp.tarotai.presentation.reading.components.HorizontalCardsLayout
import com.waveapp.tarotai.presentation.reading.components.YesNoAnswerCard
import java.text.SimpleDateFormat
import java.util.*

/**
 * Pantalla de detalle de una lectura guardada.
 *
 * v1.2.0: Refactorizada completamente para:
 * - Mostrar cartas en layout visual (igual que ReadingScreen)
 * - Mostrar interpretación con cards (igual que ReadingScreen)
 * - Sistema de notas mejorado (lista con fecha, editar/eliminar)
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
    onNavigateToHome: () -> Unit,
    viewModel: ReadingDetailViewModel = hiltViewModel()
) {
    val reading by viewModel.reading.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

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
                },
                actions = {
                    IconButton(onClick = onNavigateToHome) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Volver al inicio"
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
                    onCardClick = onCardClick,
                    onAddNote = viewModel::addNote,
                    onEditNote = viewModel::editNote,
                    onDeleteNote = viewModel::deleteNote,
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
    onCardClick: (Int) -> Unit,
    onAddNote: (String) -> Unit,
    onEditNote: (ReadingNote, String) -> Unit,
    onDeleteNote: (ReadingNote) -> Unit,
    modifier: Modifier = Modifier
) {
    var showNoteDialog by remember { mutableStateOf(false) }
    var editingNote by remember { mutableStateOf<ReadingNote?>(null) }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Encabezado
        ReadingHeader(reading = reading)

        // Pregunta
        if (!reading.question.isNullOrBlank()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Pregunta:",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = reading.question,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }

        // Cartas en layout visual
        SectionTitle("Cartas")
        val config = com.waveapp.tarotai.domain.model.SpreadConfiguration.fromType(reading.spreadType)
        when (config.layout) {
            LayoutType.HORIZONTAL -> {
                HorizontalCardsLayout(
                    drawnCards = reading.drawnCards,
                    onCardClick = { drawnCard -> onCardClick(drawnCard.card.id) }
                )
            }
            LayoutType.CROSS -> {
                CrossCardsLayout(
                    drawnCards = reading.drawnCards,
                    onCardClick = { drawnCard -> onCardClick(drawnCard.card.id) }
                )
            }
        }

        // Interpretación con cards
        SectionTitle("Interpretación")

        // Si es tirada Sí/No, mostrar respuesta
        if (reading.interpretation.yesNoAnswer != null) {
            YesNoAnswerCard(
                answer = reading.interpretation.yesNoAnswer,
                justification = null
            )
        }

        // Mensaje general
        GeneralInterpretationCard(
            generalInterpretation = reading.interpretation.generalInterpretation
        )

        // Notas
        SectionTitle("Notas Personales")
        NotesSection(
            notes = reading.notes,
            onAddNote = {
                editingNote = null
                showNoteDialog = true
            },
            onEditNote = { note ->
                editingNote = note
                showNoteDialog = true
            },
            onDeleteNote = onDeleteNote
        )
    }

    // Diálogo para agregar/editar nota
    if (showNoteDialog) {
        NoteDialog(
            note = editingNote,
            onDismiss = {
                showNoteDialog = false
                editingNote = null
            },
            onSave = { text ->
                if (editingNote != null) {
                    onEditNote(editingNote!!, text)
                } else {
                    onAddNote(text)
                }
                showNoteDialog = false
                editingNote = null
            }
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
private fun NotesSection(
    notes: List<ReadingNote>,
    onAddNote: () -> Unit,
    onEditNote: (ReadingNote) -> Unit,
    onDeleteNote: (ReadingNote) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Botón agregar nota
        OutlinedButton(
            onClick = onAddNote,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text("Agregar Nota")
        }

        // Lista de notas
        if (notes.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(
                    text = "No hay notas todavía",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            // Ordenar notas por fecha descendente (más recientes primero)
            notes.sortedByDescending { it.timestamp }.forEach { note ->
                NoteItem(
                    note = note,
                    onEdit = { onEditNote(note) },
                    onDelete = { onDeleteNote(note) }
                )
            }
        }
    }
}

@Composable
private fun NoteItem(
    note: ReadingNote,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            // Fecha y acciones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatTimestamp(note.timestamp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar nota",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar nota",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Texto de la nota
            Text(
                text = note.text,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun NoteDialog(
    note: ReadingNote?,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var text by remember { mutableStateOf(note?.text ?: "") }

    Dialog(onDismissRequest = onDismiss) {
        Card {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = if (note == null) "Agregar Nota" else "Editar Nota",
                    style = MaterialTheme.typography.titleLarge
                )

                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Escribe tu nota...") },
                    minLines = 5,
                    maxLines = 10,
                    supportingText = {
                        Text("${text.length}/2000 caracteres")
                    }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { onSave(text) },
                        enabled = text.isNotBlank() && text.length <= 2000
                    ) {
                        Text("Guardar")
                    }
                }
            }
        }
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
