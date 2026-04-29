package com.waveapp.tarotai.domain.model

/**
 * Representa una carta seleccionada en una tirada.
 *
 * @property card La carta del tarot
 * @property position Posición numérica en la tirada (0, 1, 2...)
 * @property positionName Nombre de la posición ("Pasado", "Presente", etc.)
 * @property orientation Orientación de la carta (derecha o invertida)
 */
data class DrawnCard(
    val card: TarotCard,
    val position: Int,
    val positionName: String,
    val orientation: CardOrientation
)
