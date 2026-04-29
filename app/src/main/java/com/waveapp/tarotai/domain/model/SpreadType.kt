package com.waveapp.tarotai.domain.model

/**
 * Tipos de tirada disponibles en la aplicación.
 */
enum class SpreadType {
    SIMPLE,     // Carta Simple (1 carta)
    YES_NO,     // Sí o No (1 carta con interpretación específica)
    PRESENT,    // Presente (3 cartas)
    TENDENCY,   // Tendencia (3 cartas)
    CROSS       // Cruz (5 cartas)
}
