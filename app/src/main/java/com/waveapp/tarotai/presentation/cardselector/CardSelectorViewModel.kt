package com.waveapp.tarotai.presentation.cardselector

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waveapp.tarotai.domain.model.CardFilter
import com.waveapp.tarotai.domain.model.ManualLoadState
import com.waveapp.tarotai.domain.model.TarotCard
import com.waveapp.tarotai.domain.usecase.manualload.GetAvailableCardsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para la pantalla de selección de carta.
 *
 * Responsabilidades:
 * - Cargar cartas disponibles según el estado actual
 * - Aplicar filtros (todas, arcanos mayores, por palo)
 * - Marcar cartas ya usadas con overlay
 * - Gestionar selección de carta y orientación
 *
 * Estados:
 * - availableCards: Lista de cartas disponibles (filtradas y ordenadas)
 * - selectedFilter: Filtro actualmente aplicado
 * - isLoading: Indicador de carga
 * - error: Mensaje de error
 *
 * NOTA: El ManualLoadState se obtiene del ViewModel compartido (no implementado aún).
 * Por ahora, se asume que el ViewModel padre mantiene el estado y se pasa vía navigation.
 *
 * @param savedStateHandle Para obtener positionIndex de navegación
 * @param getAvailableCardsUseCase Para obtener cartas disponibles con filtros
 *
 * @since v1.1.0
 */
@HiltViewModel
class CardSelectorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getAvailableCardsUseCase: GetAvailableCardsUseCase
) : ViewModel() {

    // Obtener positionIndex de navegación
    val positionIndex: Int = savedStateHandle.get<Int>("positionIndex") ?: 0

    // Estado del filtro seleccionado
    private val _selectedFilter = MutableStateFlow(CardFilter.ALL)
    val selectedFilter: StateFlow<CardFilter> = _selectedFilter.asStateFlow()

    // Lista de cartas disponibles (después de aplicar filtro)
    private val _availableCards = MutableStateFlow<List<TarotCard>>(emptyList())
    val availableCards: StateFlow<List<TarotCard>> = _availableCards.asStateFlow()

    // Estado de carga
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Estado de error
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Estado manual actual (se debe pasar desde el ViewModel padre)
    // TODO: Implementar mecanismo de compartir estado entre ViewModels
    // Opciones: NavBackStackEntry SavedStateHandle, Hilt AssistedInject, o estado compartido
    private var currentManualLoadState: ManualLoadState = ManualLoadState()

    /**
     * Inicializa el ViewModel cargando las cartas disponibles.
     * Debe llamarse desde init o desde la UI con el estado actual.
     */
    fun initialize(manualLoadState: ManualLoadState) {
        currentManualLoadState = manualLoadState
        loadAvailableCards()
    }

    /**
     * Cambia el filtro aplicado y recarga las cartas.
     */
    fun selectFilter(filter: CardFilter) {
        _selectedFilter.value = filter
        loadAvailableCards()
    }

    /**
     * Carga las cartas disponibles según el filtro y estado actual.
     */
    private fun loadAvailableCards() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val result = getAvailableCardsUseCase(
                currentState = currentManualLoadState,
                filter = _selectedFilter.value
            )

            result.fold(
                onSuccess = { cards ->
                    _availableCards.value = cards
                    _isLoading.value = false
                },
                onFailure = { exception ->
                    _error.value = exception.message ?: "Error al cargar las cartas"
                    _isLoading.value = false
                }
            )
        }
    }

    /**
     * Verifica si una carta está ya en uso.
     * @param cardId ID de la carta
     * @return true si la carta ya fue seleccionada
     */
    fun isCardUsed(cardId: Int): Boolean {
        return currentManualLoadState.isCardUsed(cardId)
    }

    /**
     * Limpia el mensaje de error.
     */
    fun clearError() {
        _error.value = null
    }
}
