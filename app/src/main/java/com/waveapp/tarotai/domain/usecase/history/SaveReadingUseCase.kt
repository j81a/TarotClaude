package com.waveapp.tarotai.domain.usecase.history

import com.waveapp.tarotai.domain.model.ReadingHistory
import com.waveapp.tarotai.domain.repository.ReadingHistoryRepository
import javax.inject.Inject

/**
 * Caso de uso para guardar una nueva lectura en el historial.
 *
 * Responsabilidades:
 * - Validar que la lectura tenga datos válidos
 * - Asignar timestamp actual si no tiene
 * - Delegar el guardado al repositorio
 * - Devolver el ID de la lectura guardada
 *
 * Usado desde:
 * - InterpretationScreen (al hacer click en "Guardar Lectura")
 *
 * @param readingHistoryRepository Repositorio de historial
 *
 * @since v1.1.0
 */
class SaveReadingUseCase @Inject constructor(
    private val readingHistoryRepository: ReadingHistoryRepository
) {

    /**
     * Guarda una lectura en el historial.
     *
     * @param reading Lectura a guardar (sin ID, será autogenerado)
     * @return Result con el ID de la lectura insertada, o error si falla
     */
    suspend operator fun invoke(reading: ReadingHistory): Result<Long> {
        // Validar que el nombre del consultante no esté vacío
        if (reading.consultantName.isBlank()) {
            return Result.failure(IllegalArgumentException("El nombre del consultante es obligatorio"))
        }

        // Validar que haya cartas
        if (reading.drawnCards.isEmpty()) {
            return Result.failure(IllegalArgumentException("La lectura debe contener cartas"))
        }

        // Asignar timestamp actual si no tiene (0 = nuevo registro)
        val readingWithTimestamp = if (reading.timestamp == 0L) {
            reading.copy(timestamp = System.currentTimeMillis())
        } else {
            reading
        }

        // Guardar en el repositorio
        return readingHistoryRepository.saveReading(readingWithTimestamp)
    }
}
