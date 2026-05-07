package com.waveapp.tarotai.presentation.cardselector

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.waveapp.tarotai.domain.model.CardFilter
import com.waveapp.tarotai.domain.model.CardOrientation
import com.waveapp.tarotai.domain.model.ManualLoadState
import com.waveapp.tarotai.domain.model.TarotCard

/**
 * Pantalla de selección de carta para carga manual.
 *
 * Muestra un grid de 3 columnas con todas las cartas disponibles:
 * - Filtros por tipo (Todas, Arcanos Mayores, Bastos, Copas, Espadas, Oros)
 * - Cartas ya usadas con overlay semi-transparente y texto "En uso"
 * - Al hacer click en carta disponible, muestra dialog de orientación
 *
 * Flujo:
 * 1. Usuario selecciona filtro (opcional)
 * 2. Usuario toca carta disponible
 * 3. Dialog pregunta orientación (Derecha/Invertida)
 * 4. Se agrega carta al ViewModel padre y se navega atrás
 *
 * @param positionName Nombre de la posición (para mostrar en header)
 * @param manualLoadState Estado actual de carga manual (para excluir cartas usadas)
 * @param onCardSelected Callback cuando se selecciona carta con orientación
 * @param onNavigateBack Callback para volver atrás
 * @param viewModel ViewModel inyectado por Hilt
 *
 * @since v1.1.0
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardSelectorScreen(
    positionName: String,
    manualLoadState: ManualLoadState,
    onCardSelected: (TarotCard, CardOrientation) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: CardSelectorViewModel = hiltViewModel()
) {
    // Inicializar ViewModel con el estado actual
    LaunchedEffect(manualLoadState) {
        viewModel.initialize(manualLoadState)
    }

    val selectedFilter by viewModel.selectedFilter.collectAsState()
    val availableCards by viewModel.availableCards.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    // Dialog de selección de orientación
    var showOrientationDialog by remember { mutableStateOf(false) }
    var selectedCard by remember { mutableStateOf<TarotCard?>(null) }

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
                title = {
                    Column {
                        Text("Seleccionar Carta")
                        Text(
                            text = positionName,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
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
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Filtros
            FilterChipsRow(
                selectedFilter = selectedFilter,
                onFilterSelected = { viewModel.selectFilter(it) }
            )

            HorizontalDivider()

            // Grid de cartas
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (availableCards.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay cartas disponibles con este filtro",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(availableCards, key = { it.id }) { card ->
                        val isUsed = viewModel.isCardUsed(card.id)
                        CardGridItem(
                            card = card,
                            isUsed = isUsed,
                            onClick = {
                                if (!isUsed) {
                                    selectedCard = card
                                    showOrientationDialog = true
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    // Dialog de orientación
    if (showOrientationDialog && selectedCard != null) {
        OrientationDialog(
            cardName = selectedCard!!.name,
            onOrientationSelected = { orientation ->
                onCardSelected(selectedCard!!, orientation)
                showOrientationDialog = false
                selectedCard = null
            },
            onDismiss = {
                showOrientationDialog = false
                selectedCard = null
            }
        )
    }
}

@Composable
private fun FilterChipsRow(
    selectedFilter: CardFilter,
    onFilterSelected: (CardFilter) -> Unit
) {
    val filters = CardFilter.getAllFilters()
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        filters.forEach { filter ->
            FilterChip(
                selected = selectedFilter == filter,
                onClick = { onFilterSelected(filter) },
                label = {
                    Text(
                        text = "${filter.getDisplayName()} (${filter.getCardCount()})",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            )
        }
    }
}

@Composable
private fun CardGridItem(
    card: TarotCard,
    isUsed: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(0.7f)
            .clickable(onClick = onClick, enabled = !isUsed)
    ) {
        Card(
            modifier = Modifier.fillMaxSize(),
            colors = CardDefaults.cardColors(
                containerColor = if (isUsed)
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                else
                    MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = card.name,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Indicador de arcano
                Text(
                    text = if (card.arcanaType.name == "MAJOR") "Arcano Mayor" else card.suit?.name ?: "",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
        }

        // Overlay "En uso" para cartas ya seleccionadas
        if (isUsed) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "En uso",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun OrientationDialog(
    cardName: String,
    onOrientationSelected: (CardOrientation) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Orientación de la carta",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column {
                Text(
                    text = cardName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "¿Cómo salió esta carta en tu tirada física?",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onOrientationSelected(CardOrientation.UPRIGHT) }) {
                Text("Derecha")
            }
        },
        dismissButton = {
            TextButton(onClick = { onOrientationSelected(CardOrientation.REVERSED) }) {
                Text("Invertida")
            }
        }
    )
}
