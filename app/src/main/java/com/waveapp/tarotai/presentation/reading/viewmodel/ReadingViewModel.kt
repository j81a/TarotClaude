package com.waveapp.tarotai.presentation.reading.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waveapp.tarotai.domain.model.ReadingHistory
import com.waveapp.tarotai.domain.model.SpreadType
import com.waveapp.tarotai.domain.model.TarotReading
import com.waveapp.tarotai.domain.usecase.GenerateInterpretationUseCase
import com.waveapp.tarotai.domain.usecase.GetSpreadConfigurationUseCase
import com.waveapp.tarotai.domain.usecase.PerformReadingUseCase
import com.waveapp.tarotai.domain.usecase.history.SaveReadingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para el flujo de tiradas de tarot.
 *
 * Maneja la lógica de:
 * - Selección de tipo de tirada
 * - Pregunta del usuario
 * - Realización de la tirada (selección de cartas)
 * - Generación de interpretación con IA
 *
 * @property performReadingUseCase Caso de uso para realizar tiradas
 * @property getSpreadConfigurationUseCase Caso de uso para obtener configuración de tiradas
 * @property generateInterpretationUseCase Caso de uso para generar interpretaciones con IA
 */
@HiltViewModel
class ReadingViewModel @Inject constructor(
    private val performReadingUseCase: PerformReadingUseCase,
    private val getSpreadConfigurationUseCase: GetSpreadConfigurationUseCase,
    private val generateInterpretationUseCase: GenerateInterpretationUseCase,
    private val saveReadingUseCase: SaveReadingUseCase
) : ViewModel() {

    // Estado de la tirada de cartas
    private val _readingUiState = MutableStateFlow<ReadingUiState>(ReadingUiState.Idle)
    val readingUiState: StateFlow<ReadingUiState> = _readingUiState.asStateFlow()

    // Estado de la interpretación por IA
    private val _interpretationUiState = MutableStateFlow<InterpretationUiState>(InterpretationUiState.Idle)
    val interpretationUiState: StateFlow<InterpretationUiState> = _interpretationUiState.asStateFlow()

    // Estado del guardado en historial (v1.2.0)
    private val _saveState = MutableStateFlow<SaveState>(SaveState.NotSaved)
    val saveState: StateFlow<SaveState> = _saveState.asStateFlow()

    /**
     * Realiza una tirada de tarot.
     *
     * Selecciona cartas aleatorias según el tipo de tirada y opcionalmente
     * inicia la generación de interpretación automáticamente.
     *
     * @param spreadType Tipo de tirada elegida
     * @param question Pregunta del usuario (opcional, depende del tipo de tirada)
     * @param autoGenerateInterpretation Si true, genera la interpretación automáticamente tras la tirada
     */
    fun performReading(
        spreadType: SpreadType,
        question: String?,
        autoGenerateInterpretation: Boolean = true
    ) {
        viewModelScope.launch {
            _readingUiState.value = ReadingUiState.Loading

            val result = performReadingUseCase(spreadType, question)

            _readingUiState.value = if (result.isSuccess) {
                val reading = result.getOrThrow()

                // Opcionalmente generar interpretación automáticamente
                if (autoGenerateInterpretation) {
                    generateInterpretation(reading)
                }

                ReadingUiState.Success(reading)
            } else {
                ReadingUiState.Error(
                    result.exceptionOrNull()?.message ?: "Error al realizar la tirada"
                )
            }
        }
    }

    /**
     * Genera la interpretación de una tirada usando la IA de Claude.
     *
     * @param reading Tirada a interpretar
     */
    fun generateInterpretation(reading: TarotReading) {
        viewModelScope.launch {
            _interpretationUiState.value = InterpretationUiState.Loading

            val result = generateInterpretationUseCase(reading)

            _interpretationUiState.value = if (result.isSuccess) {
                InterpretationUiState.Success(result.getOrThrow())
            } else {
                InterpretationUiState.Error(
                    result.exceptionOrNull()?.message ?: "Error al generar la interpretación"
                )
            }
        }
    }

    /**
     * Reintenta generar la interpretación en caso de error.
     */
    fun retryInterpretation(reading: TarotReading) {
        generateInterpretation(reading)
    }

    /**
     * Verifica si un tipo de tirada requiere pregunta.
     */
    fun requiresQuestion(spreadType: SpreadType): Boolean {
        val config = getSpreadConfigurationUseCase(spreadType)
        return config.requiresQuestion
    }

    /**
     * Reinicia el estado de la tirada a Idle.
     */
    fun resetReadingState() {
        _readingUiState.value = ReadingUiState.Idle
    }

    /**
     * Reinicia el estado de la interpretación a Idle.
     */
    fun resetInterpretationState() {
        _interpretationUiState.value = InterpretationUiState.Idle
    }

    /**
     * Reinicia todos los estados a Idle (para iniciar una nueva tirada).
     */
    fun resetAllStates() {
        resetReadingState()
        resetInterpretationState()
    }

    /**
     * Guarda la lectura actual en el historial (v1.2.0).
     *
     * @param reading Tirada de tarot a guardar
     * @param interpretation Interpretación generada por IA
     * @param consultantName Nombre del consultante
     */
    fun saveToHistory(
        reading: TarotReading,
        interpretation: com.waveapp.tarotai.domain.model.Interpretation,
        consultantName: String
    ) {
        viewModelScope.launch {
            _saveState.value = SaveState.Saving

            val readingHistory = ReadingHistory(
                id = 0, // Será autogenerado por Room
                timestamp = System.currentTimeMillis(),
                consultantName = consultantName,
                spreadType = reading.spreadType,
                question = reading.question,
                drawnCards = reading.drawnCards,
                interpretation = interpretation,
                notes = null
            )

            val result = saveReadingUseCase(readingHistory)

            _saveState.value = if (result.isSuccess) {
                SaveState.Saved(result.getOrThrow())
            } else {
                SaveState.Error(result.exceptionOrNull()?.message ?: "Error al guardar la lectura")
            }
        }
    }
}

/**
 * Estados del guardado en historial (v1.2.0).
 */
sealed class SaveState {
    /** No se ha guardado todavía */
    data object NotSaved : SaveState()

    /** Guardando en progreso */
    data object Saving : SaveState()

    /** Guardado exitosamente */
    data class Saved(val readingId: Long) : SaveState()

    /** Error al guardar */
    data class Error(val message: String) : SaveState()
}
