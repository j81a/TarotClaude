package com.waveapp.tarotai.domain.repository

import com.waveapp.tarotai.domain.model.Interpretation
import com.waveapp.tarotai.domain.model.TarotReading

/**
 * Interfaz del repositorio para interactuar con la API de Claude.
 *
 * Define el contrato para generar interpretaciones de tiradas de tarot
 * utilizando la IA de Claude (Anthropic).
 *
 * Esta interfaz sigue los principios de Clean Architecture:
 * - Está en la capa de dominio (independiente de frameworks)
 * - La implementación concreta está en la capa de datos
 * - Permite fácil testing con mocks
 */
interface ClaudeRepository {

    /**
     * Genera una interpretación de una tirada de tarot usando la API de Claude.
     *
     * Envía la tirada completa (tipo, pregunta, cartas) a Claude y obtiene:
     * - Interpretación individual de cada carta
     * - Interpretación general de toda la tirada
     * - Para tiradas YES_NO: respuesta binaria + justificación educativa
     *
     * @param reading Tirada de tarot a interpretar
     * @return Result con la interpretación generada o un error
     *
     * Posibles errores:
     * - Error de red (timeout, sin conexión)
     * - Error de autenticación (API key inválida)
     * - Error del servidor (500, 429 rate limit)
     * - Error de parseo (respuesta JSON inválida)
     */
    suspend fun generateInterpretation(reading: TarotReading): Result<Interpretation>
}
