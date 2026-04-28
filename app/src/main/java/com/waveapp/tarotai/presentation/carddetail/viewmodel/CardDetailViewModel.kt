package com.waveapp.tarotai.presentation.carddetail.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
 * ViewModel para la pantalla de detalle de carta.
 *
 * @HiltViewModel: Indica a Hilt que debe crear este ViewModel
 * @param savedStateHandle: Para recuperar argumentos de navegación
 * @param repository: Repository inyectado por Hilt
 *
 * Responsabilidades:
 * - Cargar los detalles de una carta específica
 * - Gestionar el estado de la UI (loading, success, error)
 */
@HiltViewModel
class CardDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: TarotCardRepository
) : ViewModel() {

    /**
     * ID de la carta a mostrar (extraído de los argumentos de navegación).
     */
    private val cardId: Int = checkNotNull(savedStateHandle["cardId"])

    /**
     * Estado de la pantalla de detalle.
     */
    private val _uiState = MutableStateFlow<CardDetailUiState>(CardDetailUiState.Loading)
    val uiState: StateFlow<CardDetailUiState> = _uiState.asStateFlow()

    init {
        loadCardDetails()
    }

    /**
     * Carga los detalles de la carta.
     */
    private fun loadCardDetails() {
        viewModelScope.launch {
            _uiState.value = CardDetailUiState.Loading

            repository.getCardById(cardId)
                .catch { exception ->
                    _uiState.value = CardDetailUiState.Error(
                        exception.message ?: "Error al cargar la carta"
                    )
                }
                .collect { card ->
                    if (card != null) {
                        _uiState.value = CardDetailUiState.Success(card)
                    } else {
                        _uiState.value = CardDetailUiState.Error("Carta no encontrada")
                    }
                }
        }
    }

    /**
     * Reintentar la carga de la carta.
     */
    fun retry() {
        loadCardDetails()
    }
}

/**
 * Estados posibles de la pantalla de detalle.
 */
sealed class CardDetailUiState {
    /**
     * Estado inicial: cargando datos.
     */
    data object Loading : CardDetailUiState()

    /**
     * Éxito: carta cargada.
     */
    data class Success(val card: TarotCard) : CardDetailUiState()

    /**
     * Error al cargar.
     */
    data class Error(val message: String) : CardDetailUiState()
}
