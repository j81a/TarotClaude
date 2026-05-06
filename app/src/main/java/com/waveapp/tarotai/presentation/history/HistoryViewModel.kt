package com.waveapp.tarotai.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waveapp.tarotai.domain.model.ReadingHistory
import com.waveapp.tarotai.domain.usecase.history.GetAllReadingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * ViewModel para la pantalla de historial de lecturas.
 *
 * Responsabilidades:
 * - Obtener todas las lecturas guardadas del repositorio
 * - Exponer el estado de las lecturas como StateFlow reactivo
 * - Manejar la navegación a los detalles de una lectura
 *
 * @param getAllReadingsUseCase Caso de uso para obtener todas las lecturas
 *
 * @since v1.1.0
 */
@HiltViewModel
class HistoryViewModel @Inject constructor(
    getAllReadingsUseCase: GetAllReadingsUseCase
) : ViewModel() {

    /**
     * Estado de las lecturas del historial.
     *
     * El Flow del Use Case se convierte en StateFlow para que Compose pueda observarlo.
     * - SharingStarted.WhileSubscribed(5000): Mantiene la conexión activa 5 segundos
     *   después de que el último colector se desconecte
     * - initialValue: Lista vacía mientras carga
     */
    val readings: StateFlow<List<ReadingHistory>> = getAllReadingsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}
