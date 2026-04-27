package com.waveapp.tarotai.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.waveapp.tarotai.data.local.dao.TarotCardDao
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
 */
@Database(
    entities = [TarotCardEntity::class],  // Lista de tablas
    version = 1,                          // Versión del esquema
    exportSchema = false                  // No exportar esquema por ahora
)
@TypeConverters(Converters::class)        // Usar los converters para List<String>
abstract class TarotDatabase : RoomDatabase() {

    /**
     * Devuelve el DAO para acceder a la tabla de cartas.
     * Room implementa este método automáticamente.
     */
    abstract fun cardDao(): TarotCardDao
}
