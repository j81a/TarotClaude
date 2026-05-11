package com.waveapp.tarotai.presentation.manualload

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.waveapp.tarotai.R
import com.waveapp.tarotai.domain.model.CardOrientation
import com.waveapp.tarotai.domain.model.ManualLoadCard
import com.waveapp.tarotai.domain.model.SpreadConfiguration
import com.waveapp.tarotai.presentation.reading.components.GeneralInterpretationCard
import com.waveapp.tarotai.presentation.reading.components.YesNoAnswerCard
import com.waveapp.tarotai.presentation.reading.components.HorizontalCardsLayout
import com.waveapp.tarotai.presentation.reading.components.CrossCardsLayout

/**
 * Pantalla de carga manual de tirada.
 *
 * Permite al usuario cargar una tirada física carta por carta, seleccionando:
 * - Qué carta está en cada posición
 * - La orientación de cada carta (derecha/invertida)
 *
 * Layout:
 * - Header con info de la tirada y progreso
 * - Grid/Lista de posiciones con placeholders
 * - Botón "Generar Interpretación" (habilitado cuando está completa)
 *
 * Navegación:
 * - Click en posición vacía → CardSelectorScreen
 * - Click en posición llena → Dialog para cambiar/remover
 * - Interpretación completa → InterpretationScreen
 *
 * @param onNavigateBack Callback para volver atrás
 * @param onNavigateToCardSelector Callback para ir al selector de cartas (positionIndex)
 * @param onNavigateToInterpretation Callback cuando se genera interpretación exitosamente
 * @param viewModel ViewModel inyectado por Hilt
 *
 * @since v1.1.0
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManualLoadScreen(
    onNavigateBack: () -> Unit,
    onNavigateToCardSelector: (Int) -> Unit,
    onNavigateToReadingDetail: (Long) -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: ManualLoadViewModel = hiltViewModel()
) {
    val configuration by viewModel.configuration.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val savedReadingId by viewModel.savedReadingId.collectAsState()
    val interpretation by viewModel.interpretationGenerated.collectAsState() // v1.2.0
    val saveState by viewModel.saveState.collectAsState() // v1.2.0

    // Navegar al detalle de la lectura guardada cuando se genera y guarda
    LaunchedEffect(savedReadingId) {
        savedReadingId?.let { readingId ->
            onNavigateToReadingDetail(readingId)
            viewModel.clearSavedReadingId()
        }
    }

    // Mostrar error en Snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(error) {
        error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cargar Tirada Manual") },
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
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Header con info y progreso
            // v1.2.0: Usar valor por defecto si consultantName está vacío
            ManualLoadHeader(
                consultantName = configuration.consultantName.ifBlank { "Lectura personal" },
                spreadTypeName = getSpreadTypeName(configuration.spreadType.name),
                question = configuration.question,
                progress = configuration.getProgress()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // v1.2.0: Layout visual de cartas (dorso o reveladas)
            val spreadConfig = SpreadConfiguration.fromType(configuration.spreadType)
            val drawnCards = configuration.state.toDrawnCards()
            val isInterpreted = interpretation != null

            when (spreadConfig.layout) {
                com.waveapp.tarotai.domain.model.LayoutType.HORIZONTAL -> {
                    ManualLoadCardsLayout(
                        drawnCards = drawnCards,
                        spreadConfig = spreadConfig,
                        isInterpreted = isInterpreted,  // v1.2.0: Pasar estado para iconos
                        onCardClick = if (!isInterpreted) {
                            { index -> onNavigateToCardSelector(index) }
                        } else {
                            { } // No clickeable después de interpretar
                        }
                    )
                }
                com.waveapp.tarotai.domain.model.LayoutType.CROSS -> {
                    ManualLoadCrossLayout(
                        drawnCards = drawnCards,
                        spreadConfig = spreadConfig,
                        isInterpreted = isInterpreted,  // v1.2.0: Pasar estado para iconos
                        onCardClick = if (!isInterpreted) {
                            { index -> onNavigateToCardSelector(index) }
                        } else {
                            { } // No clickeable después de interpretar
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón de generar interpretación
            Button(
                onClick = { viewModel.generateInterpretation() },
                modifier = Modifier.fillMaxWidth(),
                enabled = configuration.isComplete() && !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Generando interpretación...")
                } else {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Generar Interpretación")
                }
            }

            // Texto de ayuda
            if (!configuration.isComplete()) {
                Spacer(modifier = Modifier.height(8.dp))
                val (current, required) = configuration.getProgress()
                Text(
                    text = "Selecciona las $required cartas para continuar ($current/$required)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // v1.2.0: Sección de interpretación (si existe)
            interpretation?.let { interp ->
                Spacer(modifier = Modifier.height(24.dp))

                HorizontalDivider(
                    thickness = 2.dp,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Si es tirada Sí/No, mostrar respuesta
                if (interp.yesNoAnswer != null) {
                    com.waveapp.tarotai.presentation.reading.components.YesNoAnswerCard(
                        answer = interp.yesNoAnswer,
                        justification = null
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Botón de guardar en historial
                val finalConsultantName = configuration.consultantName.ifBlank { "Lectura personal" }

                when (saveState) {
                    is com.waveapp.tarotai.presentation.reading.viewmodel.SaveState.NotSaved -> {
                        Button(
                            onClick = { viewModel.saveToHistory() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text("Guardar en Historial")
                        }
                    }
                    is com.waveapp.tarotai.presentation.reading.viewmodel.SaveState.Saving -> {
                        Button(
                            onClick = {},
                            enabled = false,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(16.dp)
                                    .padding(end = 8.dp),
                                strokeWidth = 2.dp
                            )
                            Text("Guardando...")
                        }
                    }
                    is com.waveapp.tarotai.presentation.reading.viewmodel.SaveState.Saved -> {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(end = 12.dp)
                                )
                                Text(
                                    text = "Lectura guardada en el historial",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }
                    is com.waveapp.tarotai.presentation.reading.viewmodel.SaveState.Error -> {
                        val errorState = saveState as com.waveapp.tarotai.presentation.reading.viewmodel.SaveState.Error
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "Error al guardar: ${errorState.message}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onErrorContainer
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                TextButton(onClick = {
                                    viewModel.saveToHistory()
                                }) {
                                    Text("Reintentar")
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Interpretación general
                com.waveapp.tarotai.presentation.reading.components.GeneralInterpretationCard(
                    generalInterpretation = interp.generalInterpretation
                )
            }
        }
    }
}

@Composable
private fun ManualLoadHeader(
    consultantName: String,
    spreadTypeName: String,
    question: String?,
    progress: Pair<Int, Int>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = consultantName,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = spreadTypeName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )

            if (!question.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "\"$question\"",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progreso
            val (current, required) = progress
            LinearProgressIndicator(
                progress = { current.toFloat() / required.toFloat() },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Progreso: $current de $required cartas",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun CardPositionItem(
    positionName: String,
    positionIndex: Int,
    card: ManualLoadCard?,
    onCardClick: () -> Unit,
    onRemoveCard: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onCardClick),
        border = if (card == null) {
            BorderStroke(2.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
        } else null
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = positionName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )

                if (card != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = card.card.name,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = if (card.orientation == CardOrientation.UPRIGHT) "Derecha" else "Invertida",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (card.orientation == CardOrientation.UPRIGHT)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.error
                    )
                } else {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Toca para seleccionar carta",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (card != null) {
                IconButton(onClick = onRemoveCard) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Remover carta",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar carta",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
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

/**
 * v1.2.0: Layout horizontal para carga manual (muestra dorso si no hay carta).
 */
