package com.waveapp.tarotai.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.waveapp.tarotai.data.local.entities.ReadingHistoryEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO (Data Access Object) para acceder a la tabla reading_history.
 *
 * Provee métodos para:
 * - Obtener todas las lecturas guardadas (ordenadas por fecha descendente)
 * - Obtener una lectura específica por ID
 * - Insertar nuevas lecturas
 * - Actualizar las notas de una lectura existente
 * - Eliminar lecturas (opcional)
 *
 * Los métodos de lectura devuelven Flow para observar cambios en tiempo real.
 * Los métodos de escritura son suspend functions para ejecutarse en corrutinas.
 */
@Dao
interface ReadingHistoryDao {

    /**
     * Obtiene todas las lecturas guardadas ordenadas por fecha descendente.
     * Devuelve Flow para observar cambios en tiempo real.
     *
     * @return Flow de lista de lecturas (más reciente primero)
     */
    @Query("SELECT * FROM reading_history ORDER BY timestamp DESC")
    fun getAllReadings(): Flow<List<ReadingHistoryEntity>>

    /**
     * Obtiene una lectura específica por su ID.
     *
     * @param readingId ID de la lectura a buscar
     * @return La lectura encontrada o null si no existe
     */
    @Query("SELECT * FROM reading_history WHERE id = :readingId")
    suspend fun getReadingById(readingId: Long): ReadingHistoryEntity?

    /**
     * Inserta una nueva lectura en el historial.
     *
     * @param reading Lectura a insertar
     * @return ID de la lectura insertada (autogenerado)
     */
    @Insert
    suspend fun insertReading(reading: ReadingHistoryEntity): Long

    /**
     * Actualiza las notas de una lectura existente.
     * Usado para el autosave de notas en ReadingDetailScreen.
     *
     * @param readingId ID de la lectura a actualizar
     * @param notes Nuevas notas (puede ser null para borrar)
     * @deprecated Usar updateReading para mayor flexibilidad (v1.2.0+)
     * NOTA: Comentado porque la columna "notes" no existe, usar "notesJson" con updateReading
     */
    // @Query("UPDATE reading_history SET notes = :notes WHERE id = :readingId")
    // suspend fun updateNotes(readingId: Long, notes: String?)

    /**
     * Actualiza una lectura completa (v1.2.0).
     *
     * @param reading Lectura actualizada (debe tener id válido)
     */
    @Update
    suspend fun updateReading(reading: ReadingHistoryEntity)

    /**
     * Elimina una lectura del historial.
     * Opcional: puede usarse para implementar funcionalidad de borrado.
     *
     * @param reading Lectura a eliminar
     */
    @Delete
    suspend fun deleteReading(reading: ReadingHistoryEntity)
}
