package com.waveapp.tarotai.presentation.reading.viewmodel

import com.waveapp.tarotai.domain.model.TarotReading

/**
 * Estados de UI para la pantalla de tirada.
 */
sealed class ReadingUiState {
    object Idle : ReadingUiState()
    object Loading : ReadingUiState()
    data class Success(val reading: TarotReading) : ReadingUiState()
    data class Error(val message: String) : ReadingUiState()
}
