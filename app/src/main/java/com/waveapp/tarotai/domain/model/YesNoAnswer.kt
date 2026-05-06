package com.waveapp.tarotai.domain.model

import kotlinx.serialization.Serializable

/**
 * Respuesta binaria para tiradas de tipo "Sí o No".
 *
 * Representa las posibles respuestas que puede dar una tirada de Sí o No:
 * - YES: Respuesta afirmativa
 * - NO: Respuesta negativa
 * - UNCLEAR: No se puede determinar una respuesta clara
 */
@Serializable
enum class YesNoAnswer {
    /** Respuesta afirmativa */
    YES,

    /** Respuesta negativa */
    NO,

    /** No se puede determinar una respuesta clara */
    UNCLEAR
}
