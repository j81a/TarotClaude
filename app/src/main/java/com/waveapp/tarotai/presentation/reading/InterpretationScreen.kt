package com.waveapp.tarotai.presentation.reading

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.waveapp.tarotai.domain.model.Interpretation
import com.waveapp.tarotai.domain.model.TarotReading
import com.waveapp.tarotai.domain.model.YesNoAnswer
import com.waveapp.tarotai.presentation.reading.components.CardInterpretationCard
import com.waveapp.tarotai.presentation.reading.components.GeneralInterpretationCard
import com.waveapp.tarotai.presentation.reading.components.YesNoAnswerCard
import com.waveapp.tarotai.presentation.reading.viewmodel.InterpretationUiState
import com.waveapp.tarotai.presentation.reading.viewmodel.ReadingViewModel

/**
 * Pantalla de interpretación de la tirada.
 *
 * Muestra:
 * - Interpretaciones individuales de cada carta
 * - Interpretación general de toda la tirada
 * - Para tiradas YES_NO: Respuesta Sí/No con justificación educativa
 *
 * Estados:
 * - Idle: No se ha solicitado interpretación
 * - Loading: Generando interpretación con IA
 * - Success: Interpretación generada y mostrada
 * - Error: Error al generar interpretación (con botón reintentar)
 *
 * @param reading Tirada de tarot a interpretar
 * @param onNavigateBack Callback para volver atrás
 * @param onCardClick Callback al hacer click en una carta (navega a detalle)
 * @param onNewReading Callback para iniciar una nueva tirada
 * @param viewModel ViewModel que maneja el estado
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InterpretationScreen(
    reading: TarotReading,
    onNavigateBack: () -> Unit,
    onCardClick: (Int) -> Unit,
    onNewReading: () -> Unit,
    viewModel: ReadingViewModel = hiltViewModel()
) {
    val interpretationState by viewModel.interpretationUiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Interpretación") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        when (val state = interpretationState) {
            is InterpretationUiState.Idle -> {
                // Estado inicial - no debería verse, pero por seguridad mostramos un mensaje
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Iniciando interpretación...")
                }
            }

            is InterpretationUiState.Loading -> {
                LoadingInterpretation(modifier = Modifier.padding(paddingValues))
            }

            is InterpretationUiState.Success -> {
                InterpretationContent(
                    reading = reading,
                    interpretation = state.interpretation,
                    onCardClick = onCardClick,
                    onNewReading = onNewReading,
                    modifier = Modifier.padding(paddingValues)
                )
            }

            is InterpretationUiState.Error -> {
                ErrorInterpretation(
                    errorMessage = state.message,
                    onRetry = { viewModel.retryInterpretation(reading) },
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

/**
 * Composable que muestra el indicador de carga mientras se genera la interpretación.
 */
@Composable
private fun LoadingInterpretation(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(64.dp)
            )
            Text(
                text = "Generando interpretación...",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Esto puede tomar unos segundos",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Composable que muestra el contenido de la interpretación exitosa.
 */
@Composable
private fun InterpretationContent(
    reading: TarotReading,
    interpretation: Interpretation,
    onCardClick: (Int) -> Unit,
    onNewReading: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Título de la sección
        Text(
            text = "Tu Tirada",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        // Si es una tirada Sí o No, mostrar la respuesta primero (destacada)
        if (interpretation.yesNoAnswer != null && interpretation.yesNoJustification != null) {
            YesNoAnswerCard(
                answer = interpretation.yesNoAnswer,
                justification = interpretation.yesNoJustification
            )
        }

        // Interpretaciones individuales de cada carta
        Text(
            text = "Interpretación de cada carta",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )

        interpretation.individualInterpretations.forEach { cardInterpretation ->
            // Buscar la carta correspondiente en la tirada para obtener su ID
            val drawnCard = reading.drawnCards.find {
                it.card.name == cardInterpretation.cardName
            }

            CardInterpretationCard(
                cardInterpretation = cardInterpretation,
                onClick = { drawnCard?.let { onCardClick(it.card.id) } }
            )
        }

        // Interpretación general
        Text(
            text = "Interpretación general",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )

        GeneralInterpretationCard(
            generalInterpretation = interpretation.generalInterpretation
        )

        // Botón para nueva tirada
        Button(
            onClick = onNewReading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Nueva Tirada")
        }

        // Espaciado final
        Spacer(modifier = Modifier.height(16.dp))
    }
}

/**
 * Composable que muestra el estado de error con opción de reintentar.
 */
@Composable
private fun ErrorInterpretation(
    errorMessage: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.error
            )

            Text(
                text = "Error al generar interpretación",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )

            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Button(
                onClick = onRetry,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Reintentar")
            }
        }
    }
}
