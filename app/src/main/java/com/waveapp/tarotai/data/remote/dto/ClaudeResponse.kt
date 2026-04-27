package com.waveapp.tarotai.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Modelos para las respuestas de la API de Claude.
 *
 * Estos DTOs mapean la estructura JSON que devuelve la API de Claude.
 * Documentación: https://docs.anthropic.com/claude/reference/messages_post
 */

/**
 * Respuesta completa de la API de Claude.
 *
 * @param id: ID único de la respuesta
 * @param type: Tipo de respuesta (siempre "message")
 * @param role: Rol del mensaje (siempre "assistant")
 * @param content: Lista de bloques de contenido (normalmente 1 bloque de texto)
 * @param model: Modelo usado para generar la respuesta
 * @param stopReason: Razón por la que Claude detuvo la generación
 * @param usage: Información sobre tokens consumidos
 */
@Serializable
data class ClaudeResponse(
    @SerialName("id")
    val id: String,

    @SerialName("type")
    val type: String,

    @SerialName("role")
    val role: String,

    @SerialName("content")
    val content: List<ContentBlock>,

    @SerialName("model")
    val model: String,

    @SerialName("stop_reason")
    val stopReason: String?,

    @SerialName("usage")
    val usage: Usage
)

/**
 * Bloque de contenido en la respuesta.
 *
 * @param type: Tipo de contenido (normalmente "text")
 * @param text: Texto generado por Claude
 */
@Serializable
data class ContentBlock(
    @SerialName("type")
    val type: String,

    @SerialName("text")
    val text: String
)

/**
 * Información sobre el uso de tokens.
 *
 * @param inputTokens: Tokens usados en el prompt (entrada)
 * @param outputTokens: Tokens usados en la respuesta (salida)
 *
 * Útil para calcular costos: ~$3 por millón de tokens de entrada, ~$15 por millón de salida.
 */
@Serializable
data class Usage(
    @SerialName("input_tokens")
    val inputTokens: Int,

    @SerialName("output_tokens")
    val outputTokens: Int
)