@Composable
private fun ManualLoadCardsLayout(
    drawnCards: List<com.waveapp.tarotai.domain.model.DrawnCard>,
    spreadConfig: SpreadConfiguration,
    isInterpreted: Boolean,
    onCardClick: (Int) -> Unit
) {
    if (spreadConfig.positions.size == 1) {
        // Una sola carta: centrada
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ManualLoadCardItem(
                drawnCard = drawnCards.getOrNull(0),
                positionName = spreadConfig.positions[0],
                positionIndex = 0,
                onCardClick = onCardClick,
                isInterpreted = isInterpreted,
                modifier = Modifier.width(220.dp)
            )
        }
    } else {
        // Múltiples cartas: en fila
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            spreadConfig.positions.forEachIndexed { index, positionName ->
                ManualLoadCardItem(
                    drawnCard = drawnCards.getOrNull(index),
                    positionName = positionName,
                    positionIndex = index,
                    onCardClick = onCardClick,
                    isInterpreted = isInterpreted,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

/**
 * v1.2.0: Layout en cruz para carga manual (muestra dorso si no hay carta).
 */
@Composable
private fun ManualLoadCrossLayout(
    drawnCards: List<com.waveapp.tarotai.domain.model.DrawnCard>,
    spreadConfig: SpreadConfiguration,
    isInterpreted: Boolean,
    onCardClick: (Int) -> Unit
) {
    val cardWidth = 110.dp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Arriba (posición 2)
        if (spreadConfig.positions.size > 2) {
            ManualLoadCardItem(
                drawnCard = drawnCards.getOrNull(2),
                positionName = spreadConfig.positions[2],
                positionIndex = 2,
                onCardClick = onCardClick,
                isInterpreted = isInterpreted,
                modifier = Modifier.width(cardWidth)
            )
        }

        // Centro (Izquierda, Centro, Derecha)
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Izquierda (posición 0)
            if (spreadConfig.positions.isNotEmpty()) {
                ManualLoadCardItem(
                    drawnCard = drawnCards.getOrNull(0),
                    positionName = spreadConfig.positions[0],
                    positionIndex = 0,
                    onCardClick = onCardClick,
                    isInterpreted = isInterpreted,
                    modifier = Modifier.width(cardWidth)
                )
            }

            // Centro (posición 4)
            if (spreadConfig.positions.size > 4) {
                ManualLoadCardItem(
                    drawnCard = drawnCards.getOrNull(4),
                    positionName = spreadConfig.positions[4],
                    positionIndex = 4,
                    onCardClick = onCardClick,
                    isInterpreted = isInterpreted,
                    modifier = Modifier.width(cardWidth)
                )
            }

            // Derecha (posición 1)
            if (spreadConfig.positions.size > 1) {
                ManualLoadCardItem(
                    drawnCard = drawnCards.getOrNull(1),
                    positionName = spreadConfig.positions[1],
                    positionIndex = 1,
                    onCardClick = onCardClick,
                    isInterpreted = isInterpreted,
                    modifier = Modifier.width(cardWidth)
                )
            }
        }

        // Abajo (posición 3)
        if (spreadConfig.positions.size > 3) {
            ManualLoadCardItem(
                drawnCard = drawnCards.getOrNull(3),
                positionName = spreadConfig.positions[3],
                positionIndex = 3,
                onCardClick = onCardClick,
                isInterpreted = isInterpreted,
                modifier = Modifier.width(cardWidth)
            )
        }
    }
}

/**
 * v1.2.0: Item de carta para carga manual.
 * Muestra dorso si no hay carta seleccionada, carta revelada si hay.
 * Muestra iconos según estado: '+' (agregar), 'lápiz' (editar), 'i' (info).
 */
@Composable
private fun ManualLoadCardItem(
    drawnCard: com.waveapp.tarotai.domain.model.DrawnCard?,
    positionName: String,
    positionIndex: Int,
    onCardClick: (Int) -> Unit,
    isInterpreted: Boolean = false,  // v1.2.0: Para saber qué icono mostrar
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable { onCardClick(positionIndex) }
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Nombre de la posición
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = positionName,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 2,
                    textAlign = TextAlign.Center,
                    fontSize = androidx.compose.ui.unit.TextUnit(10f, androidx.compose.ui.unit.TextUnitType.Sp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Imagen de la carta o dorso con icono según estado
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (drawnCard != null) {
                        // Carta revelada
                        androidx.compose.foundation.Image(
                            painter = painterResource(
                                id = getDrawableResourceId(drawnCard.card.imagePath)
                            ),
                            contentDescription = drawnCard.card.name,
                            modifier = Modifier
                                .fillMaxSize()
                                .then(
                                    if (drawnCard.orientation == CardOrientation.REVERSED) {
                                        Modifier.rotate(180f)
                                    } else {
                                        Modifier
                                    }
                                ),
                            contentScale = androidx.compose.ui.layout.ContentScale.Fit
                        )
                    } else {
                        // Dorso de carta
                        androidx.compose.foundation.Image(
                            painter = painterResource(id = com.waveapp.tarotai.R.drawable.card_back),
                            contentDescription = "Seleccionar carta",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = androidx.compose.ui.layout.ContentScale.Fit
                        )
                    }
                }

                // v1.2.0: Icono según estado
                val icon: ImageVector
                val description: String
                when {
                    drawnCard == null -> {
                        // Estado inicial: dorso sin seleccionar → icono '+'
                        icon = Icons.Filled.Add
                        description = "Agregar carta"
                    }
                    isInterpreted -> {
                        // Estado interpretado: carta seleccionada después de interpretar → icono 'i'
                        icon = Icons.Filled.Info
                        description = "Ver detalle de la carta"
                    }
                    else -> {
                        // Estado seleccionado pero antes de interpretar → icono 'lápiz'
                        icon = Icons.Filled.Edit
                        description = "Editar carta"
                    }
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 8.dp)
                        .size(32.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = description,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Nombre de la carta (si está seleccionada)
            if (drawnCard != null) {
                Text(
                    text = drawnCard.card.name,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = if (drawnCard.orientation == CardOrientation.UPRIGHT) "Derecha" else "Invertida",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (drawnCard.orientation == CardOrientation.UPRIGHT)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            } else {
                Text(
                    text = "Toca para seleccionar",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/**
 * Función helper para obtener el ID de recurso drawable desde el nombre.
 */
@Composable
private fun getDrawableResourceId(imagePath: String): Int {
    val context = androidx.compose.ui.platform.LocalContext.current
    return context.resources.getIdentifier(
        imagePath,
        "drawable",
        context.packageName
    ).takeIf { it != 0 } ?: com.waveapp.tarotai.R.drawable.ic_launcher_foreground
}
