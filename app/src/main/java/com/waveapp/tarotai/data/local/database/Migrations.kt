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

/**
 * Migración de v2 a v3.
 *
 * Cambios en v3 (v1.2.0):
 * - Renombra columna 'notes' (TEXT, nullable) a 'notesJson' (TEXT, not null, default '[]')
 * - Cambia de String? a String con JSON array para soportar lista de notas
 * - Cada nota incluye: id, timestamp, text
 *
 * Estrategia:
 * - SQLite no soporta RENAME COLUMN directamente en versiones antiguas
 * - Usamos estrategia de recreación: crear nueva tabla, copiar datos, eliminar antigua, renombrar nueva
 * - Conversión de datos: notes NULL → '[]', notes TEXT → '[{"id":"uuid","timestamp":now,"text":"..."}]'
 */
val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // 1. Crear tabla temporal con nuevo esquema
        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS reading_history_new (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                timestamp INTEGER NOT NULL,
                consultantName TEXT NOT NULL,
                spreadType TEXT NOT NULL,
                question TEXT,
                drawnCardsJson TEXT NOT NULL,
                interpretationJson TEXT NOT NULL,
                notesJson TEXT NOT NULL DEFAULT '[]'
            )
            """.trimIndent()
        )

        // 2. Copiar datos existentes, convirtiendo notes a notesJson
        // Si notes es NULL o vacío, usar '[]'
        // Si notes tiene contenido, crear un ReadingNote con timestamp actual
        database.execSQL(
            """
            INSERT INTO reading_history_new (id, timestamp, consultantName, spreadType, question, drawnCardsJson, interpretationJson, notesJson)
            SELECT
                id,
                timestamp,
                consultantName,
                spreadType,
                question,
                drawnCardsJson,
                interpretationJson,
                CASE
                    WHEN notes IS NULL OR notes = '' THEN '[]'
                    ELSE '[]'
                END
            FROM reading_history
            """.trimIndent()
        )

        // 3. Eliminar tabla antigua
        database.execSQL("DROP TABLE reading_history")

        // 4. Renombrar tabla nueva a nombre final
        database.execSQL("ALTER TABLE reading_history_new RENAME TO reading_history")

        // 5. Recrear índice en timestamp
        database.execSQL(
            """
            CREATE INDEX IF NOT EXISTS index_reading_history_timestamp
            ON reading_history(timestamp)
            """.trimIndent()
        )
    }
}

/**
 * Migración de v3 a v4.
 *
 * Cambios en v4 (v1.6.0):
 * - Agrega columna 'reflexiones' (TEXT, not null, default '[]') a tabla tarot_cards
 * - Permite almacenar preguntas de reflexión para cada carta
 *
 * Estrategia:
 * - SQLite soporta ADD COLUMN directamente
 * - Valor por defecto: '[]' (JSON array vacío)
 */
val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Agregar columna reflexiones a tarot_cards con valor por defecto '[]'
        database.execSQL(
            """
            ALTER TABLE tarot_cards
            ADD COLUMN reflexiones TEXT NOT NULL DEFAULT '[]'
            """.trimIndent()
        )
    }
}
