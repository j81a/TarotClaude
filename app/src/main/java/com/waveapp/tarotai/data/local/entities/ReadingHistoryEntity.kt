package com.waveapp.tarotai.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entidad de Room para el historial de lecturas guardadas.
 *
 * Almacena:
 * - Información básica de la lectura (timestamp, consultante, tipo de tirada, pregunta)
 * - Cartas tiradas (serializadas como JSON)
 * - Interpretación generada por IA (serializada como JSON)
 * - Notas personales del tarotista (editables, serializadas como JSON)
 *
 * @property id ID autogenerado de la lectura
 * @property timestamp Timestamp en milisegundos de cuándo se realizó la lectura
 * @property consultantName Nombre del consultante (obligatorio)
 * @property spreadType Tipo de tirada (THREE_CARD, CELTIC_CROSS, YES_NO)
 * @property question Pregunta realizada (opcional, puede ser null)
 * @property drawnCardsJson JSON serializado de List<DrawnCard>
 * @property interpretationJson JSON serializado de Interpretation
 * @property notesJson JSON serializado de List<ReadingNote> (v1.2.0)
 */
@Entity(
    tableName = "reading_history",
    indices = [Index(value = ["timestamp"], unique = false)]
)
data class ReadingHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val timestamp: Long,
    val consultantName: String,
    val spreadType: String,
    val question: String?,
    val drawnCardsJson: String,
    val interpretationJson: String,
    val notesJson: String = "[]" // v1.2.0: Lista vacía por defecto
)
