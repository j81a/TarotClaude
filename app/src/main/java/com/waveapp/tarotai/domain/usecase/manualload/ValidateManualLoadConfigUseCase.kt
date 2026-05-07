package com.waveapp.tarotai.domain.usecase.manualload

import com.waveapp.tarotai.domain.model.ManualLoadConfiguration
import com.waveapp.tarotai.domain.model.SpreadConfiguration
import javax.inject.Inject

/**
 * Caso de uso para validar una configuración de carga manual.
 *
 * Verifica que:
 * - El nombre del consultante sea válido (2-100 caracteres)
 * - Si hay pregunta, sea válida (10-500 caracteres)
 * - La configuración esté completa (todas las posiciones tengan carta)
 * - Las cartas seleccionadas sean válidas y no repetidas
 *
 * @since v1.1.0
 */
class ValidateManualLoadConfigUseCase @Inject constructor() {

    companion object {
        const val MIN_CONSULTANT_NAME_LENGTH = 2
        const val MAX_CONSULTANT_NAME_LENGTH = 100
        const val MIN_QUESTION_LENGTH = 10
        const val MAX_QUESTION_LENGTH = 500
    }

    /**
     * Valida la configuración de carga manual.
     *
     * @param config Configuración a validar
     * @return Result.success(Unit) si es válida, Result.failure con el error específico si no
     */
    operator fun invoke(config: ManualLoadConfiguration): Result<Unit> {
        // Validar nombre del consultante
        if (config.consultantName.isBlank()) {
            return Result.failure(
                IllegalArgumentException("El nombre del consultante es obligatorio")
            )
        }

        if (config.consultantName.length < MIN_CONSULTANT_NAME_LENGTH) {
            return Result.failure(
                IllegalArgumentException(
                    "El nombre del consultante debe tener al menos $MIN_CONSULTANT_NAME_LENGTH caracteres"
                )
            )
        }

        if (config.consultantName.length > MAX_CONSULTANT_NAME_LENGTH) {
            return Result.failure(
                IllegalArgumentException(
                    "El nombre del consultante no puede exceder $MAX_CONSULTANT_NAME_LENGTH caracteres"
                )
            )
        }

        // Validar pregunta (opcional, pero si existe debe ser válida)
        config.question?.let { question ->
            if (question.isNotBlank()) {
                if (question.length < MIN_QUESTION_LENGTH) {
                    return Result.failure(
                        IllegalArgumentException(
                            "La pregunta debe tener al menos $MIN_QUESTION_LENGTH caracteres"
                        )
                    )
                }

                if (question.length > MAX_QUESTION_LENGTH) {
                    return Result.failure(
                        IllegalArgumentException(
                            "La pregunta no puede exceder $MAX_QUESTION_LENGTH caracteres"
                        )
                    )
                }
            }
        }

        // Validar que la configuración esté completa
        if (!config.isComplete()) {
            val (current, required) = config.getProgress()
            return Result.failure(
                IllegalStateException(
                    "La tirada está incompleta: $current/$required cartas seleccionadas"
                )
            )
        }

        // Validar que no haya cartas duplicadas (doble verificación)
        val cardIds = config.state.selectedCards.map { it.card.id }
        if (cardIds.size != cardIds.toSet().size) {
            return Result.failure(
                IllegalStateException("Hay cartas duplicadas en la tirada")
            )
        }

        // Validar que las posiciones sean consecutivas y comiencen en 0
        val spreadConfig = SpreadConfiguration.fromType(config.spreadType)
        val positions = config.state.selectedCards.map { it.positionIndex }.sorted()
        val expectedPositions = (0 until spreadConfig.cardCount).toList()
        if (positions != expectedPositions) {
            return Result.failure(
                IllegalStateException(
                    "Las posiciones de las cartas no son válidas. " +
                    "Esperado: $expectedPositions, Actual: $positions"
                )
            )
        }

        return Result.success(Unit)
    }
}
