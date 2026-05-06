package com.waveapp.tarotai.domain.usecase.history

import com.waveapp.tarotai.domain.model.ReadingHistory
import com.waveapp.tarotai.domain.repository.ReadingHistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Caso de uso para obtener todas las lecturas guardadas en el historial.
 *
 * Responsabilidades:
 * - Obtener todas las lecturas del repositorio
 * - Devolver un Flow reactivo que emite cambios en tiempo real
 * - Las lecturas vienen ordenadas por fecha descendente (más reciente primero)
 *
 * Usado desde:
 * - HistoryScreen (para mostrar la lista de lecturas)
 *
 * @param readingHistoryRepository Repositorio de historial
 *
 * @since v1.1.0
 */
class GetAllReadingsUseCase @Inject constructor(
    private val readingHistoryRepository: ReadingHistoryRepository
) {

    /**
     * Obtiene todas las lecturas guardadas.
     *
     * @return Flow de lista de lecturas ordenadas por fecha descendente
     */
    operator fun invoke(): Flow<List<ReadingHistory>> {
        return readingHistoryRepository.getAllReadings()
    }
}
