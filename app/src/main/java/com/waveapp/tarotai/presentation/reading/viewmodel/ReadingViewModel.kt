package com.waveapp.tarotai.presentation.reading.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waveapp.tarotai.domain.model.SpreadType
import com.waveapp.tarotai.domain.usecase.GetSpreadConfigurationUseCase
import com.waveapp.tarotai.domain.usecase.PerformReadingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para el flujo de tiradas de tarot.
 *
 * Maneja la lógica de selección de tipo de tirada, pregunta y realización de tirada.
 */
@HiltViewModel
class ReadingViewModel @Inject constructor(
    private val performReadingUseCase: PerformReadingUseCase,
    private val getSpreadConfigurationUseCase: GetSpreadConfigurationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ReadingUiState>(ReadingUiState.Idle)
    val uiState: StateFlow<ReadingUiState> = _uiState.asStateFlow()

    /**
     * Realiza una tirada de tarot.
     *
     * @param spreadType Tipo de tirada elegida
     * @param question Pregunta del usuario (opcional, depende del tipo de tirada)
     */
    fun performReading(spreadType: SpreadType, question: String?) {
        viewModelScope.launch {
            _uiState.value = ReadingUiState.Loading

            val result = performReadingUseCase(spreadType, question)

            _uiState.value = if (result.isSuccess) {
                ReadingUiState.Success(result.getOrThrow())
            } else {
                ReadingUiState.Error(
                    result.exceptionOrNull()?.message ?: "Error al realizar la tirada"
                )
            }
        }
    }

    /**
     * Verifica si un tipo de tirada requiere pregunta.
     */
    fun requiresQuestion(spreadType: SpreadType): Boolean {
        val config = getSpreadConfigurationUseCase(spreadType)
        return config.requiresQuestion
    }

    /**
     * Reinicia el estado a Idle.
     */
    fun resetState() {
        _uiState.value = ReadingUiState.Idle
    }
}
