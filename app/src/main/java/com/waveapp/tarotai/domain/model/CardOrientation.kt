package com.waveapp.tarotai.domain.model

import kotlinx.serialization.Serializable

/**
 * Orientación de una carta en una tirada.
 */
@Serializable
enum class CardOrientation {
    UPRIGHT,    // Derecha (posición normal)
    REVERSED    // Invertida (al revés)
}
