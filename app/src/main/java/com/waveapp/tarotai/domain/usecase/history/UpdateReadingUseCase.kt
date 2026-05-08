package com.waveapp.tarotai.domain.usecase.history

import com.waveapp.tarotai.domain.model.ReadingHistory
import com.waveapp.tarotai.domain.repository.ReadingHistoryRepository
import javax.inject.Inject

/**
 * Caso de uso para actualizar una lectura completa en el historial.
 *
 * Responsabilidades:
 * - Validar que el ID sea válido
 * - Actualizar la lectura completa en el repositorio
 *
 * Usado desde:
 * - ReadingDetailViewModel (para actualizar notas)
 *
 * @param readingHistoryRepository Repositorio de historial
 *
 * @since v1.2.0
 */
class UpdateReadingUseCase @Inject constructor(
    private val readingHistoryRepository: ReadingHistoryRepository
) {

    /**
     * Actualiza una lectura completa.
     *
     * @param reading Lectura actualizada
     * @return Result con Unit si éxito, o error si falla
     */
    suspend operator fun invoke(reading: ReadingHistory): Result<Unit> {
        // Validar que el ID sea válido
        if (reading.id <= 0) {
            return Result.failure(IllegalArgumentException("ID inválido: ${reading.id}"))
        }

        // Actualizar en el repositorio
        return readingHistoryRepository.updateReading(reading)
    }
}
