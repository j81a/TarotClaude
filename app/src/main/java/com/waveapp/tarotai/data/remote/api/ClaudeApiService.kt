package com.waveapp.tarotai.data.remote.api

import com.waveapp.tarotai.data.remote.dto.ClaudeRequest
import com.waveapp.tarotai.data.remote.dto.ClaudeResponse
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Interfaz de Retrofit para la API de Claude.
 *
 * Retrofit genera automáticamente la implementación de esta interfaz.
 * Define los endpoints HTTP que usaremos para comunicarnos con Claude.
 *
 * API Reference: https://docs.anthropic.com/claude/reference/messages_post
 */
interface ClaudeApiService {

    /**
     * Envía un mensaje a Claude y obtiene su respuesta.
     *
     * @POST: Define que este es un request HTTP POST
     * @Body: Indica que el parámetro se enviará en el body del request como JSON
     *
     * @param request: Petición con el modelo, mensajes y configuración
     * @return Respuesta de Claude con el texto interpretativo
     *
     * Endpoint: POST https://api.anthropic.com/v1/messages
     */
    @POST("v1/messages")
    suspend fun sendMessage(
        @Body request: ClaudeRequest
    ): ClaudeResponse
}
