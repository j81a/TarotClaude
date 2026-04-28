package com.waveapp.tarotai.presentation.encyclopedia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waveapp.tarotai.domain.model.ArcanaType
import com.waveapp.tarotai.domain.model.Suit
import com.waveapp.tarotai.domain.model.TarotCard
import com.waveapp.tarotai.domain.repository.TarotCardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para la pantalla de Enciclopedia.
 *
 * @HiltViewModel: Indica a Hilt que debe crear este ViewModel
 * @param repository: Repository inyectado por Hilt
 *
 * Responsabilidades:
 * - Cargar la lista de cartas
 * - Gestionar filtros (arcana type, suit)
 * - Gestionar búsqueda
 * - Exponer el estado de UI
 */
@HiltViewModel
class EncyclopediaViewModel @Inject constructor(
    private val repository: TarotCardRepository
) : ViewModel() {

    /**
     * Estado de la pantalla de enciclopedia.
     */
    private val _uiState = MutableStateFlow<EncyclopediaUiState>(EncyclopediaUiState.Loading)
    val uiState: StateFlow<EncyclopediaUiState> = _uiState.asStateFlow()

    /**
     * Filtro actual de tipo de arcano (null = todos).
     */
    private val _arcanaFilter = MutableStateFlow<ArcanaType?>(null)
    val arcanaFilter: StateFlow<ArcanaType?> = _arcanaFilter.asStateFlow()

    /**
     * Filtro actual de palo (null = todos).
     */
    private val _suitFilter = MutableStateFlow<Suit?>(null)
    val suitFilter: StateFlow<Suit?> = _suitFilter.asStateFlow()

    /**
     * Query de búsqueda actual.
     */
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        loadCards()
    }

    /**
     * Carga las cartas aplicando filtros y búsqueda.
     */
    fun loadCards() {
        viewModelScope.launch {
            _uiState.value = EncyclopediaUiState.Loading

            try {
                // Determinar qué flujo usar según filtros
                val cardsFlow = when {
                    // Si hay búsqueda, usar searchCards
                    _searchQuery.value.isNotBlank() -> {
                        repository.searchCards(_searchQuery.value)
                    }
                    // Si hay filtro de palo, usarlo
                    _suitFilter.value != null -> {
                        repository.getCardsBySuit(_suitFilter.value!!)
                    }
                    // Si hay filtro de arcana type, usarlo
                    _arcanaFilter.value != null -> {
                        repository.getCardsByArcanaType(_arcanaFilter.value!!)
                    }
                    // Sin filtros, obtener todas
                    else -> {
                        repository.getAllCards()
                    }
                }

                // Recolectar el flujo
                cardsFlow.catch { exception ->
                    _uiState.value = EncyclopediaUiState.Error(
                        exception.message ?: "Error desconocido"
                    )
                }.collect { cards ->
                    if (cards.isEmpty()) {
                        _uiState.value = EncyclopediaUiState.Empty
                    } else {
                        _uiState.value = EncyclopediaUiState.Success(cards)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = EncyclopediaUiState.Error(
                    e.message ?: "Error al cargar las cartas"
                )
            }
        }
    }

    /**
     * Actualiza el query de búsqueda y recarga.
     */
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        loadCards()
    }

    /**
     * Actualiza el filtro de tipo de arcano y recarga.
     */
    fun onArcanaFilterChanged(arcanaType: ArcanaType?) {
        _arcanaFilter.value = arcanaType
        // Si filtramos por arcano mayor, quitar filtro de palo
        if (arcanaType == ArcanaType.MAJOR) {
            _suitFilter.value = null
        }
        loadCards()
    }

    /**
     * Actualiza el filtro de palo y recarga.
     */
    fun onSuitFilterChanged(suit: Suit?) {
        _suitFilter.value = suit
        // Si filtramos por palo, automáticamente filtramos por arcanos menores
        if (suit != null) {
            _arcanaFilter.value = ArcanaType.MINOR
        }
        loadCards()
    }

    /**
     * Limpia todos los filtros.
     */
    fun clearFilters() {
        _arcanaFilter.value = null
        _suitFilter.value = null
        _searchQuery.value = ""
        loadCards()
    }
}

/**
 * Estados posibles de la pantalla de Enciclopedia.
 */
sealed class EncyclopediaUiState {
    /**
     * Estado inicial: cargando datos.
     */
    data object Loading : EncyclopediaUiState()

    /**
     * Éxito: cartas cargadas.
     */
    data class Success(val cards: List<TarotCard>) : EncyclopediaUiState()

    /**
     * No hay resultados (por filtros o búsqueda).
     */
    data object Empty : EncyclopediaUiState()

    /**
     * Error al cargar.
     */
    data class Error(val message: String) : EncyclopediaUiState()
}
