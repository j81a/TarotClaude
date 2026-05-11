package com.waveapp.tarotai.presentation.manualload

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waveapp.tarotai.domain.model.CardOrientation
import com.waveapp.tarotai.domain.model.ManualLoadConfiguration
import com.waveapp.tarotai.domain.model.ManualLoadState
import com.waveapp.tarotai.domain.model.SpreadConfiguration
import com.waveapp.tarotai.domain.model.SpreadType
import com.waveapp.tarotai.domain.model.TarotCard
import com.waveapp.tarotai.domain.model.ReadingHistory
import com.waveapp.tarotai.domain.usecase.history.SaveReadingUseCase
import com.waveapp.tarotai.domain.usecase.manualload.AddCardToManualLoadUseCase
import com.waveapp.tarotai.domain.usecase.manualload.GenerateInterpretationFromManualLoadUseCase
import com.waveapp.tarotai.domain.usecase.manualload.RemoveCardFromManualLoadUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para la pantalla de carga manual de tirada.
 *
 * Responsabilidades:
 * - Mantener el estado de la configuración de carga manual
 * - Gestionar la selección y remoción de cartas
 * - Validar la configuración antes de generar interpretación
 * - Coordinar la navegación al selector de cartas
 *
 * Estados:
 * - configuration: Configuración actual (spreadType, pregunta, consultante, estado)
 * - isLoading: Indicador de carga durante generación de interpretación
 * - error: Mensaje de error si algo falla
 * - interpretationGenerated: Flag para navegar a pantalla de interpretación
 *
 * @param savedStateHandle Para obtener parámetros de navegación
 * @param addCardToManualLoadUseCase Agregar carta a la configuración
 * @param removeCardFromManualLoadUseCase Remover carta de la configuración
 * @param generateInterpretationFromManualLoadUseCase Generar interpretación final
 * @param saveReadingUseCase Guardar la lectura en el historial
 *
 * @since v1.1.0
 */
