package com.waveapp.tarotai.domain.usecase.manualload

import com.waveapp.tarotai.domain.model.Interpretation
import com.waveapp.tarotai.domain.model.ManualLoadConfiguration
import com.waveapp.tarotai.domain.model.SpreadConfiguration
import com.waveapp.tarotai.domain.model.TarotReading
import com.waveapp.tarotai.domain.usecase.GenerateInterpretationUseCase
import java.util.UUID
import javax.inject.Inject

/**
 * Caso de uso para generar interpretación desde una carga manual.
 *
 * Este caso de uso orquesta:
 * 1. Validación de la configuración manual
 * 2. Conversión del estado manual a formato de interpretación (DrawnCards)
 * 3. Delegación al caso de uso general de interpretación
 *
 * A diferencia de las tiradas automáticas, aquí las cartas ya están seleccionadas
 * y solo necesitamos generar la interpretación basándonos en ellas.
 *
 * @param validateManualLoadConfigUseCase Para validar antes de interpretar
 * @param generateInterpretationUseCase Para generar la interpretación con Claude
 *
 * @since v1.1.0
 */
class GenerateInterpretationFromManualLoadUseCase @Inject constructor(
    private val validateManualLoadConfigUseCase: ValidateManualLoadConfigUseCase,
    private val generateInterpretationUseCase: GenerateInterpretationUseCase
) {

    /**
     * Genera una interpretación desde una configuración de carga manual.
     *
     * Flujo:
     * 1. Valida que la configuración esté completa y sea válida
     * 2. Convierte el estado manual a lista de DrawnCard
     * 3. Genera la interpretación usando el caso de uso general
     *
     * @param config Configuración de carga manual completa
     * @return Result con la interpretación generada o error específico
     */
    suspend operator fun invoke(
        config: ManualLoadConfiguration
    ): Result<Interpretation> {
        // 1. Validar la configuración
        val validationResult = validateManualLoadConfigUseCase(config)
        if (validationResult.isFailure) {
            return Result.failure(
                validationResult.exceptionOrNull()
                    ?: IllegalStateException("Error de validación desconocido")
            )
        }

        // 2. Convertir estado manual a DrawnCards
        val drawnCards = config.state.toDrawnCards()

        if (drawnCards.isEmpty()) {
            return Result.failure(
                IllegalStateException("No hay cartas seleccionadas para interpretar")
            )
        }

        // 3. Verificar que la cantidad de cartas coincide con el spread type
        val spreadConfig = SpreadConfiguration.fromType(config.spreadType)
        val requiredCards = spreadConfig.cardCount
        if (drawnCards.size != requiredCards) {
            return Result.failure(
                IllegalStateException(
                    "Cantidad incorrecta de cartas. Esperado: $requiredCards, Actual: ${drawnCards.size}"
                )
            )
        }

        // 4. Crear TarotReading a partir de la configuración manual
        val reading = TarotReading(
            id = UUID.randomUUID().toString(),
            spreadType = config.spreadType,
            question = config.question,
            drawnCards = drawnCards,
            timestamp = System.currentTimeMillis()
        )

        // 5. Generar interpretación usando el caso de uso general
        return try {
            generateInterpretationUseCase(reading)
        } catch (e: Exception) {
            Result.failure(
                RuntimeException(
                    "Error al generar interpretación desde carga manual: ${e.message}",
                    e
                )
            )
        }
    }
}
