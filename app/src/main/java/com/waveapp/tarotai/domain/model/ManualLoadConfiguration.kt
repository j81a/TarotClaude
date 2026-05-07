package com.waveapp.tarotai.domain.model

import kotlinx.serialization.Serializable

/**
 * Configuración para una carga manual de tirada.
 *
 * Representa la configuración completa de una sesión de carga manual:
 * - Tipo de tirada seleccionada
 * - Pregunta del consultante
 * - Nombre del consultante
 * - Estado actual de las cartas seleccionadas
 *
 * @property spreadType Tipo de tirada (Simple, 3 Cartas, Cruz, etc.)
 * @property question Pregunta opcional del consultante
 * @property consultantName Nombre del consultante (obligatorio en carga manual)
 * @property state Estado actual de las cartas seleccionadas
 *
 * @since v1.1.0
 */
@Serializable
data class ManualLoadConfiguration(
    val spreadType: SpreadType,
    val question: String?,
    val consultantName: String,
    val state: ManualLoadState = ManualLoadState()
) {
    /**
     * Verifica si la configuración está completa para generar interpretación.
     * @return true si todas las posiciones requeridas tienen cartas asignadas
     */
    fun isComplete(): Boolean {
        val config = SpreadConfiguration.fromType(spreadType)
        return state.selectedCards.size == config.cardCount
    }

    /**
     * Obtiene el progreso actual de la carga manual.
     * @return Par de (cartas seleccionadas, cartas requeridas)
     */
    fun getProgress(): Pair<Int, Int> {
        val config = SpreadConfiguration.fromType(spreadType)
        return Pair(state.selectedCards.size, config.cardCount)
    }

    /**
     * Verifica si una posición específica ya tiene carta asignada.
     * @param positionIndex Índice de la posición (0-based)
     * @return true si la posición tiene carta asignada
     */
    fun hasCardAtPosition(positionIndex: Int): Boolean {
        return state.selectedCards.any { it.positionIndex == positionIndex }
    }

    /**
     * Obtiene la carta asignada a una posición específica.
     * @param positionIndex Índice de la posición (0-based)
     * @return ManualLoadCard si existe, null si no
     */
    fun getCardAtPosition(positionIndex: Int): ManualLoadCard? {
        return state.selectedCards.find { it.positionIndex == positionIndex }
    }
}
