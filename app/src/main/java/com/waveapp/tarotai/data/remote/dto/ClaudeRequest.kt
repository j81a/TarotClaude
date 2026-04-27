package com.waveapp.tarotai.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Modelos para las peticiones a la API de Claude.
 *
 * @Serializable: Marca estas clases para que Kotlin Serialization pueda convertirlas a/desde JSON.
 * @SerialName: Especifica el nombre del campo en el JSON (útil cuando difiere del nombre en Kotlin).
 *
 * Documentación: https://docs.anthropic.com/claude/reference/messages_post
 */

/**
 * Petición completa a la API de Claude.
 *
 * @param model: ID del modelo a usar (ej: "claude-3-5-sonnet-20241022")
 * @param maxTokens: Máximo de tokens en la respuesta (1 token ≈ 4 caracteres)
 * @param messages: Lista de mensajes de la conversación
 * @param system: Prompt del sistema (opcional, define el comportamiento de Claude)
 */
@Serializable
data class ClaudeRequest(
    @SerialName("model")
    val model: String,

    @SerialName("max_tokens")
    val maxTokens: Int,

    @SerialName("messages")
    val messages: List<Message>,

    @SerialName("system")
    val system: String? = null
)

/**
 * Mensaje individual en la conversación.
 *
 * @param role: Rol del mensaje ("user" o "assistant")
 * @param content: Contenido del mensaje (texto)
 */
@Serializable
data class Message(
    @SerialName("role")
    val role: String,  // "user" o "assistant"

    @SerialName("content")
    val content: String
)
