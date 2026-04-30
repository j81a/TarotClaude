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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.waveapp.tarotai.R
import com.waveapp.tarotai.domain.model.CardOrientation
import com.waveapp.tarotai.domain.model.DrawnCard
import com.waveapp.tarotai.domain.model.LayoutType
import com.waveapp.tarotai.domain.model.SpreadType
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
    onNavigateBack: () -> Unit,
    onCardClick: (DrawnCard) -> Unit = {},
    viewModel: ReadingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Realizar tirada cuando se carga la pantalla (solo una vez usando Unit como key)
    LaunchedEffect(Unit) {
        viewModel.performReading(spreadType, question)
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
    onCardClick: (DrawnCard) -> Unit = {},
    modifier: Modifier = Modifier
) {
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
        // Mostrar pregunta si existe
        reading.question?.let { question ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Tu pregunta:",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = question,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
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
    }
}

/**
 * Layout horizontal para cartas (tiradas Simple, Yes/No, Present, Tendency).
 * Si hay más de 1 carta, se muestran en fila horizontal ajustándose al ancho de pantalla.
 */
@Composable
private fun HorizontalCardsLayout(
    drawnCards: List<DrawnCard>,
    onCardClick: (DrawnCard) -> Unit = {}
) {
    if (drawnCards.size == 1) {
        // Una sola carta: centrada verticalmente, ancho fijo
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DrawnCardItem(
                drawnCard = drawnCards[0],
                onCardClick = onCardClick,
                modifier = Modifier.width(220.dp)
            )
        }
    } else {
        // Múltiples cartas: en fila horizontal, cada una ocupa el mismo espacio
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            drawnCards.forEach { drawnCard ->
                DrawnCardItem(
                    drawnCard = drawnCard,
                    onCardClick = onCardClick,
                    modifier = Modifier.weight(1f) // Cada carta ocupa el mismo peso
                )
            }
        }
    }
}

/**
 * Layout en cruz para tirada Cross.
 * Posiciones: 0-Izquierda, 1-Derecha, 2-Arriba, 3-Abajo, 4-Centro
 * Todas las cartas tienen el mismo tamaño.
 */
@Composable
private fun CrossCardsLayout(
    drawnCards: List<DrawnCard>,
    onCardClick: (DrawnCard) -> Unit = {}
) {
    val cardWidth = 110.dp // Mismo ancho para todas las cartas (más pequeño para que quepan 3)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Arriba (posición 2)
        if (drawnCards.size > 2) {
            DrawnCardItem(
                drawnCard = drawnCards[2],
                onCardClick = onCardClick,
                modifier = Modifier
                    .width(cardWidth)
                    .wrapContentHeight()
            )
        }

        // Fila central: Izquierda, Centro, Derecha
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Izquierda (posición 0)
            if (drawnCards.isNotEmpty()) {
                DrawnCardItem(
                    drawnCard = drawnCards[0],
                    onCardClick = onCardClick,
                    modifier = Modifier
                        .width(cardWidth)
                        .wrapContentHeight()
                )
            }

            // Centro (posición 4)
            if (drawnCards.size > 4) {
                DrawnCardItem(
                    drawnCard = drawnCards[4],
                    onCardClick = onCardClick,
                    modifier = Modifier
                        .width(cardWidth)
                        .wrapContentHeight()
                )
            }

            // Derecha (posición 1)
            if (drawnCards.size > 1) {
                DrawnCardItem(
                    drawnCard = drawnCards[1],
                    onCardClick = onCardClick,
                    modifier = Modifier
                        .width(cardWidth)
                        .wrapContentHeight()
                )
            }
        }

        // Abajo (posición 3)
        if (drawnCards.size > 3) {
            DrawnCardItem(
                drawnCard = drawnCards[3],
                onCardClick = onCardClick,
                modifier = Modifier
                    .width(cardWidth)
                    .wrapContentHeight()
            )
        }
    }
}

/**
 * Item individual de carta en la tirada.
 * Todas las cartas tienen la misma estructura y tamaño.
 */
@Composable
private fun DrawnCardItem(
    drawnCard: DrawnCard,
    onCardClick: (DrawnCard) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable { onCardClick(drawnCard) }
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Nombre de la posición - Altura fija para que todas ocupen lo mismo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .padding(horizontal = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = drawnCard.positionName,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 2,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    fontSize = androidx.compose.ui.unit.TextUnit(10f, androidx.compose.ui.unit.TextUnitType.Sp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Imagen de la carta - Altura fija
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                Image(
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
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Nombre de la carta - Altura fija para que todas ocupen lo mismo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .padding(horizontal = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = drawnCard.card.name,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        fontSize = androidx.compose.ui.unit.TextUnit(11f, androidx.compose.ui.unit.TextUnitType.Sp)
                    )

                    // Orientación
                    Text(
                        text = if (drawnCard.orientation == CardOrientation.UPRIGHT) {
                            stringResource(R.string.orientation_upright)
                        } else {
                            stringResource(R.string.orientation_reversed)
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = androidx.compose.ui.unit.TextUnit(9f, androidx.compose.ui.unit.TextUnitType.Sp)
                    )
                }
            }
        }
    }
}

/**
 * Helper para obtener ID de drawable desde nombre.
 */
@Composable
private fun getDrawableResourceId(imagePath: String): Int {
    val context = androidx.compose.ui.platform.LocalContext.current
    return context.resources.getIdentifier(
        imagePath,
        "drawable",
        context.packageName
    ).takeIf { it != 0 } ?: R.drawable.ic_launcher_foreground
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
