package com.waveapp.tarotai.domain.model

import kotlinx.serialization.Serializable

/**
 * Filtros disponibles para selección de cartas en carga manual.
 *
 * Permite filtrar el deck completo por:
 * - Todas las cartas (sin filtro)
 * - Solo Arcanos Mayores
 * - Por palo de Arcanos Menores (Bastos, Copas, Espadas, Oros)
 *
 * Los filtros son mutuamente excluyentes (solo uno puede estar activo a la vez).
 *
 * @since v1.1.0
 */
@Serializable
enum class CardFilter {
    /**
     * Sin filtro - muestra todas las 78 cartas del tarot.
     */
    ALL,

    /**
     * Solo Arcanos Mayores (22 cartas).
     * Cartas de El Loco (0) a El Mundo (21).
     */
    MAJOR_ARCANA,

    /**
     * Solo Arcanos Menores de Bastos (14 cartas).
     * As hasta Rey de Bastos.
     */
    WANDS,

    /**
     * Solo Arcanos Menores de Copas (14 cartas).
     * As hasta Rey de Copas.
     */
    CUPS,

    /**
     * Solo Arcanos Menores de Espadas (14 cartas).
     * As hasta Rey de Espadas.
     */
    SWORDS,

    /**
     * Solo Arcanos Menores de Oros (14 cartas).
     * As hasta Rey de Oros.
     */
    PENTACLES;

    /**
     * Obtiene el nombre displayable del filtro para la UI.
     * @return Nombre en español del filtro
     */
    fun getDisplayName(): String {
        return when (this) {
            ALL -> "Todas"
            MAJOR_ARCANA -> "Arcanos Mayores"
            WANDS -> "Bastos"
            CUPS -> "Copas"
            SWORDS -> "Espadas"
            PENTACLES -> "Oros"
        }
    }

    /**
     * Aplica el filtro a una lista de cartas.
     * @param cards Lista completa de cartas
     * @return Lista filtrada según el criterio
     */
    fun apply(cards: List<TarotCard>): List<TarotCard> {
        return when (this) {
            ALL -> cards
            MAJOR_ARCANA -> cards.filter { it.arcanaType == ArcanaType.MAJOR }
            WANDS -> cards.filter { it.suit == Suit.WANDS }
            CUPS -> cards.filter { it.suit == Suit.CUPS }
            SWORDS -> cards.filter { it.suit == Suit.SWORDS }
            PENTACLES -> cards.filter { it.suit == Suit.PENTACLES }
        }
    }

    /**
     * Obtiene la cantidad de cartas que devuelve este filtro (en un deck completo).
     * @return Número de cartas
     */
    fun getCardCount(): Int {
        return when (this) {
            ALL -> 78
            MAJOR_ARCANA -> 22
            WANDS, CUPS, SWORDS, PENTACLES -> 14
        }
    }

    companion object {
        /**
         * Obtiene todos los filtros disponibles en orden de UI.
         * @return Lista ordenada de filtros
         */
        fun getAllFilters(): List<CardFilter> {
            return listOf(ALL, MAJOR_ARCANA, WANDS, CUPS, SWORDS, PENTACLES)
        }
    }
}
