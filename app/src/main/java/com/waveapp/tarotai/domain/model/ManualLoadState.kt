package com.waveapp.tarotai.domain.model

import kotlinx.serialization.Serializable

/**
 * Estado de una sesión de carga manual de tirada.
 *
 * Mantiene el registro de:
 * - Cartas seleccionadas hasta el momento (con posición y orientación)
 * - IDs de cartas ya utilizadas (para evitar duplicados)
 *
 * Esta clase es inmutable y cada operación (agregar/remover carta)
 * devuelve una nueva instancia del estado.
 *
 * @property selectedCards Lista de cartas seleccionadas con su metadata
 * @property usedCardIds Set de IDs de cartas ya utilizadas en esta tirada
 *
 * @since v1.1.0
 */
@Serializable
data class ManualLoadState(
    val selectedCards: List<ManualLoadCard> = emptyList(),
    val usedCardIds: Set<Int> = emptySet()
) {
    /**
     * Agrega una carta al estado.
     *
     * @param card Carta del tarot a agregar
     * @param positionIndex Índice de la posición en el spread (0-based)
     * @param positionName Nombre de la posición (ej: "Pasado", "Presente")
     * @param orientation Orientación de la carta física (derecha/invertida)
     * @return Nuevo estado con la carta agregada
     * @throws IllegalArgumentException si la carta ya fue usada o la posición ya tiene carta
     */
    fun addCard(
        card: TarotCard,
        positionIndex: Int,
        positionName: String,
        orientation: CardOrientation
    ): ManualLoadState {
        require(!usedCardIds.contains(card.id)) {
            "La carta ${card.name} ya ha sido utilizada en esta tirada"
        }
        require(!selectedCards.any { it.positionIndex == positionIndex }) {
            "La posición $positionName ya tiene una carta asignada"
        }

        val newCard = ManualLoadCard(
            card = card,
            positionIndex = positionIndex,
            positionName = positionName,
            orientation = orientation
        )

        return copy(
            selectedCards = selectedCards + newCard,
            usedCardIds = usedCardIds + card.id
        )
    }

    /**
     * Remueve una carta del estado por índice de posición.
     *
     * @param positionIndex Índice de la posición (0-based)
     * @return Nuevo estado con la carta removida, o el mismo estado si no existe carta en esa posición
     */
    fun removeCardAtPosition(positionIndex: Int): ManualLoadState {
        val cardToRemove = selectedCards.find { it.positionIndex == positionIndex }
            ?: return this

        return copy(
            selectedCards = selectedCards.filter { it.positionIndex != positionIndex },
            usedCardIds = usedCardIds - cardToRemove.card.id
        )
    }

    /**
     * Verifica si una carta específica ya fue utilizada.
     * @param cardId ID de la carta
     * @return true si la carta ya está en uso
     */
    fun isCardUsed(cardId: Int): Boolean = usedCardIds.contains(cardId)

    /**
     * Convierte el estado a una lista de DrawnCard para interpretación.
     * @return Lista de DrawnCard ordenadas por posición
     */
    fun toDrawnCards(): List<DrawnCard> {
        return selectedCards
            .sortedBy { it.positionIndex }
            .map { it.toDrawnCard() }
    }
}

/**
 * Representa una carta seleccionada manualmente con su contexto.
 *
 * @property card La carta del tarot seleccionada
 * @property positionIndex Índice de la posición en el spread (0-based)
 * @property positionName Nombre de la posición (ej: "Pasado", "Presente")
 * @property orientation Orientación física de la carta
 *
 * @since v1.1.0
 */
@Serializable
data class ManualLoadCard(
    val card: TarotCard,
    val positionIndex: Int,
    val positionName: String,
    val orientation: CardOrientation
) {
    /**
     * Convierte a DrawnCard para uso en interpretación.
     */
    fun toDrawnCard(): DrawnCard {
        return DrawnCard(
            card = card,
            position = positionIndex,
            positionName = positionName,
            orientation = orientation
        )
    }
}
