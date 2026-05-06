package com.waveapp.tarotai.data.local.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Migraciones de base de datos para Room.
 *
 * Cuando incrementamos la versión de la base de datos, necesitamos proporcionar
 * una Migration que indique cómo transformar el esquema antiguo al nuevo.
 *
 * Sin migraciones, Room destruirá la base de datos existente y creará una nueva,
 * perdiendo todos los datos del usuario.
 */

/**
 * Migración de v1 a v2.
 *
 * Cambios en v2 (v1.1.0):
 * - Agrega tabla reading_history para el historial de lecturas guardadas
 * - No modifica tabla tarot_cards existente (se conservan todos los datos)
 *
 * La tabla reading_history incluye:
 * - id (PRIMARY KEY, autogenerado)
 * - timestamp (con índice para ordenamiento rápido)
 * - consultantName (nombre del consultante, obligatorio)
 * - spreadType (tipo de tirada)
 * - question (pregunta realizada, opcional)
 * - drawnCardsJson (JSON serializado de List<DrawnCard>)
 * - interpretationJson (JSON serializado de Interpretation)
 * - notes (notas personales editables, opcional)
 */
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Crear tabla reading_history
        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS reading_history (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                timestamp INTEGER NOT NULL,
                consultantName TEXT NOT NULL,
                spreadType TEXT NOT NULL,
                question TEXT,
                drawnCardsJson TEXT NOT NULL,
                interpretationJson TEXT NOT NULL,
                notes TEXT
            )
            """.trimIndent()
        )

        // Crear índice en timestamp para optimizar queries ORDER BY timestamp DESC
        database.execSQL(
            """
            CREATE INDEX IF NOT EXISTS index_reading_history_timestamp
            ON reading_history(timestamp)
            """.trimIndent()
        )
    }
}
