package com.waveapp.tarotai.domain.model

/**
 * Interpretación individual de una carta en una tirada.
 *
 * Representa la explicación de una carta específica considerando:
 * - Su posición en la tirada
 * - Su orientación (derecha o invertida)
 * - Su relación con la pregunta del usuario
 *
 * @property cardName Nombre de la carta interpretada (ej: "El Loco", "As de Copas")
 * @property position Nombre de la posición de la carta en la tirada (ej: "Pasado", "Presente")
 * @property interpretation Texto explicativo de la interpretación de la carta
 */
data class CardInterpretation(
    val cardName: String,
    val position: String,
    val interpretation: String
)