@HiltViewModel
class ManualLoadViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val addCardToManualLoadUseCase: AddCardToManualLoadUseCase,
    private val removeCardFromManualLoadUseCase: RemoveCardFromManualLoadUseCase,
    private val generateInterpretationFromManualLoadUseCase: GenerateInterpretationFromManualLoadUseCase,
    private val saveReadingUseCase: SaveReadingUseCase
) : ViewModel() {

    // Obtener parámetros de navegación
    private val spreadTypeStr: String = savedStateHandle.get<String>("spreadType") ?: "SIMPLE"
    private val question: String? = savedStateHandle.get<String>("question")
    // v1.2.0: Usar valor por defecto "Lectura personal" si consultantName es null o vacío
    private val consultantName: String = savedStateHandle.get<String>("consultantName")?.takeIf { it.isNotBlank() } ?: "Lectura personal"

    private val spreadType = try {
        SpreadType.valueOf(spreadTypeStr)
    } catch (e: Exception) {
        SpreadType.SIMPLE
    }

    // Estado de la configuración
    private val _configuration = MutableStateFlow(
        ManualLoadConfiguration(
            spreadType = spreadType,
            question = question,
            consultantName = consultantName,
            state = ManualLoadState()
        )
    )
    val configuration: StateFlow<ManualLoadConfiguration> = _configuration.asStateFlow()

    // Estado de carga
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Estado de error
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Flag para navegación (se setea cuando interpretación se genera exitosamente)
    private val _interpretationGenerated = MutableStateFlow<com.waveapp.tarotai.domain.model.Interpretation?>(null)
    val interpretationGenerated: StateFlow<com.waveapp.tarotai.domain.model.Interpretation?> = _interpretationGenerated.asStateFlow()

    // ID de la lectura guardada (para navegar al detalle)
    private val _savedReadingId = MutableStateFlow<Long?>(null)
    val savedReadingId: StateFlow<Long?> = _savedReadingId.asStateFlow()

    // v1.2.0: Estado de guardado manual (igual que ReadingViewModel)
    private val _saveState = MutableStateFlow<com.waveapp.tarotai.presentation.reading.viewmodel.SaveState>(
        com.waveapp.tarotai.presentation.reading.viewmodel.SaveState.NotSaved
    )
    val saveState: StateFlow<com.waveapp.tarotai.presentation.reading.viewmodel.SaveState> = _saveState.asStateFlow()

    /**
     * Agrega una carta a una posición específica.
     *
     * @param card Carta seleccionada
     * @param positionIndex Índice de la posición (0-based)
     * @param orientation Orientación de la carta física
     */
    fun addCard(card: TarotCard, positionIndex: Int, orientation: CardOrientation) {
        val spreadConfig = SpreadConfiguration.fromType(spreadType)
        val positionName = spreadConfig.positions[positionIndex]
        val currentState = _configuration.value.state

        val result = addCardToManualLoadUseCase(
            currentState = currentState,
            card = card,
            positionIndex = positionIndex,
            positionName = positionName,
            orientation = orientation
        )

        result.fold(
            onSuccess = { newState ->
                _configuration.value = _configuration.value.copy(state = newState)
                _error.value = null
            },
            onFailure = { exception ->
                _error.value = exception.message ?: "Error al agregar la carta"
            }
        )
    }

    /**
     * Remueve la carta de una posición específica.
     *
     * @param positionIndex Índice de la posición (0-based)
     */
    fun removeCard(positionIndex: Int) {
        val currentState = _configuration.value.state

        val result = removeCardFromManualLoadUseCase(
            currentState = currentState,
            positionIndex = positionIndex
        )

        result.fold(
            onSuccess = { newState ->
                _configuration.value = _configuration.value.copy(state = newState)
                _error.value = null
            },
            onFailure = { exception ->
                _error.value = exception.message ?: "Error al remover la carta"
            }
        )
    }

    /**
     * Genera la interpretación de la tirada manual (v1.2.0: NO guarda automáticamente).
     * Solo se ejecuta si la configuración está completa.
     */
    fun generateInterpretation() {
        if (!_configuration.value.isComplete()) {
            _error.value = "Debes seleccionar todas las cartas antes de generar la interpretación"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val interpretationResult = generateInterpretationFromManualLoadUseCase(_configuration.value)

            interpretationResult.fold(
                onSuccess = { interpretation ->
                    _interpretationGenerated.value = interpretation
                    _isLoading.value = false
                    // v1.2.0: NO guardar automáticamente
                },
                onFailure = { exception ->
                    _error.value = exception.message ?: "Error al generar la interpretación"
                    _isLoading.value = false
                }
            )
        }
    }

    /**
     * v1.2.0: Guarda la lectura en el historial manualmente (controlado por el usuario).
     * Similar a ReadingViewModel.saveToHistory()
     */
    fun saveToHistory() {
        val interpretation = _interpretationGenerated.value
        if (interpretation == null) {
            _error.value = "No hay interpretación generada para guardar"
            return
        }

        viewModelScope.launch {
            _saveState.value = com.waveapp.tarotai.presentation.reading.viewmodel.SaveState.Saving

            val reading = ReadingHistory(
                id = 0, // Se genera automáticamente
                timestamp = System.currentTimeMillis(),
                consultantName = _configuration.value.consultantName.ifBlank { "Lectura personal" },
                spreadType = _configuration.value.spreadType,
                question = _configuration.value.question,
                drawnCards = _configuration.value.state.toDrawnCards(),
                interpretation = interpretation,
                notes = emptyList()
            )

            val saveResult = saveReadingUseCase(reading)

            saveResult.fold(
                onSuccess = { readingId ->
                    _saveState.value = com.waveapp.tarotai.presentation.reading.viewmodel.SaveState.Saved(readingId)
                    _savedReadingId.value = readingId // Para navegación
                },
                onFailure = { exception ->
                    _saveState.value = com.waveapp.tarotai.presentation.reading.viewmodel.SaveState.Error(
                        exception.message ?: "Error al guardar la lectura"
                    )
                }
            )
        }
    }

    /**
     * Limpia el mensaje de error.
     */
    fun clearError() {
        _error.value = null
    }

    /**
     * Resetea el flag de interpretación generada (después de navegar).
     */
    fun clearInterpretationGenerated() {
        _interpretationGenerated.value = null
    }

    /**
     * Resetea el ID de lectura guardada (después de navegar).
     */
    fun clearSavedReadingId() {
        _savedReadingId.value = null
    }
}
