package com.waveapp.tarotai.domain.usecase.manualload

import com.waveapp.tarotai.domain.model.ManualLoadState
import javax.inject.Inject

/**
 * Caso de uso para remover una carta de una sesión de carga manual.
 *
 * Permite deshacer la selección de una carta en una posición específica,
 * liberando tanto la posición como la carta para ser reutilizadas.
 *
 * Si la posición no tiene carta asignada, devuelve el mismo estado sin error
 * (operación idempotente).
 *
 * @since v1.1.0
 */
class RemoveCardFromManualLoadUseCase @Inject constructor() {

    /**
     * Remueve una carta del estado de carga manual.
     *
     * @param currentState Estado actual de la carga manual
     * @param positionIndex Índice de la posición (0-based) de la cual remover la carta
     * @return Result con el nuevo estado (siempre exitoso)
     */
    operator fun invoke(
        currentState: ManualLoadState,
        positionIndex: Int
    ): Result<ManualLoadState> {
        // Validar parámetros básicos
        if (positionIndex < 0) {
            return Result.failure(
                IllegalArgumentException("El índice de posición debe ser >= 0")
            )
        }

        // Remover la carta (si no existe, retorna el mismo estado)
        return try {
            val newState = currentState.removeCardAtPosition(positionIndex)
            Result.success(newState)
        } catch (e: Exception) {
            Result.failure(
                RuntimeException("Error inesperado al remover la carta: ${e.message}", e)
            )
        }
    }

    /**
     * Remueve todas las cartas del estado (reset completo).
     *
     * @param currentState Estado actual de la carga manual (ignorado)
     * @return Result con un nuevo estado vacío
     */
    fun removeAll(currentState: ManualLoadState): Result<ManualLoadState> {
        return Result.success(ManualLoadState())
    }
}
