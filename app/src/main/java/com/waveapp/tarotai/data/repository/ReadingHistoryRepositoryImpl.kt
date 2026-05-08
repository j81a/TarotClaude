package com.waveapp.tarotai.data.repository

import com.waveapp.tarotai.data.local.dao.ReadingHistoryDao
import com.waveapp.tarotai.data.local.mapper.toDomain
import com.waveapp.tarotai.data.local.mapper.toEntity
import com.waveapp.tarotai.domain.model.ReadingHistory
import com.waveapp.tarotai.domain.repository.ReadingHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementación del repositorio de historial de lecturas.
 *
 * Responsabilidades:
 * - Acceder a la base de datos Room a través del DAO
 * - Convertir entre entidades de BD y modelos de dominio usando mappers
 * - Manejar errores y devolver Results
 * - Proporcionar Flow reactivos para observar cambios
 *
 * @param readingHistoryDao DAO de Room para acceder a reading_history
 *
 * @since v1.1.0
 */
class ReadingHistoryRepositoryImpl @Inject constructor(
    private val readingHistoryDao: ReadingHistoryDao
) : ReadingHistoryRepository {

    /**
     * Obtiene todas las lecturas guardadas ordenadas por fecha descendente.
     * El Flow emite una nueva lista cada vez que cambia la tabla en Room.
     *
     * @return Flow de lista de lecturas (más reciente primero)
     */
    override fun getAllReadings(): Flow<List<ReadingHistory>> {
        return readingHistoryDao.getAllReadings()
            .map { entities ->
                entities.map { it.toDomain() }
            }
    }

    /**
     * Obtiene una lectura específica por su ID.
     *
     * @param id ID de la lectura a buscar
     * @return Result con la lectura si existe, o error si no se encuentra
     */
    override suspend fun getReadingById(id: Long): Result<ReadingHistory> {
        return try {
            val entity = readingHistoryDao.getReadingById(id)
            if (entity != null) {
                Result.success(entity.toDomain())
            } else {
                Result.failure(Exception("Lectura con ID $id no encontrada"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Guarda una nueva lectura en el historial.
     *
     * @param reading Lectura a guardar
     * @return Result con el ID de la lectura insertada, o error si falla
     */
    override suspend fun saveReading(reading: ReadingHistory): Result<Long> {
        return try {
            val entity = reading.toEntity()
            val insertedId = readingHistoryDao.insertReading(entity)
            Result.success(insertedId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Actualiza las notas de una lectura existente.
     * Usado para el autosave en ReadingDetailScreen.
     *
     * @param id ID de la lectura a actualizar
     * @param notes Nuevas notas (puede ser null para borrar)
     * @return Result con Unit si éxito, o error si falla
     * @deprecated Usar updateReading para mayor flexibilidad (v1.2.0+)
     * NOTA: Comentado porque el método updateNotes en el DAO fue removido
     */
    /*
    override suspend fun updateNotes(id: Long, notes: String?): Result<Unit> {
        return try {
            readingHistoryDao.updateNotes(id, notes)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    */

    /**
     * Actualiza una lectura completa.
     * v1.2.0: Reemplaza updateNotes para mayor flexibilidad.
     *
     * @param reading Lectura actualizada
     * @return Result con Unit si éxito, o error si falla
     */
    override suspend fun updateReading(reading: ReadingHistory): Result<Unit> {
        return try {
            val entity = reading.toEntity()
            readingHistoryDao.updateReading(entity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Elimina una lectura del historial.
     *
     * @param id ID de la lectura a eliminar
     * @return Result con Unit si éxito, o error si falla
     */
    override suspend fun deleteReading(id: Long): Result<Unit> {
        return try {
            val entity = readingHistoryDao.getReadingById(id)
            if (entity != null) {
                readingHistoryDao.deleteReading(entity)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Lectura con ID $id no encontrada"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
