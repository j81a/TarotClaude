package com.waveapp.tarotai.presentation.reading

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.waveapp.tarotai.R
import com.waveapp.tarotai.domain.model.CardOrientation
import com.waveapp.tarotai.domain.model.DrawnCard
import com.waveapp.tarotai.domain.model.LayoutType
import com.waveapp.tarotai.domain.model.SpreadType
import com.waveapp.tarotai.presentation.reading.components.CardInterpretationCard
import com.waveapp.tarotai.presentation.reading.components.CrossCardsLayout
import com.waveapp.tarotai.presentation.reading.components.GeneralInterpretationCard
import com.waveapp.tarotai.presentation.reading.components.HorizontalCardsLayout
import com.waveapp.tarotai.presentation.reading.components.YesNoAnswerCard
import com.waveapp.tarotai.presentation.reading.viewmodel.InterpretationUiState
import com.waveapp.tarotai.presentation.reading.viewmodel.ReadingUiState
import com.waveapp.tarotai.presentation.reading.viewmodel.ReadingViewModel

/**
 * Pantalla que muestra la tirada realizada con las cartas seleccionadas.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadingScreen(
    spreadType: SpreadType,
    question: String?,
    consultantName: String?,
    onNavigateBack: () -> Unit,
    onCardClick: (DrawnCard) -> Unit = {},
    onNavigateToHome: () -> Unit,
    viewModel: ReadingViewModel = hiltViewModel()
) {
    val uiState by viewModel.readingUiState.collectAsState()

    // Realizar tirada solo si no hay una tirada ya cargada
    // Esto evita que se regenere la tirada al volver de CardDetail
    LaunchedEffect(Unit) {
        if (uiState is ReadingUiState.Idle) {
            viewModel.performReading(spreadType, question)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.reading_screen_title)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.nav_back)
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
        when (val state = uiState) {
            is ReadingUiState.Loading -> {
                LoadingContent(modifier = Modifier.padding(paddingValues))
            }
            is ReadingUiState.Success -> {
                ReadingContent(
                    reading = state.reading,
                    consultantName = consultantName,
                    onCardClick = onCardClick,
                    modifier = Modifier.padding(paddingValues)
                )
            }
            is ReadingUiState.Error -> {
                ErrorContent(
                    message = state.message,
                    onRetry = { viewModel.performReading(spreadType, question) },
                    modifier = Modifier.padding(paddingValues)
                )
            }
            is ReadingUiState.Idle -> {
                // No mostrar nada en estado Idle
            }
        }
    }
}

@Composable
private fun ReadingContent(
    reading: com.waveapp.tarotai.domain.model.TarotReading,
    consultantName: String?,
    modifier: Modifier = Modifier,
    onCardClick: (DrawnCard) -> Unit = {},
    viewModel: ReadingViewModel = hiltViewModel()
) {
    val interpretationState by viewModel.interpretationUiState.collectAsState()

    Log.d("ReadingScreen", "ReadingContent: Displaying ${reading.drawnCards.size} cards")
    reading.drawnCards.forEachIndexed { index, card ->
        Log.d("ReadingScreen", "Card $index: ${card.card.name}")
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Mostrar consultante y pregunta si existen
        if (consultantName != null || reading.question != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Mostrar consultante si existe
                    consultantName?.let { name ->
                        Text(
                            text = "Consultante:",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = name,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    // Mostrar pregunta si existe
                    reading.question?.let { question ->
                        if (consultantName != null) {
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                        Text(
                            text = "Pregunta:",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = question,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }

        // Mostrar layout según el tipo
        val config = com.waveapp.tarotai.domain.model.SpreadConfiguration.fromType(reading.spreadType)

        when (config.layout) {
            LayoutType.HORIZONTAL -> {
                HorizontalCardsLayout(
                    drawnCards = reading.drawnCards,
                    onCardClick = onCardClick
                )
            }
            LayoutType.CROSS -> {
                CrossCardsLayout(
                    drawnCards = reading.drawnCards,
                    onCardClick = onCardClick
                )
            }
        }

        // Sección de Interpretación
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.primary
        )

        when (val state = interpretationState) {
            is InterpretationUiState.Loading -> {
                InterpretationLoadingView()
            }
            is InterpretationUiState.Success -> {
                InterpretationSuccessView(
                    reading = reading,
                    interpretation = state.interpretation,
                    consultantName = consultantName,
                    onCardClick = onCardClick
                )
            }
            is InterpretationUiState.Error -> {
                InterpretationErrorView(
                    errorMessage = state.message,
                    onRetry = { viewModel.retryInterpretation(reading) }
                )
            }
            is InterpretationUiState.Idle -> {
                // No mostrar nada si no se ha iniciado la interpretación
            }
        }
    }
}

@Composable
private fun LoadingContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error
            )
            Button(onClick = onRetry) {
                Text(stringResource(R.string.retry))
            }
        }
    }
}

/**
 * Vista de loading mientras se genera la interpretación con IA.
 */
@Composable
private fun InterpretationLoadingView() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Interpretando tu tirada...",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Text(
                text = "Esto puede tomar unos segundos",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

/**
 * Vista de la interpretación exitosa.
 */
@Composable
private fun InterpretationSuccessView(
    reading: com.waveapp.tarotai.domain.model.TarotReading,
    interpretation: com.waveapp.tarotai.domain.model.Interpretation,
    consultantName: String?,
    onCardClick: (DrawnCard) -> Unit,
    viewModel: ReadingViewModel = hiltViewModel()
) {
    val saveState by viewModel.saveState.collectAsState()

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Si es una tirada Sí o No, mostrar la respuesta primero (sin justificación)
        if (interpretation.yesNoAnswer != null) {
            YesNoAnswerCard(
                answer = interpretation.yesNoAnswer,
                justification = null // v1.2.0: No mostrar justificación (redundante)
            )
        }

        // Botón de guardar en historial (v1.2.0) - ANTES del mensaje
        // Solo mostrar si hay consultante
        consultantName?.let { name ->
            when (saveState) {
                is com.waveapp.tarotai.presentation.reading.viewmodel.SaveState.NotSaved -> {
                    Button(
                        onClick = {
                            viewModel.saveToHistory(reading, interpretation, name)
                        },
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
                    val error = saveState as com.waveapp.tarotai.presentation.reading.viewmodel.SaveState.Error
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
                                text = "Error al guardar: ${error.message}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            TextButton(onClick = {
                                viewModel.saveToHistory(reading, interpretation, name)
                            }) {
                                Text("Reintentar")
                            }
                        }
                    }
                }
            }
        }

        // Interpretación general - DESPUÉS del botón de guardar
        GeneralInterpretationCard(
            generalInterpretation = interpretation.generalInterpretation
        )
    }
}

/**
 * Vista de error en la interpretación.
 */
@Composable
private fun InterpretationErrorView(
    errorMessage: String,
    onRetry: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Error al generar interpretación",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer,
                textAlign = TextAlign.Center
            )
            Button(onClick = onRetry) {
                Text("Reintentar")
            }
        }
    }
}

