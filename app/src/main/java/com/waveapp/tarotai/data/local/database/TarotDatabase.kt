package com.waveapp.tarotai.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.waveapp.tarotai.data.local.dao.ReadingHistoryDao
import com.waveapp.tarotai.data.local.dao.TarotCardDao
import com.waveapp.tarotai.data.local.entities.ReadingHistoryEntity
import com.waveapp.tarotai.data.local.entities.TarotCardEntity

/**
 * Base de datos principal de TarotAI usando Room.
 *
 * @Database: Marca esta clase abstracta como una base de datos de Room.
 * - entities: Lista de todas las tablas (entidades) de la BD.
 * - version: Versión del esquema de la BD. Incrementar cuando cambie el esquema.
 * - exportSchema: Si true, genera un archivo JSON con el esquema (útil para migraciones).
 *
 * @TypeConverters: Indica qué converters usar para tipos complejos.
 *
 * Room genera automáticamente la implementación de esta clase.
 *
 * ## Historial de versiones
 * - v1: Tabla tarot_cards (v1.0.0)
 * - v2: Agregada tabla reading_history (v1.1.0)
 * - v3: Cambio notes (String) a notesJson (String JSON array) (v1.2.0)
 * - v4: Agregado campo reflexiones a tarot_cards (v1.6.0)
 */
@Database(
    entities = [
        TarotCardEntity::class,
        ReadingHistoryEntity::class  // 🆕 Nueva tabla v1.1.0
    ],
    version = 4,                     // 🔄 Incrementado para v1.6.0
    exportSchema = false
)
@TypeConverters(Converters::class)   // Converters para List<String>, DrawnCard, Interpretation
abstract class TarotDatabase : RoomDatabase() {

    /**
     * Devuelve el DAO para acceder a la tabla de cartas.
     * Room implementa este método automáticamente.
     */
    abstract fun cardDao(): TarotCardDao

    /**
     * Devuelve el DAO para acceder a la tabla de historial de lecturas.
     * Room implementa este método automáticamente.
     *
     * @since v1.1.0
     */
    abstract fun readingHistoryDao(): ReadingHistoryDao
}
