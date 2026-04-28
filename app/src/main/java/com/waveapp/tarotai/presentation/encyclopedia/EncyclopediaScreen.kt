package com.waveapp.tarotai.presentation.encyclopedia

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.waveapp.tarotai.R
import com.waveapp.tarotai.domain.model.ArcanaType
import com.waveapp.tarotai.domain.model.Suit
import com.waveapp.tarotai.presentation.encyclopedia.components.FilterChipsRow
import com.waveapp.tarotai.presentation.encyclopedia.components.TarotCardListItem
import com.waveapp.tarotai.presentation.encyclopedia.viewmodel.EncyclopediaUiState
import com.waveapp.tarotai.presentation.encyclopedia.viewmodel.EncyclopediaViewModel

/**
 * Pantalla de Enciclopedia - Muestra la lista de cartas del tarot.
 *
 * Composable principal que gestiona:
 * - Barra de búsqueda
 * - Filtros por tipo de arcano y palo
 * - Lista de cartas
 * - Estados de loading, error, y vacío
 *
 * @param onCardClick Callback cuando se hace clic en una carta
 * @param viewModel ViewModel inyectado por Hilt
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EncyclopediaScreen(
    onCardClick: (Int) -> Unit,
    viewModel: EncyclopediaViewModel = hiltViewModel()
) {
    // Recoger estados del ViewModel
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val arcanaFilter by viewModel.arcanaFilter.collectAsState()
    val suitFilter by viewModel.suitFilter.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.encyclopedia_title)) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Barra de búsqueda
            SearchBar(
                query = searchQuery,
                onQueryChange = { viewModel.onSearchQueryChanged(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Filtros
            FilterChipsRow(
                selectedArcana = arcanaFilter,
                selectedSuit = suitFilter,
                onArcanaSelected = { viewModel.onArcanaFilterChanged(it) },
                onSuitSelected = { viewModel.onSuitFilterChanged(it) },
                onClearFilters = { viewModel.clearFilters() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Contenido según estado
            when (val state = uiState) {
                is EncyclopediaUiState.Loading -> {
                    LoadingContent()
                }
                is EncyclopediaUiState.Success -> {
                    CardsList(
                        cards = state.cards,
                        onCardClick = onCardClick
                    )
                }
                is EncyclopediaUiState.Empty -> {
                    EmptyContent()
                }
                is EncyclopediaUiState.Error -> {
                    ErrorContent(
                        message = state.message,
                        onRetry = { viewModel.loadCards() }
                    )
                }
            }
        }
    }
}

/**
 * Barra de búsqueda.
 */
@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text(stringResource(R.string.encyclopedia_search_hint)) },
        modifier = modifier,
        singleLine = true
    )
}

/**
 * Lista de cartas.
 */
@Composable
private fun CardsList(
    cards: List<com.waveapp.tarotai.domain.model.TarotCard>,
    onCardClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = cards,
            key = { it.id }
        ) { card ->
            TarotCardListItem(
                card = card,
                onClick = { onCardClick(card.id) }
            )
        }
    }
}

/**
 * Estado de carga.
 */
@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

/**
 * Estado vacío (sin resultados).
 */
@Composable
private fun EmptyContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.encyclopedia_empty_state),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

/**
 * Estado de error.
 */
@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
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
