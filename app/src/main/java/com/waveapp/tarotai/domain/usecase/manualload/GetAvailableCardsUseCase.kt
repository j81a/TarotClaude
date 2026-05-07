package com.waveapp.tarotai.domain.usecase.manualload

import com.waveapp.tarotai.domain.model.CardFilter
import com.waveapp.tarotai.domain.model.ManualLoadState
import com.waveapp.tarotai.domain.model.TarotCard
import com.waveapp.tarotai.domain.repository.TarotCardRepository
import javax.inject.Inject

/**
 * Caso de uso para obtener las cartas disponibles para selección manual.
 *
 * Devuelve todas las cartas del tarot que:
 * - No han sido usadas todavía en esta tirada
 * - Coinciden con el filtro aplicado (todas, arcanos mayores, o palo específico)
 *
 * Las cartas se devuelven ordenadas por ID (orden natural del deck).
 *
 * @param tarotCardRepository Repositorio de cartas del tarot
 *
 * @since v1.1.0
 */
class GetAvailableCardsUseCase @Inject constructor(
    private val tarotCardRepository: TarotCardRepository
) {

    /**
     * Obtiene las cartas disponibles según el estado actual y el filtro aplicado.
     *
     * @param currentState Estado actual de la carga manual (para excluir cartas usadas)
     * @param filter Filtro a aplicar (por defecto: todas las cartas)
     * @return Result con la lista de cartas disponibles ordenadas por ID
     */
    suspend operator fun invoke(
        currentState: ManualLoadState,
        filter: CardFilter = CardFilter.ALL
    ): Result<List<TarotCard>> {
        return try {
            // Obtener todas las cartas del repositorio (una sola vez)
            val allCardsResult = tarotCardRepository.getAllCardsOnce()

            allCardsResult.fold(
                onSuccess = { allCards ->
                    // Aplicar filtro de tipo de carta
                    val filteredByType = filter.apply(allCards)

                    // Excluir cartas ya usadas
                    val availableCards = filteredByType.filter { card ->
                        !currentState.isCardUsed(card.id)
                    }

                    // Ordenar por ID (orden natural del deck)
                    val sortedCards = availableCards.sortedBy { it.id }

                    Result.success(sortedCards)
                },
                onFailure = { exception ->
                    Result.failure(
                        RuntimeException("Error al obtener las cartas: ${exception.message}", exception)
                    )
                }
            )
        } catch (e: Exception) {
            Result.failure(
                RuntimeException("Error inesperado al obtener cartas disponibles: ${e.message}", e)
            )
        }
    }

    /**
     * Obtiene el conteo de cartas disponibles según el filtro (sin estado).
     * Útil para mostrar información al usuario antes de seleccionar cartas.
     *
     * @param filter Filtro a aplicar
     * @return Result con el número de cartas disponibles
     */
    suspend fun getAvailableCount(filter: CardFilter = CardFilter.ALL): Result<Int> {
        return try {
            val allCardsResult = tarotCardRepository.getAllCardsOnce()

            allCardsResult.fold(
                onSuccess = { allCards ->
                    val filteredCards = filter.apply(allCards)
                    Result.success(filteredCards.size)
                },
                onFailure = { exception ->
                    Result.failure(exception)
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
