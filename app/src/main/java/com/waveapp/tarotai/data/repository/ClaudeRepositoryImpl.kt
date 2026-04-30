package com.waveapp.tarotai.data.repository

import android.util.Log
import com.waveapp.tarotai.data.remote.api.ClaudeApiService
import com.waveapp.tarotai.data.remote.dto.ClaudeRequest
import com.waveapp.tarotai.data.remote.dto.Message
import com.waveapp.tarotai.data.remote.mapper.InterpretationMapper
import com.waveapp.tarotai.data.remote.prompt.PromptBuilder
import com.waveapp.tarotai.domain.model.Interpretation
import com.waveapp.tarotai.domain.model.TarotReading
import com.waveapp.tarotai.domain.repository.ClaudeRepository
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Implementación del repositorio para interactuar con la API de Claude.
 *
 * Gestiona:
 * - Construcción de prompts dinámicos
 * - Llamadas HTTP a la API de Claude
 * - Reintentos con backoff exponencial
 * - Parseo de respuestas JSON
 * - Manejo de errores (red, autenticación, rate limiting)
 *
 * @property claudeApiService Servicio de Retrofit para llamadas a la API
 */
class ClaudeRepositoryImpl @Inject constructor(
    private val claudeApiService: ClaudeApiService
) : ClaudeRepository {

    companion object {
        private const val TAG = "ClaudeRepository"
        private const val MAX_RETRIES = 2
        private const val INITIAL_BACKOFF_MS = 1000L
    }

    /**
     * Genera una interpretación de una tirada usando la API de Claude.
     *
     * Proceso:
     * 1. Construye el prompt dinámico con PromptBuilder
     * 2. Crea el request de Claude con el modelo y configuración
     * 3. Envía el request a la API (con reintentos si falla)
     * 4. Parsea la respuesta JSON a Interpretation
     * 5. Maneja errores y los convierte a mensajes legibles
     *
     * @param reading Tirada de tarot a interpretar
     * @return Result con la interpretación o error
     */
    override suspend fun generateInterpretation(reading: TarotReading): Result<Interpretation> {
        return try {
            Log.d(TAG, "Generando interpretación para tirada: ${reading.spreadType}")

            // 1. Construir el prompt dinámico
            val prompt = PromptBuilder.buildPrompt(reading)
            Log.d(TAG, "Prompt generado:\n$prompt")

            // 2. Crear el request de Claude
            val request = ClaudeRequest(
                model = PromptBuilder.getModel(),
                maxTokens = PromptBuilder.getMaxTokens(),
                messages = listOf(
                    Message(role = "user", content = prompt)
                ),
                system = PromptBuilder.getSystemPrompt()
            )

            // 3. Enviar request con reintentos
            val response = executeWithRetry {
                claudeApiService.sendMessage(request)
            }

            // 4. Extraer el texto de la respuesta
            val responseText = response.content.firstOrNull()?.text
                ?: throw Exception("Respuesta de Claude vacía")

            Log.d(TAG, "Respuesta recibida de Claude:\n$responseText")

            // 5. Parsear JSON a Interpretation
            val interpretation = InterpretationMapper.parseInterpretation(responseText)
            Log.d(TAG, "Interpretación parseada exitosamente")

            Result.success(interpretation)

        } catch (e: HttpException) {
            Log.e(TAG, "Error HTTP: ${e.code()}", e)
            Result.failure(mapHttpException(e))
        } catch (e: IOException) {
            Log.e(TAG, "Error de red", e)
            Result.failure(Exception("Error de conexión. Verifica tu internet."))
        } catch (e: Exception) {
            Log.e(TAG, "Error al generar interpretación", e)
            Result.failure(Exception("Error al procesar la interpretación: ${e.message}"))
        }
    }

    /**
     * Ejecuta una operación con reintentos y backoff exponencial.
     *
     * Si la operación falla, reintenta hasta MAX_RETRIES veces,
     * esperando cada vez más tiempo entre intentos (backoff exponencial).
     *
     * @param operation Operación suspendida a ejecutar
     * @return Resultado de la operación
     * @throws Exception Si todos los reintentos fallan
     */
    private suspend fun <T> executeWithRetry(operation: suspend () -> T): T {
        var lastException: Exception? = null
        var currentDelay = INITIAL_BACKOFF_MS

        repeat(MAX_RETRIES + 1) { attempt ->
            try {
                return operation()
            } catch (e: HttpException) {
                // Si es un error 4xx (cliente), no reintentar
                if (e.code() in 400..499 && e.code() != 429) {
                    throw e
                }
                lastException = e
                Log.w(TAG, "Intento ${attempt + 1} falló, reintentando en ${currentDelay}ms", e)
            } catch (e: IOException) {
                lastException = e
                Log.w(TAG, "Intento ${attempt + 1} falló, reintentando en ${currentDelay}ms", e)
            }

            // Esperar antes del siguiente intento (backoff exponencial)
            if (attempt < MAX_RETRIES) {
                delay(currentDelay)
                currentDelay *= 2  // Duplicar el tiempo de espera
            }
        }

        // Si llegamos aquí, todos los reintentos fallaron
        throw lastException ?: Exception("Error desconocido en la API")
    }

    /**
     * Mapea excepciones HTTP a mensajes de error legibles para el usuario.
     *
     * @param exception Excepción HTTP de Retrofit
     * @return Exception con mensaje descriptivo
     */
    private fun mapHttpException(exception: HttpException): Exception {
        return when (exception.code()) {
            401 -> Exception("API Key inválida. Configura tu clave de Claude.")
            429 -> Exception("Demasiadas solicitudes. Intenta más tarde.")
            500, 502, 503 -> Exception("Error del servidor de Claude. Intenta más tarde.")
            else -> Exception("Error HTTP ${exception.code()}: ${exception.message()}")
        }
    }
}
