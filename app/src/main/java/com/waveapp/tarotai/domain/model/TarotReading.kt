package com.waveapp.tarotai.domain.model

/**
 * Representa una tirada de tarot completa.
 *
 * @property id ID único de la tirada (para historial)
 * @property spreadType Tipo de tirada realizada
 * @property question Pregunta del usuario (opcional)
 * @property drawnCards Lista de cartas seleccionadas con sus posiciones
 * @property timestamp Timestamp de cuándo se realizó la tirada
 */
data class TarotReading(
    val id: String,
    val spreadType: SpreadType,
    val question: String?,
    val drawnCards: List<DrawnCard>,
    val timestamp: Long = System.currentTimeMillis()
)
