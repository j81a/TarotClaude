package com.waveapp.tarotai.domain.usecase.manualload

import com.waveapp.tarotai.domain.model.CardOrientation
import com.waveapp.tarotai.domain.model.ManualLoadState
import com.waveapp.tarotai.domain.model.TarotCard
import javax.inject.Inject

/**
 * Caso de uso para agregar una carta a una sesión de carga manual.
 *
 * Valida que:
 * - La carta no haya sido usada previamente en esta tirada
 * - La posición no tenga ya una carta asignada
 * - Los parámetros sean válidos
 *
 * Devuelve un nuevo estado actualizado si la operación es exitosa.
 *
 * @since v1.1.0
 */
class AddCardToManualLoadUseCase @Inject constructor() {

    /**
     * Agrega una carta al estado de carga manual.
     *
     * @param currentState Estado actual de la carga manual
     * @param card Carta a agregar
     * @param positionIndex Índice de la posición (0-based)
     * @param positionName Nombre de la posición (ej: "Pasado")
     * @param orientation Orientación de la carta física
     * @return Result con el nuevo estado si exitoso, o error específico
     */
    operator fun invoke(
        currentState: ManualLoadState,
        card: TarotCard,
        positionIndex: Int,
        positionName: String,
        orientation: CardOrientation
    ): Result<ManualLoadState> {
        // Validar parámetros básicos
        if (positionIndex < 0) {
            return Result.failure(
                IllegalArgumentException("El índice de posición debe ser >= 0")
            )
        }

        if (positionName.isBlank()) {
            return Result.failure(
                IllegalArgumentException("El nombre de la posición no puede estar vacío")
            )
        }

        // Validar que la carta no esté ya en uso
        if (currentState.isCardUsed(card.id)) {
            return Result.failure(
                IllegalStateException("La carta '${card.name}' ya ha sido seleccionada para esta tirada")
            )
        }

        // Validar que la posición no tenga ya una carta
        if (currentState.selectedCards.any { it.positionIndex == positionIndex }) {
            return Result.failure(
                IllegalStateException("La posición '$positionName' ya tiene una carta asignada")
            )
        }

        // Intentar agregar la carta al estado
        return try {
            val newState = currentState.addCard(
                card = card,
                positionIndex = positionIndex,
                positionName = positionName,
                orientation = orientation
            )
            Result.success(newState)
        } catch (e: IllegalArgumentException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(
                RuntimeException("Error inesperado al agregar la carta: ${e.message}", e)
            )
        }
    }
}
