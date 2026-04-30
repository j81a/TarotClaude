package com.waveapp.tarotai.domain.usecase

import com.waveapp.tarotai.domain.model.Interpretation
import com.waveapp.tarotai.domain.model.TarotReading
import com.waveapp.tarotai.domain.repository.ClaudeRepository
import javax.inject.Inject

/**
 * Caso de uso para generar interpretaciones de tiradas usando IA.
 *
 * Este use case orquesta la lógica de negocio para:
 * 1. Validar que la tirada tiene cartas
 * 2. Llamar al repositorio de Claude para generar la interpretación
 * 3. Retornar el resultado (éxito o error)
 *
 * Siguiendo Clean Architecture:
 * - Contiene la lógica de negocio de la aplicación
 * - Es independiente de frameworks (UI, BD, APIs)
 * - Puede ser testeado unitariamente con mocks
 *
 * @property claudeRepository Repositorio para interactuar con la API de Claude
 */
class GenerateInterpretationUseCase @Inject constructor(
    private val claudeRepository: ClaudeRepository
) {

    /**
     * Genera una interpretación de la tirada usando la API de Claude.
     *
     * @param reading Tirada de tarot a interpretar
     * @return Result con la interpretación generada o un error
     *
     * Casos de error:
     * - Tirada sin cartas (validación)
     * - Error de red (sin conexión, timeout)
     * - Error de autenticación (API key inválida)
     * - Error del servidor (500, 429)
     * - Error de parseo (respuesta JSON inválida)
     */
    suspend operator fun invoke(reading: TarotReading): Result<Interpretation> {
        // Validación: La tirada debe tener al menos una carta
        if (reading.drawnCards.isEmpty()) {
            return Result.failure(
                IllegalArgumentException("La tirada no tiene cartas para interpretar")
            )
        }

        // Llamar al repositorio para generar la interpretación
        return claudeRepository.generateInterpretation(reading)
    }
}
