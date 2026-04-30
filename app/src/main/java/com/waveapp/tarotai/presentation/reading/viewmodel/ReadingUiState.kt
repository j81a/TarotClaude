package com.waveapp.tarotai.presentation.reading.viewmodel

import com.waveapp.tarotai.domain.model.Interpretation
import com.waveapp.tarotai.domain.model.TarotReading

/**
 * Estados de UI para la pantalla de tirada.
 */
sealed class ReadingUiState {
    /** Estado inicial (sin acción) */
    object Idle : ReadingUiState()

    /** Cargando la tirada de cartas */
    object Loading : ReadingUiState()

    /** Tirada completada exitosamente */
    data class Success(val reading: TarotReading) : ReadingUiState()

    /** Error al realizar la tirada */
    data class Error(val message: String) : ReadingUiState()
}

/**
 * Estados de UI para la interpretación de la tirada.
 */
sealed class InterpretationUiState {
    /** Estado inicial (sin acción) */
    object Idle : InterpretationUiState()

    /** Generando interpretación con IA */
    object Loading : InterpretationUiState()

    /** Interpretación generada exitosamente */
    data class Success(val interpretation: Interpretation) : InterpretationUiState()

    /** Error al generar la interpretación */
    data class Error(val message: String) : InterpretationUiState()
}
