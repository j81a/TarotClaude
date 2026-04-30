package com.waveapp.tarotai.presentation.carddetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.waveapp.tarotai.R
import com.waveapp.tarotai.domain.model.ArcanaType
import com.waveapp.tarotai.domain.model.Suit
import com.waveapp.tarotai.domain.model.TarotCard
import com.waveapp.tarotai.presentation.carddetail.viewmodel.CardDetailUiState
import com.waveapp.tarotai.presentation.carddetail.viewmodel.CardDetailViewModel

/**
 * Pantalla de detalle de una carta del tarot.
 *
 * Muestra toda la información de una carta:
 * - Imagen (placeholder por ahora)
 * - Nombre
 * - Tipo de arcano y palo
 * - Significado general
 * - Significado en posición normal
 * - Significado invertido
 * - Simbolismo
 * - Palabras clave
 *
 * @param onNavigateBack Callback para volver atrás
 * @param fromReading Indica si se accede desde una tirada (muestra botón IA)
 * @param onInterpretWithAI Callback cuando se toca "Interpretar con IA" (solo si fromReading=true)
 * @param viewModel ViewModel inyectado por Hilt
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardDetailScreen(
    onNavigateBack: () -> Unit,
    fromReading: Boolean = false,
    onInterpretWithAI: () -> Unit = {},
    viewModel: CardDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    when (val state = uiState) {
                        is CardDetailUiState.Success -> Text(state.card.name)
                        else -> Text(stringResource(R.string.card_detail_title))
                    }
                },
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
            is CardDetailUiState.Loading -> {
                LoadingContent(modifier = Modifier.padding(paddingValues))
            }
            is CardDetailUiState.Success -> {
                CardDetailContent(
                    card = state.card,
                    fromReading = fromReading,
                    onInterpretWithAI = onInterpretWithAI,
                    modifier = Modifier.padding(paddingValues)
                )
            }
            is CardDetailUiState.Error -> {
                ErrorContent(
                    message = state.message,
                    onRetry = { viewModel.retry() },
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

/**
 * Contenido principal del detalle de carta.
 */
@Composable
private fun CardDetailContent(
    card: TarotCard,
    fromReading: Boolean = false,
    onInterpretWithAI: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Imagen de la carta
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Image(
                painter = painterResource(
                    id = getDrawableResourceId(card.imagePath)
                ),
                contentDescription = card.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }

        // Nombre y badges
        Text(
            text = card.name,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ArcanaTypeBadge(arcanaType = card.arcanaType)

            card.suit?.let { suit ->
                Spacer(modifier = Modifier.width(8.dp))
                SuitBadge(suit = suit)
            }
        }

        HorizontalDivider()

        // Palabras clave
        if (card.keywords.isNotEmpty()) {
            SectionCard(title = stringResource(R.string.card_keywords_title)) {
                Text(
                    text = card.keywords.joinToString(", "),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Significado general
        SectionCard(title = stringResource(R.string.card_general_meaning_title)) {
            Text(
                text = card.generalMeaning,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // Significado en posición normal
        SectionCard(title = stringResource(R.string.card_upright_meaning_title)) {
            Text(
                text = card.uprightMeaning,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // Significado invertido
        SectionCard(title = stringResource(R.string.card_reversed_meaning_title)) {
            Text(
                text = card.reversedMeaning,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // Simbolismo
        SectionCard(title = stringResource(R.string.card_symbolism_title)) {
            Text(
                text = card.symbolism,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // Botón "Interpretar con IA" solo si viene desde una tirada
        if (fromReading) {
            Button(
                onClick = onInterpretWithAI,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(stringResource(R.string.interpret_with_ai_button))
            }
        }
    }
}

/**
 * Placeholder para la imagen de la carta.
 */
@Composable
private fun CardImagePlaceholder(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = MaterialTheme.shapes.medium
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.card_image_placeholder),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

/**
 * Tarjeta para una sección de contenido.
 */
@Composable
private fun SectionCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            content()
        }
    }
}

/**
 * Badge para tipo de arcano.
 */
@Composable
private fun ArcanaTypeBadge(arcanaType: ArcanaType) {
    val text = when (arcanaType) {
        ArcanaType.MAJOR -> stringResource(R.string.arcana_type_major)
        ArcanaType.MINOR -> stringResource(R.string.arcana_type_minor)
    }

    val color = when (arcanaType) {
        ArcanaType.MAJOR -> MaterialTheme.colorScheme.primaryContainer
        ArcanaType.MINOR -> MaterialTheme.colorScheme.secondaryContainer
    }

    Surface(
        color = color,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

/**
 * Badge para palo.
 */
@Composable
private fun SuitBadge(suit: Suit) {
    val text = when (suit) {
        Suit.WANDS -> stringResource(R.string.suit_wands)
        Suit.CUPS -> stringResource(R.string.suit_cups)
        Suit.SWORDS -> stringResource(R.string.suit_swords)
        Suit.PENTACLES -> stringResource(R.string.suit_pentacles)
    }

    Surface(
        color = MaterialTheme.colorScheme.tertiaryContainer,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

/**
 * Estado de carga.
 */
@Composable
private fun LoadingContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

/**
 * Estado de error.
 */
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
 * Función helper para obtener el ID de recurso drawable desde el nombre.
 * Convierte "card_major_00" al ID R.drawable.card_major_00
 */
@Composable
private fun getDrawableResourceId(imagePath: String): Int {
    val context = androidx.compose.ui.platform.LocalContext.current
    return context.resources.getIdentifier(
        imagePath,
        "drawable",
        context.packageName
    ).takeIf { it != 0 } ?: R.drawable.ic_launcher_foreground // Fallback si no existe
}

