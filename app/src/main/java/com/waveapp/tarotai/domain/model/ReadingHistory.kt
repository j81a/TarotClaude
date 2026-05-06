package com.waveapp.tarotai.domain.model

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
 * @property notes Notas personales del tarotista (editables)
 *
 * @since v1.1.0
 */
data class ReadingHistory(
    val id: Long,
    val timestamp: Long,
    val consultantName: String,
    val spreadType: SpreadType,
    val question: String?,
    val drawnCards: List<DrawnCard>,
    val interpretation: Interpretation,
    val notes: String? = null
)
