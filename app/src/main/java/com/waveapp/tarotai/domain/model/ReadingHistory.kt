package com.waveapp.tarotai.domain.model

import kotlinx.serialization.Serializable

/**
 * Modelo de una nota individual para una lectura.
 *
 * @property id ID único de la nota (UUID)
 * @property timestamp Timestamp en milisegundos de cuándo se creó la nota
 * @property text Texto de la nota
 *
 * @since v1.2.0
 */
@Serializable
data class ReadingNote(
    val id: String,
    val timestamp: Long,
    val text: String
)

/**
 * Modelo de dominio para una lectura guardada en el historial.
 *
 * Representa una lectura completa que ha sido guardada por el usuario, incluyendo:
 * - Información de la lectura (timestamp, consultante, tipo, pregunta)
 * - Las cartas que fueron tiradas
 * - La interpretación generada por IA
 * - Notas personales editables del tarotista
 *
 * Este modelo se usa en la capa de presentación y casos de uso.
 * Es independiente de la implementación de persistencia (Room).
 *
 * @property id ID único de la lectura en el historial
 * @property timestamp Timestamp en milisegundos de cuándo se realizó la lectura
 * @property consultantName Nombre del consultante (obligatorio)
 * @property spreadType Tipo de tirada realizada
 * @property question Pregunta realizada (opcional)
 * @property drawnCards Lista de cartas tiradas con sus posiciones y orientaciones
 * @property interpretation Interpretación completa generada por IA
 * @property notes Lista de notas personales del tarotista (editables)
 *
 * @since v1.1.0
 * @since v1.2.0 - Cambio de notes: String? a notes: List<ReadingNote>
 */
data class ReadingHistory(
    val id: Long,
    val timestamp: Long,
    val consultantName: String,
    val spreadType: SpreadType,
    val question: String?,
    val drawnCards: List<DrawnCard>,
    val interpretation: Interpretation,
    val notes: List<ReadingNote> = emptyList()
)
