package com.waveapp.tarotai.domain.usecase.history

import com.waveapp.tarotai.domain.model.ReadingHistory
import com.waveapp.tarotai.domain.repository.ReadingHistoryRepository
import javax.inject.Inject

/**
 * Caso de uso para obtener una lectura específica del historial por su ID.
 *
 * Responsabilidades:
 * - Validar que el ID sea válido
 * - Obtener la lectura del repositorio
 * - Manejar el caso de lectura no encontrada
 *
 * Usado desde:
 * - ReadingDetailScreen (para mostrar los detalles de una lectura guardada)
 *
 * @param readingHistoryRepository Repositorio de historial
 *
 * @since v1.1.0
 */
class GetReadingByIdUseCase @Inject constructor(
    private val readingHistoryRepository: ReadingHistoryRepository
) {

    /**
     * Obtiene una lectura específica por su ID.
     *
     * @param id ID de la lectura a buscar
     * @return Result con la lectura si existe, o error si no se encuentra o el ID es inválido
     */
    suspend operator fun invoke(id: Long): Result<ReadingHistory> {
        // Validar que el ID sea válido
        if (id <= 0) {
            return Result.failure(IllegalArgumentException("ID inválido: $id"))
        }

        // Obtener la lectura del repositorio
        return readingHistoryRepository.getReadingById(id)
    }
}
