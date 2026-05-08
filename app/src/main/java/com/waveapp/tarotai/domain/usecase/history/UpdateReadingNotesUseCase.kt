package com.waveapp.tarotai.domain.usecase.history

/*
 * DEPRECATED: Este UseCase ya no se usa desde v1.2.0
 * Se reemplazó con UpdateReadingUseCase que usa readingHistory.notes (List<ReadingNote>)
 * en lugar de readingHistory.notes (String?)
 *
 * Comentado completamente para evitar errores de compilación.
 */

/*
import com.waveapp.tarotai.domain.repository.ReadingHistoryRepository
import javax.inject.Inject

/**
 * Caso de uso para actualizar las notas de una lectura guardada.
 *
 * Responsabilidades:
 * - Validar que el ID sea válido
 * - Validar que las notas no excedan el límite de caracteres
 * - Actualizar las notas en el repositorio
 *
 * Usado desde:
 * - ReadingDetailScreen (autosave de notas después de 2 segundos de inactividad)
 *
 * @param readingHistoryRepository Repositorio de historial
 *
 * @since v1.1.0
 */
class UpdateReadingNotesUseCase @Inject constructor(
    private val readingHistoryRepository: ReadingHistoryRepository
) {

    companion object {
        const val MAX_NOTES_LENGTH = 2000 // Máximo 2000 caracteres
    }

    /**
     * Actualiza las notas de una lectura.
     *
     * @param id ID de la lectura a actualizar
     * @param notes Nuevas notas (puede ser null para borrar, o vacío)
     * @return Result con Unit si éxito, o error si falla la validación o el guardado
     */
    suspend operator fun invoke(id: Long, notes: String?): Result<Unit> {
        // Validar que el ID sea válido
        if (id <= 0) {
            return Result.failure(IllegalArgumentException("ID inválido: $id"))
        }

        // Validar longitud de notas
        if (notes != null && notes.length > MAX_NOTES_LENGTH) {
            return Result.failure(
                IllegalArgumentException(
                    "Las notas no pueden exceder $MAX_NOTES_LENGTH caracteres (actual: ${notes.length})"
                )
            )
        }

        // Actualizar en el repositorio
        // Notas vacías se convierten en null
        val finalNotes = if (notes.isNullOrBlank()) null else notes
        return readingHistoryRepository.updateNotes(id, finalNotes)
    }
}
*/
