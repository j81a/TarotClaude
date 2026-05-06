package com.waveapp.tarotai.domain.repository

import com.waveapp.tarotai.domain.model.ReadingHistory
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio para gestionar el historial de lecturas guardadas.
 *
 * Define las operaciones de acceso a datos para el historial:
 * - Obtener todas las lecturas guardadas
 * - Obtener una lectura específica por ID
 * - Guardar nuevas lecturas
 * - Actualizar notas de lecturas existentes
 * - Eliminar lecturas (opcional)
 *
 * Implementación en la capa de datos (data/repository).
 *
 * @since v1.1.0
 */
interface ReadingHistoryRepository {

    /**
     * Obtiene todas las lecturas guardadas ordenadas por fecha descendente.
     * Devuelve un Flow que emite la lista actualizada cada vez que cambia.
     *
     * @return Flow de lista de lecturas (más reciente primero)
     */
    fun getAllReadings(): Flow<List<ReadingHistory>>

    /**
     * Obtiene una lectura específica por su ID.
     *
     * @param id ID de la lectura a buscar
     * @return Result con la lectura si existe, o error si no se encuentra
     */
    suspend fun getReadingById(id: Long): Result<ReadingHistory>

    /**
     * Guarda una nueva lectura en el historial.
     *
     * @param reading Lectura a guardar
     * @return Result con el ID de la lectura insertada, o error si falla
     */
    suspend fun saveReading(reading: ReadingHistory): Result<Long>

    /**
     * Actualiza las notas de una lectura existente.
     * Usado para el autosave en ReadingDetailScreen.
     *
     * @param id ID de la lectura a actualizar
     * @param notes Nuevas notas (puede ser null para borrar)
     * @return Result con Unit si éxito, o error si falla
     */
    suspend fun updateNotes(id: Long, notes: String?): Result<Unit>

    /**
     * Elimina una lectura del historial.
     * Opcional: puede usarse para implementar funcionalidad de borrado.
     *
     * @param id ID de la lectura a eliminar
     * @return Result con Unit si éxito, o error si falla
     */
    suspend fun deleteReading(id: Long): Result<Unit>
}
